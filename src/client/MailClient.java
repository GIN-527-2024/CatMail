package client;
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

    static Mail[] inbox;
    static Mail[] outbox;
    static Mail[] drafts;
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
//        if (!mailServerProxy.login(email, password)) {
//            System.out.println("invalid username or password");
//            return false;
//        }

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
            inbox = FileHandler.getInbox(email);
            outbox = FileHandler.getOutbox(email);
            drafts = FileHandler.getDrafts(email);
            return true; // Login successful
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
        return false; // Login failed
    }

    public static boolean registerRemote(String name, String email, String password) {
        try {
            int err = mailServerProxy.registerEmail(name, email, password);
            if (err == NO_ERROR.getCode()) {
                return true;
            } else if (err == INVALID_NAME.getCode()) {
                System.out.println((INVALID_EMAIL.getMessage()));
                return false;
            } else if (err == INVALID_NAME.getCode()) {
                System.out.println((INVALID_EMAIL.getMessage()));
                return false;
            } else if (err == INVALID_PASSWORD.getCode()) {
                System.out.println(INVALID_PASSWORD.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
            return false;
        }
        return true;
    }

    public static void sendEmailRemote(Mail mail) {
        try {
            if (mailServerProxy.send(mail)) {
                System.out.println("Email sent successfully.");
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
            Mail[] inboxLocal = inbox;
            Timestamp newest = new Timestamp(0);
            if(inboxLocal.length != 0) {
                newest = inboxLocal[inboxLocal.length - 1].getTimestamp();
            }


            Mail[] inboxRemote = mailServerProxy.RecievedEmail(user, newest);
            if(inboxRemote.length == 0) {
                System.out.println("Inbox aleardy up to date");
                return;
            }
            // Create a new array to hold the combined contents
            Mail[] combinedInbox = new Mail[inboxLocal.length + inboxRemote.length];

            // Copy the contents of inboxLocal into the combinedInbox
            System.arraycopy(inboxLocal, 0, combinedInbox, 0, inboxLocal.length);

            // Append the contents of inboxRemote to the end of combinedInbox
            System.arraycopy(inboxRemote, 0, combinedInbox, inboxLocal.length, inboxRemote.length);

            // Replace the reference of inboxLocal with the combined array
            inbox = combinedInbox;

            // Call saveToInbox with the combined array
            FileHandler.saveToInbox(combinedInbox);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }


    public static void refreshOutbox(Account user) {
        try {
            Mail[] outboxLocal = FileHandler.getOutbox(user.getEmail());
            Timestamp newest = outboxLocal[outboxLocal.length - 1].getTimestamp();

            Mail[] outboxRemote = mailServerProxy.SentEmail(user, newest);

            // Create a new array to hold the combined contents
            Mail[] combinedOutbox = new Mail[outboxLocal.length + outboxRemote.length];

            // Copy the contents of outboxLocal into the combinedOutbox
            System.arraycopy(outboxLocal, 0, combinedOutbox, 0, outboxLocal.length);

            // Append the contents of outboxRemote to the end of combinedOutbox
            System.arraycopy(outboxRemote, 0, combinedOutbox, outboxLocal.length, outboxRemote.length);

            // Replace the reference of outboxLocal with the combined array
            FileHandler.saveToOutbox(combinedOutbox);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

    public static void logOut() {

    }

    public static void reloadDrafts(String email) {
        drafts = FileHandler.getDrafts(email);
    }


}