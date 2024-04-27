package server;



import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import usable.Account;
import static usable.ErrorCodes.*;
import usable.Mail;

public class MailServerImpl extends UnicastRemoteObject implements MailServer {


    private final String USER_ACCOUNT_PATH = "users.ser";
    private final String MESSAGE_PATH = "../../../data/server/messages.ser";

    public MailServerImpl() throws RemoteException{
        super();

        try{
            //
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    @Override
    public void setup() throws RemoteException {

    }
    public boolean login(Account user) throws RemoteException{
        String email = user.getEmail();
        String password = user.getPassword();
        validateAccount(email, password);

        return false;
    }

    private boolean validateEmail(String email) {
        return !userFound(email) && Account.isValidEmail(email);
    }

    @Override
    public int registerEmail(String name, String email, String password) throws RemoteException {



        final int MIN_NAME_SIZE = 3;


        if(name.length() <= MIN_NAME_SIZE ) return INVALID_NAME.getCode();

        if(!validateEmail(email)) return INVALID_EMAIL.getCode();

        if(!isStrongPassword(password)) return INVALID_PASSWORD.getCode();

        Account account = new Account(email, password);
        saveEmail(account);

        //INTENDED TO BE DISPLAYED AT THE SERVER PART
        System.out.println( account + " is successfully created");

        return NO_ERROR.getCode();

    }

    //this function intends to verify the user in the login process
    @Override
    public boolean validateAccount(String email, String password) throws RemoteException {
        return search(USER_ACCOUNT_PATH, email, password);
    }

    @Override
    public Mail[] RecievedEmail(Account account, Timestamp newest) throws RemoteException {
        ArrayList<Mail> mails = new ArrayList<>();

        File file = new File(MESSAGE_PATH);
        try {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);


            boolean exists;


            while(true){

                try {
                    Mail obj = (Mail) ois.readObject();

                    if(obj.getTo().equals(account.getEmail()) && obj.getTimestamp().after(newest)) mails.add(obj);

                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        return (Mail[]) mails.toArray();
    }
    public boolean send(Mail mail) throws RemoteException{
      String to =mail.getTo();
      if(validateEmail(to)==false){
          return false;
      }
      String from =mail.getFrom();
      if(validateEmail(from)){
        return false;
      }
       
       File f = new File("/users/" + to + "/"+mail.getSubject()+".txt");
       File f2= new File("/users/"+from+"/"+mail.getSubject()+".txt" ); 
        // i think it would be better instead of subject if we add an id for each mail and store the mail by id

      try{
      FileOutputStream fos= new FileOutputStream(f);
      ObjectOutputStream obj =new ObjectOutputStream(fos);
      obj.writeObject(mail);
        obj.close();
        fos.close();
        FileOutputStream fos2= new FileOutputStream(f2);
        ObjectOutputStream obj2 =new ObjectOutputStream(fos2);
        obj2.writeObject(mail);
          obj2.close();
          fos2.close();     
        return true;
      }catch(IOException e){
          System.out.println(e);
          return false;
      }
    

    }

    @Override
    public Mail[] getEmails(Account user) throws RemoteException {
        return new Mail[0];
    }



    public Mail[] SentEmail(Account account, Timestamp newest) throws RemoteException {
        ArrayList<Mail> mails = new ArrayList<>();

        File file = new File(MESSAGE_PATH);
        try {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);


            boolean exists;


            while(true){

                try {
                    Mail obj = (Mail) ois.readObject();

                    if(obj.getFrom().equals(account.getEmail()) && obj.getTimestamp().after(newest)) mails.add(obj);

                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        return (Mail[]) mails.toArray();
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

    private void saveEmail(Account account){



        try {
        File file = new File(USER_ACCOUNT_PATH);
        FileOutputStream fos = null;
        //true is for append argument
            fos = new FileOutputStream(file, true);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

         oos.writeObject(account);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

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

    public boolean saveDraft(Mail draft) throws RemoteException{
        File f = new File("/users/"+draft.getFrom()+"/drafts/"+draft.getSubject()+".txt");
        try{
            FileOutputStream fos= new FileOutputStream(f);
            ObjectOutputStream obj =new ObjectOutputStream(fos);
            obj.writeObject(draft);
            obj.close();
            fos.close();
            return true;
          }catch(IOException e){
              System.out.println(e);
              return false;
          }
    }
   

    private boolean search(String path,String userEmail){

        userEmail = userEmail.toLowerCase();
        File file = new File(path);
        try {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
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
        try {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
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
