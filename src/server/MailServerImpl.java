package server;



import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import usable.Account;
import usable.AppendableObjectOutputStream;
import static usable.ErrorCodes.*;
import usable.Mail;

public class MailServerImpl extends UnicastRemoteObject implements MailServer {


    private final String SERVER_DIRECTORY = "./Server_Data";
    private final String USER_ACCOUNT_PATH = SERVER_DIRECTORY + "/users.ser";
    private final String MAIL_PATH = SERVER_DIRECTORY + "/mails.ser";

    public MailServerImpl() throws RemoteException{
        super();
        try {
            File serverdir = new File(SERVER_DIRECTORY);
            if(serverdir.exists()){
                System.out.println("already exists");
            }
    
            boolean b= serverdir.mkdir();
            System.out.println("Server_Data: " +b);
        } catch (Exception e) {
            System.out.println("Error creating initial files: " + e.getMessage());
        }

    }



    @Override
    public int registerEmail(String email, String password) throws RemoteException {



        /*final int MIN_NAME_SIZE = 3;


        if(name.length() <= MIN_NAME_SIZE ) return INVALID_NAME.getCode();*/

        if(!Account.isValidEmail(email)) return INVALID_EMAIL.getCode();
        if(userFound(email)) return EMAIL_TAKEN.getCode();

        if(!isStrongPassword(password)) return INVALID_PASSWORD.getCode();

        Account account = new Account(email, password);
        try {
            saveInFile(USER_ACCOUNT_PATH,account);
        } catch (IOException e) {
            e.printStackTrace(); //alternative for System.out.println(e.getMessage())
            return UNEXPECTED_ERROR.getCode();
        }

        //INTENDED TO BE DISPLAYED AT THE SERVER PART
        System.out.println( account + " is successfully created");

        return NO_ERROR.getCode();

    }

    //this function intends to verify the user in the login process
    @Override
    public boolean login(String email, String password) throws RemoteException {
        return search(USER_ACCOUNT_PATH, email, password);
    }

    @Override
    public Mail[] RecievedEmail(Account account, Timestamp newest) throws RemoteException {
        ArrayList<Mail> mails = new ArrayList<>();

        File file = new File(MAIL_PATH);
        if (!file.exists()) return new Mail[0]; //return an empty mail array
        try {
            FileInputStream  fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);


            while(true){

                try {
                    Mail obj = (Mail) ois.readObject();

                    if(!obj.getTimestamp().after(newest)) continue;

                    if(obj.getTo().equals(account.getEmail()) ) mails.add(obj);

                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        return mails.isEmpty() ? new Mail[0] : mails.toArray(new Mail[0]);
    }

    @Override
    public int send(Mail mail) throws RemoteException{
        if(!userFound(mail.getTo())) return USER_DOES_NOT_EXIST.getCode();

        try {
            saveInFile(MAIL_PATH, mail);
        } catch (IOException e) {
            e.printStackTrace();
            return UNEXPECTED_ERROR.getCode();
        }

        return NO_ERROR.getCode();
    }





    public Mail[] SentEmail(Account account, Timestamp newest) throws RemoteException {
        ArrayList<Mail> mails = new ArrayList<>();

        File file = new File(MAIL_PATH);
        if (!file.exists()) return new Mail[0]; //return an empty mail array


        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(true){

                try {
                    Mail obj = (Mail) ois.readObject();

                    if(!obj.getTimestamp().after(newest)) continue;


                    if(obj.getFrom().equals(account.getEmail())) mails.add(obj);

                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //if the mail array is empty a runtime error will be raised: ClassCastException
        return mails.isEmpty() ? new Mail[0] : mails.toArray(new Mail[0]);
    }

    //this method will search in the users file to check if the user is found
    private boolean userFound(String userEmail)  {
       return  search(USER_ACCOUNT_PATH, userEmail);

    }

 /* public Mail[] getEmails(Account user) throws RemoteException{
        try{
            File userFolder = new File("/users/"+user.getEmail());
            File[] files = userFolder.listFiles();
            Mail[] mails = new Mail[files.length];
            for (int i =0; i<files.length;i++){
                for(int j =0;j<files.length;j++){
                    FileInputStream fis = new FileInputStream(files[j]);
                    ObjectInputStream obj = new ObjectInputStream(fis);
                    mails[j] = (Mail)obj.readObject();
                    obj.close();
                    fis.close();
                }
            }
            return mails;
        }catch(IOException e){
            System.out.println(e);
            return null;
        }catch(ClassNotFoundException e){
            System.out.println(e);
            return null;
        } */
        





    //all emails should be stored in lower case

    private <T> void saveInFile(String path,T object) throws IOException {

        if(object instanceof Mail) ((Mail) object).setTimestamp(System.currentTimeMillis());

        File file = new File(path);
        boolean append = file.exists();
        FileOutputStream fos = new FileOutputStream(file, append);
        AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos, append);

         oos.writeObject(object);


    }





    private  boolean isStrongPassword(String password) {
        // Check length
        if (password.length() < 8) {
            return false;
        }

        // Check for uppercase, lowercase, digits, and special characters
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern digitPattern = Pattern.compile("[0-9]");
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()\\-+=]");

        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        Matcher digitMatcher = digitPattern.matcher(password);
        Matcher specialCharMatcher = specialCharPattern.matcher(password);

        return upperCaseMatcher.find() && lowerCaseMatcher.find() && digitMatcher.find() && specialCharMatcher.find();
    }


   

    private boolean search(String path,String userEmail){

        userEmail = userEmail.toLowerCase();
        File file = new File(path);
        if(!file.exists()) return false;
        try {
            FileInputStream fis  = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);


            boolean exists;


            while(true){

                try {
                    Account obj = (Account) ois.readObject();
                    exists = obj.getEmail().equals(userEmail);
                    if(exists) return true;

                    System.out.println(obj);
                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        //the user is not found
        return false;
    }
  
    private boolean search(String path, String userEmail, String password){

        userEmail = userEmail.toLowerCase();
        File file = new File(path);
        if(!file.exists()) return false;

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);


            boolean exists;


            while(true){

                try {
                    Account obj = (Account) ois.readObject();
                    exists = obj.getEmail().equals(userEmail) && obj.getPassword().equals(password);

                    if(exists) return true;

                    System.out.println(obj);
                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        //the user is not found
        return false;
    }
}
