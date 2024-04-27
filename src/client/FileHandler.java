package client;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import usable.Account;
import usable.Mail;

public class FileHandler {
    private static final String USER_ACCOUNT_PATH = "data/client";

    // saveToOutbox and saveToInbox are given all the emails to rewrite the file.
    public static void saveToOutbox(Mail[] mails) {
        String outboxPath = USER_ACCOUNT_PATH + "/" + mails[0].getFrom() + "/outbox.ser";
        File file = new File(outboxPath);

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

                // Write each mail object to the outbox file
                objectOut.writeObject(mails);
                System.out.println("Mails saved to outbox successfully.");

        } catch (IOException e) {
            System.err.println("Error occurred while saving mail to outbox: " + e.getMessage());
        }
    }

    public static void addToOutbox(Mail mail) {
        String outboxPath = USER_ACCOUNT_PATH + "/" + mail.getFrom() + "/outbox.ser";
        File file = new File(outboxPath);

        try {
            // Read existing outbox mails from file
            Mail[] existingOutbox = getOutbox(mail.getFrom());

            // Append the new mail to the existing outbox array
            Mail[] updatedOutbox = Arrays.copyOf(existingOutbox, existingOutbox.length + 1);
            updatedOutbox[existingOutbox.length] = mail;

            // Write the updated outbox array to the file
            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

                objectOut.writeObject(updatedOutbox);
                System.out.println("Mail saved to outbox successfully.");

            } catch (IOException e) {
                System.err.println("Error occurred while saving mail to outbox: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error occurred while getting existing outbox mails: " + e.getMessage());
        }
    }



    public static void saveToInbox(Mail[] mails) {
        String inboxPath = USER_ACCOUNT_PATH + "/" + mails[0].getTo() + "/inbox.ser";
        File file = new File(inboxPath);

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

                // Write each mail object to the inbox file
                objectOut.writeObject(mails);
                System.out.println("Mails saved to inbox successfully.");

        } catch (IOException e) {
            System.err.println("Error occurred while saving mail to inbox: " + e.getMessage());
        }
    }

    // is given one draft to save
    public static void saveToDrafts(Mail draft) {
        String draftsPath = USER_ACCOUNT_PATH + "/" + draft.getFrom() + "/drafts.ser";
        File file = new File(draftsPath);
        Mail[] existingDrafts = getDrafts(draft.getFrom());
        Mail[] updatedDrafts;
        try {

            if(existingDrafts.length != 0) {
                // Append the new draft to the existing drafts array
                updatedDrafts = Arrays.copyOf(existingDrafts, existingDrafts.length + 1);
                updatedDrafts[existingDrafts.length] = draft;
            } else { // if no drafts
                updatedDrafts = new Mail[]{draft};
            }

            // Write the updated drafts array to the file
            try (FileOutputStream fileOut = new FileOutputStream(file, false);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

                objectOut.writeObject(updatedDrafts);
                System.out.println("Mail saved to drafts successfully.");

            } catch (IOException e) {
                System.err.println("Error occurred while saving mail to drafts: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error occurred while getting existing drafts: " + e.getMessage());
        }
    }


    public static Mail[] getInbox(String email) {
        String inboxPath = USER_ACCOUNT_PATH + "/" + email + "/inbox.ser";
        File file = new File(inboxPath);
        Mail[] inbox = new Mail[0];

        if (!file.exists()) {
            // inbox file doesn't exist, return an empty array
            return inbox;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn))) {

            // Read the array of mails from the inbox file
            try {
                inbox = (Mail[]) objectIn.readObject();
            } catch (EOFException e) {
                // Do nothing, EOFException indicates end of file
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading inbox: " + e.getMessage());
        }

        return inbox;
    }

    public static Mail[] getOutbox(String email) {
        String outboxPath = USER_ACCOUNT_PATH + "/" + email + "/outbox.ser";
        File file = new File(outboxPath);
        Mail[] outbox = new Mail[0];

        if (!file.exists()) {
            // Outbox file doesn't exist, return an empty array
            return outbox;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn))) {

            // Read the array of mails from the outbox file
            try {
                outbox = (Mail[]) objectIn.readObject();
            } catch (EOFException e) {
                // Do nothing, EOFException indicates end of file
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading outbox: " + e.getMessage());
        }

        return outbox;
    }

    public static Mail[] getDrafts(String email) {
        String draftsPath = USER_ACCOUNT_PATH + "/" + email + "/drafts.ser";
        File file = new File(draftsPath);
        Mail[] drafts = new Mail[0];
        if (!file.exists()) {
            // drafts file doesn't exist, return an empty array
            return drafts;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn))) {
                try {
                    drafts = (Mail[]) objectIn.readObject();
                } catch (EOFException e) {
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while reading drafts: " + e.getMessage());
        }

        return drafts;
    }
}

