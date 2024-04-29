package client;

import java.io.IOException;
import java.util.Scanner;
import server.MailServer;
import usable.Account;
import usable.Mail;
import usable.TextColor;
import static usable.TextColor.printColored;

public class UserInterface {
    static MailServer mailServerProxy;
    static Scanner scanner = new Scanner(System.in);
    public static void displayMenu(MailServer serverProxy) {
        mailServerProxy = serverProxy;

        clearConsole();
        mailboxAscii(); //print a mailbox ascii art
        pause();
        FileHandler.createInitialFiles();

        String input;



        while (true) {
            clearConsole();
            printColored(TextColor.GREEN,"Choose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");
            input = scanner.next();
            if (input.equals("1")) {
                registerInterface();
            } else if (input.equals("2")) {
                loginInterface();
            } else if (input.equals("3")) {
                clearConsole();
                stop();

            }

            pause();
        }

    }

    private static void registerInterface() {
        boolean registered = false;

        clearConsole();


        printColored(TextColor.GREEN,"Enter your email: ");

        String regEmail = scanner.next();

        printColored(TextColor.GREEN,"Enter your password: ");

        String regPassword = scanner.next();

        registered = MailClient.registerRemote(regEmail, regPassword);

        if (!registered) {
            printColored(TextColor.RED_UNDERLINED, "Could not register");
        } else {
            printColored(TextColor.GREEN,"User " + regEmail + " registered successfully");
        }
    }

    private static void loginInterface() {
        boolean success = false;


        clearConsole();
        printColored(TextColor.GREEN,"Enter email: ");
        String regEmail = scanner.next();

        printColored(TextColor.GREEN,"Enter password: ");
        String regPassword = scanner.next();

        Account user = new Account(regEmail, regPassword);
        success = MailClient.loginRemote(regEmail, regPassword);

        if (!success) {
            printColored(TextColor.RED_UNDERLINED,"Incorrect account");

            return;
        } else {
            printColored(TextColor.GREEN,"User " + regEmail + " logged in successfully.");
            pause();
            loggedInInterface(user);
        }
    }

    public static void loggedInInterface(Account user) {
        String input;
        while (true) {
            clearConsole();
            printColored(TextColor.CYAN, user.getEmail());

            printColored(TextColor.GREEN, "Choose an option:");
            System.out.println("1. Create Email");
            System.out.println("2. View Inbox");
            System.out.println("3. View Outbox");
            System.out.println("4. View Drafts");
            System.out.println("5. Sign Out");
            input = scanner.next();
            switch (input) {
                case "1":
                    sendEmailInterface(user);
                    break;
                case "2":
                    inboxInterface(user);
                    break;
                case "3":
                    outboxInterface(user);
                    break;
                case "4":
                    draftsInterface(user);
                    break;
                case "5":
                    printColored(TextColor.GREEN, "YOU HAVE SUCCESSFULLY SIGNED OUT");
                    MailClient.logOut();
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }
    }
   
    private static void sendEmailInterface(Account user) {
       
        Mail email = new Mail(user);
       

        clearConsole();
        editEmail(user,email);
       
    }
    private static void editEmail(Account user,Mail email){
        String to, subject, text = "", input;
        Scanner scannerQ = new Scanner(System.in);
        scannerQ.useDelimiter("/q");
        int option = 0;
        boolean lp = true;
        while(lp){
            clearConsole();

            printColored(TextColor.YELLOW, "1- To: " + TextColor.CYAN.getCode() +email.getTo());
            printColored(TextColor.YELLOW, "2- Subject: " + TextColor.CYAN.getCode() +email.getSubject());
            printColored(TextColor.YELLOW, "3- Text: "  + TextColor.CYAN.getCode()
                    + (email.getText()
                    .split("\n")[0] == null ? "" : email.getText()
                    .split("\n")[0]  + "..."));
            printColored(TextColor.YELLOW,"4- Done composing");
            printColored(TextColor.YELLOW, "5- Discard");
            printColored(TextColor.YELLOW,"6- save draft");




        try{
            option= scanner.nextInt();
        }catch(Exception e){
            printColored(TextColor.RED_BOLD, "The input is invalid" +
                    "\nPlease try again...");

            option = 0; //any value so that the default case is catched
        }

        switch(option){
        case 1:
            if(!email.getTo().isBlank()){
                printColored(TextColor.GREEN,"Do you want to edit the recipient? (y/n)");
                input = scanner.next().toLowerCase().substring(0,1);
                if(input.equals("n")){
                    break;
                }
            }
            printColored(TextColor.YELLOW,"To: ");
            to = scanner.next();

            email.setTo(to);
            break;
        case 2:
            if(!email.getSubject().isBlank()){
                System.out.println("Do you want to edit the subject? (y/n)");
                input = scanner.next().toLowerCase().substring(0,1);
                if(input.equals("n")){
                    break;
                }
            }
            scanner.nextLine(); //to read any extra values from before
            printColored(TextColor.YELLOW,"Subject: ");
            subject = scanner.nextLine();

            email.setSubject(subject);
            break;
        case 3:
             if(!email.getText().isBlank()){
                printColored(TextColor.GREEN,"Do you want to edit the text? (y/n)");
                input = scanner.next();
                if(input.equals("n")){
                    break;
                }
             }
            printColored(TextColor.GREEN,"Enter email body (enter /q to exit): ");

            text = scannerQ.next();


            email.setText(text);
            break;
        case 4:
            if( email.getTo().isBlank() || email.getSubject().isBlank()|| email.getText().isBlank()){
                printColored(TextColor.RED,"Please fill all fields or save draft");

                pause();
                
                break;
            }
            lp = false;
            break;
       case 5:
            loggedInInterface(user);
            return;

            case 6:
                FileHandler.saveToDrafts(email);
                return;

            default:
                //just loop again
                break;
        }
        
        }
        sendEmail(user, email);
    
    }
    private  static void sendEmail(Account user,Mail email){
        int input;
        clearConsole();
        printColored(TextColor.GREEN,"Done composing, Choose an option");
        do {
            System.out.println("1-Send");
            System.out.println("2-Save draft");
            System.out.println("3-Discard");
            try {
                input = scanner.nextInt();
            } catch (Exception e) {
                printColored(TextColor.RED,"Invalid input");
                input = 0;
            }
           
        }  while ( !(input == 1 || input == 2 || input == 3));

            if(input==1){
                MailClient.sendEmailRemote(email);
                printColored(TextColor.GREEN, "The email has been sent successfully");
            }else if(input==2){
                FileHandler.saveToDrafts(email);

                printColored(TextColor.GREEN, "The email has been saved successfully");
            }else {
                email=null;
                printColored(TextColor.GREEN, "The email has been discarded");
            }
        pause();

    }
    private static void inboxInterface(Account user) {
        clearConsole();

        while(true) {
            int i = 0;
            int input;
            printColored(TextColor.GREEN, "Inbox: ");
            MailClient.refreshInbox(user);
            Mail[] inbox = FileHandler.getInbox(user.getEmail());
            if(inbox.length == 0) {
                printColored(TextColor.YELLOW,"Inbox empty, 0 to go back");
            }
            for (Mail mail: inbox) {
                System.out.print(++i + ": ");
                System.out.println(mail.shortForm());
            }
                System.out.println("Select mail to display, 0 to go back");
            try{
                input = scanner.nextInt();
            } catch (Exception e){
                //the input is not a number
                input = -1; // so that the user tries again
            }
            if(input == 0) return;
            if(input >= 1 && input <= inbox.length) {
                showEmail(inbox[input - 1]); // Adjust index since the displayed index starts from 1
            } else {
                printColored(TextColor.RED,"Invalid selection. Please choose an email from the list.");
            }
        }
    }


    private static void outboxInterface(Account user) {
        clearConsole();
        while(true) {
            int i = 0;
            int input;
            printColored(TextColor.GREEN, "outbox: ");
            MailClient.refreshOutbox(user);
            Mail[] outbox = FileHandler.getInbox(user.getEmail());
            if(outbox.length == 0) {
                printColored(TextColor.YELLOW,"Outbox empty. 0 to go back");
            }
            for (Mail mail: outbox) {
                System.out.print(++i + ": ");
                System.out.println(mail.shortForm());
            }
            printColored(TextColor.YELLOW,"Select mail to display, 0 to go back");
            try{
                input = scanner.nextInt();
            } catch (Exception e) {// Check if input is a number
                input = -1;
            }
            if(input == 0) return;
            if(input >= 1 && input <= outbox.length) {
                showEmail(outbox[input - 1]);
            } else {
                printColored(TextColor.RED,"Invalid selection. Please choose an email from the list.");
            }
        }
    }


    private static void draftsInterface(Account user) {
        clearConsole();
        Mail[] drafts = FileHandler.getDrafts(user.getEmail());
        int input;
        while(true) {
            int i = 0;
            System.out.println("drafts: ");
            for (Mail mail: drafts) {
                System.out.print(++i + ": ");
                System.out.println(mail.shortForm());
            }
            printColored(TextColor.YELLOW,"Select draft to edit, 0 to go back");
            try{
                input = scanner.nextInt();
            } catch (Exception e) {// Check if input is a number
                input = -1;
            }
            if(input == 0) return;
            if(input >= 1 && input <= drafts.length) {
                FileHandler.deleteFromDrafts(drafts[input-1], input-1);
                editEmail(user, drafts[input-1]);
            } else {
                printColored(TextColor.RED,"Invalid selection. Please choose a draft from the list.");
            }
        }
    }


    private static void showEmail(Mail mail) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String dateString = mail.getTimestamp().toLocalDateTime().format(formatter);
        clearConsole();
        System.out.println(mail.fullForm());
       pause();
    }

    public static void stop(){
        mailboxPicture();

        System.exit(0);
    }



    private static void clearConsole() {
        try {

            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ignore) {
            System.err.println(ignore.getMessage());

        }
    }

    public static void pause()  {
        Scanner input = new Scanner(System.in);
        printColored(TextColor.GOLD, "Press any key to continue");
        if(!input.hasNextLine()) input.nextLine();
        input.nextLine();
    }

//    https://www.asciiart.eu/miscellaneous/mailbox
    public static void mailboxAscii(){

        String ascii =
                "                                                _ __ ___   __ _(_) | |__   _____  __\n" +
                "                                                | '_ ` _ \\ / _` | | | '_ \\ / _ \\ \\/ /\n" +
                "                                                | | | | | | (_| | | | |_) | (_) >  < \n" +
                "                                                |_| |_| |_|\\__,_|_|_|_.__/ \\___/_/\\_\\";

        printColored(TextColor.BLUE, ascii);
    }

//    https://www.asciiart.eu/miscellaneous/mailbox
    public static void mailboxPicture() {
        String result =
                "         \033[38;5;208m[E]\033[0m\n" +
                        "     \033[38;5;130m.-|=====\033[38;5;208m-.\033[0m\n" +
                        "     \033[38;5;130m| \033[38;5;33m| MAIL \033[38;5;130m|\033[0m" + TextColor.MAGENTA.getCode() + "          Distributed system project: Mailbox with java rmi\n" + TextColor.reset() +
                        "     \033[38;5;130m|________|___\033[0m" + TextColor.MAGENTA.getCode() + "       Copyright © [Year] by Anthony Abisaid, Thierry Khoury, Charbel Azzi\n" + TextColor.reset() +
                        "          \033[38;5;130m||\033[0m\n" +
                        "          \033[38;5;130m||\033[0m\n" +
                        "          \033[38;5;130m||\033[0m   \033[38;5;196mwww\033[0m                \033[38;5;196m%%%\033[0m\n" +
                        "   \033[38;5;196mvwv\033[0m    \033[38;5;130m||\033[0m   \033[38;5;28m)_(\033[38;5;214m,;;;\033[38;5;220m,\033[0m        \033[38;5;214m,;\033[38;5;220m,\\_/ \033[38;5;196mwww\033[0m\n" +
                        "   \033[38;5;28m)_(\033[0m    \033[38;5;130m||\033[0m   \033[38;5;28m\\|/ \\_/\033[0m         \033[38;5;28m)_(\\|  \033[38;5;214m(_)\033[0m\n" +
                        "   \033[38;5;28m\\|/\033[0m   \033[38;5;130m \033[38;5;28m|| /\\\\|/ \\|/\033[0m         \033[38;5;28m\\| \\\\\\|// \\|\033[0m \n" +
                        "\033[38;5;28m___\\|//\033[38;5;130m   \033[38;5;28m||//_\\V/_\\|//_______\\\\\\|//V/\\\\\\|/__\033[0m";
        System.out.println(result);
    }





}
