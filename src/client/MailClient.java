package client;
import java.nio.file.attribute.FileAttribute;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import server.MailServer;
import usable.Account;
import static usable.ErrorCodes.*;

import java.io.File;
import java.rmi.*;

import usable.Mail;

public class MailClient {
    static MailServer mailServerProxy;

    public static MailServer initiateConnection(String address, String name) {
        try {
            Registry registry = LocateRegistry.getRegistry(address, 1177);

            MailServer serverProxy = (MailServer) registry.lookup(name);



            mailServerProxy = serverProxy;
            return serverProxy;
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
            return null;
        }
    }

    public static boolean loginRemote(String email, String password) {
        try {
            // Assuming login is successful, you can create the directory if it doesn't exist
        if (!mailServerProxy.login(email, password)) {
            System.out.println("invalid username or password");
            return false;
        }

            // Check if the directory exists, if not, create it
            String directoryPath = "data/client/" + email;
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    System.err.println("Failed to create directory: " + directoryPath);
                    return false;
                }
                System.out.println("Directory created: " + directoryPath);
            } else {
                System.out.println("Directory already exists: " + directoryPath);
            }
            return true; // Login successful
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
        return false; // Login failed
    }

    public static boolean registerRemote(String name, String email, String password) {
        boolean result = false;
        try {
            int err = mailServerProxy.registerEmail(name, email, password);


             if(err == NO_ERROR.getCode()) result = true;
             else{
                 System.out.println(displayMessage(err));
             }

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
            return false;
        }
        return result;
    }

    public static void sendEmailRemote(Mail mail) {
        try {
            if (mailServerProxy.send(mail) == NO_ERROR.getCode()) {
                Mail[] toSave = {mail};
                FileHandler.saveToOutbox(toSave);
                System.out.println("Email sent successfully.");
                return;
            }
            if (mailServerProxy.send(mail) == USER_DOES_NOT_EXIST.getCode()){
                System.out.println("The mail is sent to an unknown user, try again...");
                return;
            }
            System.out.println("Could not send email (invalid format)");
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

    // a way to end the connection when the client has finished.
    public static void destroyConnection() {
        mailServerProxy = null;
    }

    // a way to get the client emails
//    public static Mail[] getEmailsRemote(Account user) {
//        try {
//            return mailServerProxy.RecievedEmail(user);
//        } catch (Exception e) {
//            System.err.println("Remote Exception: " + e);
//        }
//        return null;
//    }



    public static void refreshInbox(Account user) {
        try {
            Mail[] inboxLocal = FileHandler.getInbox(user.getEmail());
            Timestamp newest = new Timestamp(0);
            if(inboxLocal.length != 0) {
                newest = inboxLocal[inboxLocal.length - 1].getTimestamp();
            }


            Mail[] inboxRemote = mailServerProxy.RecievedEmail(user, newest);
            if(inboxRemote.length == 0) {
                System.out.println("Inbox aleardy up to date");
                return;
            }

            // Call saveToInbox with the new emails
            FileHandler.saveToInbox(inboxRemote);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }


    public static void refreshOutbox(Account user) {
        try {
            Mail[] outboxLocal = FileHandler.getOutbox(user.getEmail());
            Timestamp newest = new Timestamp(0);
            if(outboxLocal.length != 0) {
                newest = outboxLocal[outboxLocal.length - 1].getTimestamp();
            }


            Mail[] outboxRemote = mailServerProxy.RecievedEmail(user, newest);
            if(outboxRemote.length == 0) {
                System.out.println("Outbox aleardy up to date");
                return;
            }

            // Call saveToInbox with the new emails
            FileHandler.saveToOutbox(outboxRemote);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

    public static void logOut() {

    }
}