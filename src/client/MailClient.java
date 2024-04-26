package client;
import server.MailServer;
import usable.Account;
import usable.Mail;

import static usable.ErrorCodes.*;

import java.rmi.*;
import java.sql.Timestamp;

public class MailClient {
    static MailServer mailServerProxy;

    public static MailServer initiateConnection(String address, String name) {
        try {
            MailServer serverProxy = (MailServer) Naming.lookup("rmi://" + address + "/" + name);
            mailServerProxy = serverProxy;
            return serverProxy;
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
            return null;
        }
    }

    public static boolean loginRemote(String email, String password) {
        try {
//            if (!mailServerProxy.login(email, password)) {
//                System.out.println("invalid username or password");
//                return false;
//            }
            return true;
        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
        return false;
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
            Mail[] inboxLocal = FileHandler.getInbox(user);
            Timestamp newest = inboxLocal[inboxLocal.length - 1].getTimestamp();

            Mail[] inboxRemote = mailServerProxy.RecievedEmail(user, newest);

            FileHandler.saveToInbox(inboxRemote);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

    public static void refreshOutbox(Account user) {
        try {
            Mail[] outboxLocal = FileHandler.getOutbox(user);
            Timestamp newest = outboxLocal[outboxLocal.length - 1].getTimestamp();

            Mail[] outboxRemote = mailServerProxy.SentEmail(user, newest);

            FileHandler.saveToOutbox(outboxRemote);

        } catch (Exception e) {
            System.err.println("Remote Exception: " + e);
        }
    }

}