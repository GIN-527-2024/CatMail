package client;
import java.io.File;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import server.MailServer;
import usable.Account;

import static client.FileHandler.INBOX_PATH;
import static client.FileHandler.OUTBOX_PATH;
import static usable.ErrorCodes.*;
import usable.Mail;

public class MailClient {
    static MailServer mailServerProxy;


    public static MailServer initiateConnection(String address, String port) throws NotBoundException, RemoteException {
        try {
            //if not a integer it will throw an exception and the catch below will catch it
            int PORT = Integer.parseInt(port);
            String remoteObject = "RemoteInterface";
            Registry registry = LocateRegistry.getRegistry(address, PORT);

            MailServer serverProxy = (MailServer) registry.lookup(remoteObject);



            mailServerProxy = serverProxy;
            return serverProxy;
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
            throw e;
        }
    }

    public static boolean loginRemote(String email, String password) {
        try {
            if (!mailServerProxy.login(email, password)) {
                return false;
            }


            // Check if the directory exists, if not, create it
            String directoryPath = FileHandler.USER_ACCOUNT_PATH +"/" + email;
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    System.err.println("Failed to create directory: " + directoryPath);
                    return false;
                }

            }
            return true; // Login successful
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
        return false; // Login failed
    }

    public static boolean registerRemote(String email, String password) {
        boolean result = false;
        try {
            int err = mailServerProxy.registerEmail(email, password);


             if(err == NO_ERROR.getCode()) result = true;
             else{
                 System.out.println(displayMessage(err));
             }

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);

        }
        return result;
    }

    public static void sendEmailRemote(Mail mail) {
        try {
            if (mailServerProxy.send(mail) == NO_ERROR.getCode()) {
                    Mail[] toSave = new Mail[1];
                    toSave[0] = mail;
                FileHandler.saveInFile(toSave,OUTBOX_PATH);
                System.out.println("Email sent successfully.");
                return;
            }
        if (mailServerProxy.send(mail) == USER_DOES_NOT_EXIST.getCode()){
                System.out.println("The mail is sent to an unknown user, try again...");
                return;
            }
            System.out.println("Could not send email (invalid format)");
        } catch (RemoteException e) {
            e.printStackTrace();
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
            Mail[] inboxLocal = FileHandler.getFromPath(user.getEmail(), INBOX_PATH);
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
            FileHandler.saveInFile(inboxRemote, INBOX_PATH);


        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }


    public static void refreshOutbox(Account user) {
        try {
            Mail[] outboxLocal = FileHandler.getFromPath(user.getEmail(), OUTBOX_PATH);

            Timestamp newest = new Timestamp(0);
            if(outboxLocal.length != 0) {
                newest = outboxLocal[outboxLocal.length - 1].getTimestamp();
            }


            Mail[] outboxRemote = mailServerProxy.SentEmail(user, newest);
            if(outboxRemote.length == 0) {
                System.out.println("Outbox already up to date");
                return;
            }

            // Call saveToInbox with the new emails
            FileHandler.saveInFile(outboxRemote, OUTBOX_PATH);


        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

    public static void logOut() {

    }
}