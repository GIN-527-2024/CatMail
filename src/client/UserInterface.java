package client;

import server.MailServer;
import usable.Account;
import usable.Mail;
import usable.TextColor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static usable.TextColor.printColored;

public class UserInterface {
    private boolean isLoggedIn = false;
    static MailServer mailServerProxy;
    static Scanner scanner = new Scanner(System.in);
    public static void displayMenu(MailServer serverProxy) {
        mailServerProxy = serverProxy;
        pause();
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
                stop();
            }

            pause();
        }

    }

    private static void registerInterface() {
        boolean registered = false;

        clearConsole();
        System.out.println("Enter your name: ");
        String regName = scanner.next();

        System.out.println("Enter your email: ");

        String regEmail = scanner.next();

        System.out.println("Enter your password: ");

        String regPassword = scanner.next();

        registered = MailClient.registerRemote(regName, regEmail, regPassword);

        if (!registered) {
            printColored(TextColor.RED_UNDERLINED, "Could not register");
        } else {
            printColored(TextColor.GREEN,"User " + regEmail + " registered successfully");
        }
    }

    private static void loginInterface() {
        boolean success = false;
        clearConsole();
        System.out.print("Enter email: ");
        String regEmail = scanner.next();

        System.out.print("Enter password: ");
        String regPassword = scanner.next();

        Account user = new Account(regEmail, regPassword);
        success = MailClient.loginRemote(regEmail, regPassword);

        if (!success) {
            printColored(TextColor.RED_UNDERLINED,"Incorrect account");

            return;
        } else {
            printColored(TextColor.GREEN,"User " + regEmail + " logged in successfully.");
            loggedInInterface(user);
        }
    }

    public static void loggedInInterface(Account user) {
        String input;
        while (true) {
            clearConsole();
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
                    MailClient.logOut();
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void sendEmailInterface(Account user) {
        String to, subject, text, input;
        Mail email = new Mail(user);

        clearConsole();
        System.out.println("Creating email... --savedraft to save.");
        System.out.println("To: ");
        to = scanner.next();

        if (to.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            MailClient.reloadDrafts(user.getEmail());
            return;
        }
        email.setTo(to);

        System.out.println("Subject: ");
        subject = scanner.next();

        if (subject.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            MailClient.reloadDrafts(user.getEmail());
            return;
        }
        email.setSubject(subject);

        System.out.println("Text: ");
        text = scanner.next();

        if (text.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            MailClient.reloadDrafts(user.getEmail());
            return;
        }
        email.setText(text);

        System.out.println("Done composing. --send or --savedraft");
        do {
            System.out.println("--send or --savedraft");
            input = scanner.next();
        }
        while (!input.equals("--savedraft") && !input.equals("--send"));
        if(input.equals("--send"))
            MailClient.sendEmailRemote(email);
        else {
            FileHandler.saveToDrafts(email);
            MailClient.reloadDrafts(user.getEmail());
        }
            return;
    }

    private static void inboxInterface(Account user) {
        clearConsole();

        while(true) {
            int i = 0;
            String input;
            System.out.println("Inbox: ");
            MailClient.refreshInbox(user);
            Mail[] inbox = MailClient.inbox;
            if(inbox.length == 0) {
                System.out.println("Inbox empty");
                return;
            }
            for (Mail mail: inbox) {
                System.out.println(++i + ": ");
                System.out.println("Subject: " + mail.getSubject() + "\nFrom: " + mail.getFrom());
            }
            System.out.println("Select mail to display, 0 to go back");
            do {
                input = scanner.next();
            } while(!input.matches("[0-9]+")); // Check if input is a number
            int selection = Integer.parseInt(input);
            if(selection == 0) return;
            if(selection >= 1 && selection <= inbox.length) {
                showEmail(inbox[selection - 1]); // Adjust index since the displayed index starts from 1
            } else {
                System.out.println("Invalid selection. Please choose an email from the list.");
            }
        }
    }


    private static void outboxInterface(Account user) {
        clearConsole();
        while(true) {
            int i = 0;
            String input;
            System.out.println("outbox: ");
            MailClient.refreshOutbox(user);
            Mail[] outbox = MailClient.outbox;
            if(outbox.length == 0) {
                System.out.println("Outbox empty");
                return;
            }
            for (Mail mail: outbox) {
                System.out.println(++i + ": ");
                System.out.println("Subject: " + mail.getSubject() + "\nTo: " + mail.getTo());
            }
            System.out.println("Select mail to display, 0 to go back");
            do {
                input = scanner.next();
            } while(!input.matches("[0-9]+")); // Check if input is a number
            int selection = Integer.parseInt(input);
            if(selection == 0) return;
            if(selection >= 1 && selection <= outbox.length) {
                showEmail(outbox[selection - 1]);
            } else {
                System.out.println("Invalid selection. Please choose an email from the list.");
            }
        }
    }


    private static void draftsInterface(Account user) {
        clearConsole();
        Mail[] drafts = MailClient.drafts;
        while(true) {
            int i = 0;
            String input;
            System.out.println("drafts: ");
            for (Mail mail: drafts) {
                System.out.println(++i + ": ");
                System.out.println("Subject: " + mail.getSubject() + "\nTo: " + mail.getTo());
            }
            System.out.println("Select draft to edit, 0 to go back");
            do {
                input = scanner.next();
            } while(!input.matches("[0-9]+")); // Check if input is a number
            int selection = Integer.parseInt(input);
            if(selection == 0) return;
            if(selection >= 1 && selection <= drafts.length) {
                // todo: edit draft using drafts[selection - 1]
            } else {
                System.out.println("Invalid selection. Please choose a draft from the list.");
            }
        }
    }


    private static void showEmail(Mail mail) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String dateString = mail.getTimestamp().toLocalDateTime().format(formatter);
        System.out.println("Mail: ");
        System.out.println("From: " +mail.getFrom());
        System.out.println("To: " +mail.getTo());
        System.out.println("Subject: " +mail.getSubject());
        //System.out.println("Time: " +dateString);
        System.out.println("\n" +mail.getText());
        System.out.println("---------------\nEnd of email. enter any key to go back");
        scanner.next();
    }

    public static void stop(){
        printColored(TextColor.MAGENTA,"The application is off");
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
        input.nextLine();
    }



}
