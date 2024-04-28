package client;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import usable.Account;
import usable.AppendableObjectOutputStream;
import usable.Mail;

public class FileHandler {
    private static final String USER_ACCOUNT_PATH = "data/client";

    // saveToOutbox and saveToInbox are given all the emails to rewrite the file.
    public static void saveToOutbox(Mail[] mails) {
        String outboxPath = USER_ACCOUNT_PATH + "/" + mails[0].getFrom() + "/outbox.ser";
        File file = new File(outboxPath);
        boolean append = file.exists();

        try {
            FileOutputStream fileOut = new FileOutputStream(file, true);
            AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, append);
            for (Mail mail : mails) {
                objectOut.writeObject(mail);
            }
            System.out.println("Mails saved to outbox successfully.");

        } catch (IOException e) {
            System.err.println("Error occurred while saving mails to outbox: " + e.getMessage());
        }
    }

    public static void saveToInbox(Mail[] mails) {
        String inboxPath = USER_ACCOUNT_PATH + "/" + mails[0].getTo() + "/inbox.ser";
        File file = new File(inboxPath);
        boolean append = file.exists();

        try {
            FileOutputStream fileOut = new FileOutputStream(file, true);
            AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, append);
            for (Mail mail : mails) {
                objectOut.writeObject(mail);
            }
            System.out.println("Mails saved to inbox successfully.");

        } catch (IOException e) {
            System.err.println("Error occurred while saving mail to inbox: " + e.getMessage());
        }
    }

    // is given one draft to save
    public static void saveToDrafts(Mail draft) {
        String draftsPath = USER_ACCOUNT_PATH + "/" + draft.getFrom() + "/drafts.ser";
        File file = new File(draftsPath);
        boolean append = file.exists();

        try {
            FileOutputStream fileOut = new FileOutputStream(file, true);
            AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, append);

            objectOut.writeObject(draft);
            System.out.println("Mail saved to drafts successfully.");

        } catch (IOException e) {
            System.err.println("Error occurred while saving mail to drafts: " + e.getMessage());
        }
    }


    public static Mail[] getInbox(String email) {
        List<Mail> inbox = new ArrayList<>();
        String inboxPath = USER_ACCOUNT_PATH + "/" + email + "/inbox.ser";
        File file = new File(inboxPath);

        if (!file.exists()) {
            // Inbox file doesn't exist, return an empty array
            return new Mail[0];
        }

        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            // Read mails from the Inbox file until EOFException is thrown
            while (true) {
                try {
                    Mail mail = (Mail) objectIn.readObject();
                    inbox.add(mail);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading Inbox: " + e.getMessage());
        }

        // Convert the list of mails to an array and return
        return inbox.toArray(new Mail[0]);
    }

    public static Mail[] getOutbox(String email) {
        List<Mail> outbox = new ArrayList<>();
        String outboxPath = USER_ACCOUNT_PATH + "/" + email + "/outbox.ser";
        File file = new File(outboxPath);

        if (!file.exists()) {
            // Outbox file doesn't exist, return an empty array
            return new Mail[0];
        }

        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
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

    public static Mail[] getDrafts(String email) {
        List<Mail> drafts = new ArrayList<>();
        String draftsPath = USER_ACCOUNT_PATH + "/" + email + "/drafts.ser";
        File file = new File(draftsPath);

        if (!file.exists()) {
            // Inbox file doesn't exist, return an empty array
            return new Mail[0];
        }

        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            // Read mails from the Inbox file until EOFException is thrown
            while (true) {
                try {
                    Mail mail = (Mail) objectIn.readObject();
                    drafts.add(mail);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading drafts: " + e.getMessage());
        }

        // Convert the list of mails to an array and return
        return drafts.toArray(new Mail[0]);
    }
}
