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
        System.out.print("Enter email: ");
        String regEmail = scanner.next();

        System.out.print("Enter password: ");
        String regPassword = scanner.next();

        Account user = new Account(regEmail, regPassword);
        registered = MailClient.registerRemote(user);

        if (!registered) {
            System.out.println("Could not register.");
            return;
        } else {
            System.out.println("User " + regEmail + " registered successfully");
            return;
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

    private static void sendEmailInterface(Account user) {
        String to, subject, text;

        System.out.println("To: ");
        to = scanner.next();

        System.out.println("Subject: ");
        subject = scanner.next();

        System.out.println("Text: ");
        text = scanner.next();

        Mail email = new Mail(user.getEmail(), to, subject, text);

        MailClient.sendEmailRemote(email);

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
