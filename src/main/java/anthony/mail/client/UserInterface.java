package anthony.mail.client;

import anthony.mail.remote.server.MailServer;
import anthony.mail.client.MailClient;
import anthony.mail.usable.Account;
import anthony.mail.usable.Mail;

import java.util.Scanner;

public class UserInterface {
    private boolean isLoggedIn = false;
    static MailServer mailServerProxy;
    static Scanner scanner = new Scanner(System.in);
    public static void displayMenu(MailServer serverProxy) {
        mailServerProxy = serverProxy;
        boolean running = true;
        String input;
        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");
            input = scanner.next();
            if (input.equals("1")) {
                registerInterface();
            } else if (input.equals("2")) {
                loginInterface();
            } else if (input.equals("3")) {
                System.exit(0);
            }
        }
    }

    private static void registerInterface() {
        boolean registered = false;

        System.out.println("Enter name: ");
        String regName = scanner.next();

        System.out.println("Enter email: ");
        String regEmail = scanner.next();

        System.out.println("Enter password: ");
        String regPassword = scanner.next();

        registered = MailClient.registerRemote(regName, regEmail, regPassword);

        if (!registered) {
            System.out.println("Could not register.");
        } else {
            System.out.println("User " + regEmail + " registered successfully");
        }
    }

    private static void loginInterface() {
        boolean success = false;

        System.out.print("Enter email: ");
        String regEmail = scanner.next();

        System.out.print("Enter password: ");
        String regPassword = scanner.next();

        Account user = new Account(regEmail, regPassword);
        success = MailClient.loginRemote(user);

        if (!success) {
            System.out.println("Could not login.");
            return;
        } else {
            System.out.println("User " + regEmail + " logged in successfully.");
            sendEmailInterface(user);
        }
    }

    public static void loggedInInterface(Account user) {
        String input;
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Create Email");
            System.out.println("2. View Inbox");
            System.out.println("3. View Outbox");
            System.out.println("4. Back");
            input = scanner.next();
            switch (input) {
                case "1":
                    sendEmailInterface(user);
                    break;
                case "2":
                    viewInbox(user); // todo
                    break;
                case "3":
                    viewOutbox(user); // todo
                    break;
                case "4":
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void sendEmailInterface(Account user) {
        String to, subject, text, input;
        System.out.println("Creating email... --savedraft to save.");
        System.out.println("To: ");
        to = scanner.next();

        if (to.equals("--savedraft")) {
            // save draft
            return;
        }
        System.out.println("Subject: ");
        subject = scanner.next();

        if (subject.equals("--savedraft")) {
            // save draft
            return;
        }

        System.out.println("Text: ");
        text = scanner.next();

        if (text.equals("--savedraft")) {
            // save draft
            return;
        }
        Mail email = new Mail(user.getEmail(), to, subject, text);
        System.out.println("Done composing. --send or --savedraft");
        do {
            System.out.println("--send or --savedraft");
            input = scanner.next();
        }
        while (!input.equals("--savedraft") || !input.equals("--send"));
        if(input.equals("--send"))
            MailClient.sendEmailRemote(email);
        else
            // save draft
            return;
    }

    private static void viewInbox(Account user) {
        System.out.println("Inbox: ");
        Mail[] inbox = FileHandler.getInbox(user);
        // display emails stored locally
        for(Mail mail: inbox) {
            System.out.println(mail.getSubject() +"\nFrom: " +mail.getFrom());
        }

    }

   private static void displayEmailsInterface(Account user) {
        System.out.println("Emails: ");
        try{
            Mail[] mails= mailServerProxy.getEmails(user);
            for (Mail mail: mails){
             System.out.println(mail);
            }
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
        }
    
    }


}
