package client;

import server.MailServer;
import usable.Account;
import usable.Mail;

import java.time.format.DateTimeFormatter;
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
        success = MailClient.loginRemote(regEmail, regPassword);

        if (!success) {
            System.out.println("Could not login.");
            return;
        } else {
            System.out.println("User " + regEmail + " logged in successfully.");
            loggedInInterface(user);
        }
    }

    public static void loggedInInterface(Account user) {
        String input;
        while (true) {
            System.out.println("Choose an option:");
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
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void sendEmailInterface(Account user) {
        String to, subject, text, input;
        Mail email = new Mail(user);
        System.out.println("Creating email... --savedraft to save.");
        System.out.println("To: ");
        to = scanner.next();

        if (to.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            return;
        }
        email.setTo(to);

        System.out.println("Subject: ");
        subject = scanner.next();

        if (subject.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            return;
        }
        email.setSubject(subject);

        System.out.println("Text: ");
        text = scanner.next();

        if (text.equals("--savedraft")) {
            FileHandler.saveToDrafts(email);
            return;
        }
        email.setText(text);

        System.out.println("Done composing. --send or --savedraft");
        do {
            System.out.println("--send or --savedraft");
            input = scanner.next();
        }
        while (!input.equals("--savedraft") || !input.equals("--send"));
        if(input.equals("--send"))
            MailClient.sendEmailRemote(email);
        else
            FileHandler.saveToDrafts(email);
            return;
    }

    private static void inboxInterface(Account user) {
        while(true) {
            int i = 0;
            String input;
            System.out.println("Inbox: ");
            MailClient.refreshInbox(user);
            Mail[] inbox = FileHandler.getInbox(user);
            for (Mail mail: inbox) {
                System.out.println(++i +": ");
                System.out.println("Subject: " +mail.getSubject() +"\nFrom: " +mail.getFrom());
            }
            System.out.println("Select mail to display, 0 to go back");
            do {
                input = scanner.next();
            } while(input.compareTo("0") < 0 || input.compareTo(new String(String.valueOf(inbox.length-1))) > 0);
            if(input.equals("0")) return;
            showEmail(inbox[Integer.parseInt(input)]);
        }
    }

    private static void outboxInterface(Account user) {
        while(true) {
            int i = 0;
            String input;
            System.out.println("outbox: ");
            MailClient.refreshOutbox(user);
            Mail[] outbox = FileHandler.getOutbox(user);
            for (Mail mail: outbox) {
                System.out.println(++i +": ");
                System.out.println("Subject: " +mail.getSubject() +"\nTo: " +mail.getTo());
            }
            System.out.println("Select mail to display, 0 to go back");
            do {
                input = scanner.next();
            } while(input.compareTo("0") < 0 || input.compareTo(new String(String.valueOf(outbox.length-1))) > 0);
            if(input.equals("0")) return;
            showEmail(outbox[Integer.parseInt(input)]);
        }
    }

    private static void draftsInterface(Account user) {
        while(true) {
            int i = 0;
            String input;
            System.out.println("drafts: ");
            Mail[] drafts = FileHandler.getDrafts(user);
            for (Mail mail: drafts) {
                System.out.println(++i +": ");
                System.out.println("Subject: " +mail.getSubject() +"\nTo: " +mail.getTo());
            }
            System.out.println("Select draft to edit, 0 to go back");
            do {
                input = scanner.next();
            } while(input.compareTo("0") < 0 || input.compareTo(new String(String.valueOf(drafts.length-1))) > 0);
            if(input.equals("0")) return;
            // todo: edit draft
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
}
