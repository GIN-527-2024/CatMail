package anthony.mail.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import anthony.mail.usable.*;

public class FileHandler {
    private static final String USER_ACCOUNT_PATH = "../../../data/client";

    public static void saveToOutbox(Mail[] mails) {
        for (Mail mail : mails) {
            String emailPath = USER_ACCOUNT_PATH + "/" + mail.getFrom() + "/outbox.ser";
            File file = new File(emailPath);

            try (FileOutputStream fileOut = new FileOutputStream(file, true);
                 ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(fileOut))) {

                // Append each mail object to the inbox file
                objectOut.writeObject(mail);
                System.out.println("Mail saved to inbox successfully.");

            } catch (IOException e) {
                System.err.println("Error occurred while saving mail to inbox: " + e.getMessage());
            }
        }
    }
    public static void saveToInbox(Mail[] mails) {
        for (Mail mail : mails) {
            String emailPath = USER_ACCOUNT_PATH + "/" + mail.getTo() + "/inbox.ser";
            File file = new File(emailPath);

            try (FileOutputStream fileOut = new FileOutputStream(file, true);
                 ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(fileOut))) {

                // Append each mail object to the inbox file
                objectOut.writeObject(mail);
                System.out.println("Mail saved to inbox successfully.");

            } catch (IOException e) {
                System.err.println("Error occurred while saving mail to inbox: " + e.getMessage());
            }
        }
    }

    public static Mail[] getInbox(Account user) {
        List<Mail> inbox = new ArrayList<>();
        String inboxPath = USER_ACCOUNT_PATH + "/" + user.getEmail() + "/inbox.ser";
        File file = new File(inboxPath);

        if (!file.exists()) {
            // Inbox file doesn't exist, return an empty array
            return new Mail[0];
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn))) {

            // Read mails from the inbox file until EOFException is thrown
            while (true) {
                try {
                    Mail mail = (Mail) objectIn.readObject();
                    inbox.add(mail);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading inbox: " + e.getMessage());
        }

        // Convert the list of mails to an array and return
        return inbox.toArray(new Mail[0]);
    }

    public static Mail[] getOutbox(String user) {
        List<Mail> outbox = new ArrayList<>();
        String outboxPath = USER_ACCOUNT_PATH + "/" + user + "/outbox.ser";
        File file = new File(outboxPath);

        if (!file.exists()) {
            // Outbox file doesn't exist, return an empty array
            return new Mail[0];
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn))) {

            // Read mails from the outbox file until EOFException is thrown
            while (true) {
                try {
                    Mail mail = (Mail) objectIn.readObject();
                    outbox.add(mail);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading outbox: " + e.getMessage());
        }

        // Convert the list of mails to an array and return
        return outbox.toArray(new Mail[0]);
    }

}
