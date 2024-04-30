package client;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import usable.AppendableObjectOutputStream;
import usable.Mail;

public class FileHandler {
    static final String USER_ACCOUNT_PATH = "./Client_data";
    static final String OUTBOX_PATH = "/outbox.ser";
    static final String INBOX_PATH = "/inbox.ser";
    static final String DRAFT_PATH = "/drafts.ser";

    public static void createInitialFiles(){
        try {
            File clientdir=new File(USER_ACCOUNT_PATH);
            if(clientdir.exists()){
                return;
            }

            boolean b=clientdir.mkdir();
            System.out.println("Client_Data: " +b);
        } catch(Exception e){
            System.out.println("Error creating initial files: " + e.getMessage());
        }

    }
    // saveToOutbox and saveToInbox are given all the emails to rewrite the file.
    public static void saveInFile(Mail[] mails, String slashedPath) {
        boolean isFrom = slashedPath.equals(OUTBOX_PATH) || slashedPath.equals(DRAFT_PATH);

        String email = isFrom? mails[0].getFrom() : mails[0].getTo();
        String outboxPath = USER_ACCOUNT_PATH + "/" + email + slashedPath;
        File file = new File(outboxPath);
        boolean append = file.exists();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file, append);
            AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, append);
            for (Mail mail : mails) {
                objectOut.writeObject(mail);
            }

            System.out.println("Mails successfully saved in: " + outboxPath );

        } catch (IOException e) {
            System.err.println("Error occurred while saving mails: " + outboxPath);
            e.printStackTrace();
        }
    }


    public static Mail[] getFromPath(String email, String slashedPath) {
        List<Mail> inbox = new ArrayList<>();
        String inboxPath = USER_ACCOUNT_PATH + "/" + email + slashedPath;
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
            System.err.println("Error occurred while reading from: " + inboxPath);
            e.printStackTrace();
        }

        // Convert the list of mails to an array and return
        return inbox.toArray(new Mail[0]);
    }

    public static void deleteFromDrafts(Mail mail, int index) {
        String draftsPath = USER_ACCOUNT_PATH + "/" + mail.getFrom() + DRAFT_PATH;
        File file = new File(draftsPath);
        Mail[] draftsArr = FileHandler.getFromPath(mail.getFrom(), DRAFT_PATH);

        ArrayList<Mail> draftsList = new ArrayList<>(Arrays.asList(draftsArr));

        // Check if index is valid
        if (index >= 0 && index < draftsList.size()) {
            // Remove the draft at the specified index
            draftsList.remove(index);

            // Write each object one by one to the drafts file
            try {
                FileOutputStream fileOut = new FileOutputStream(file, false);
                AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, false);
                for (Mail draft : draftsList) {
                    objectOut.writeObject(draft);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid index. Draft not deleted.");
        }
    }
}