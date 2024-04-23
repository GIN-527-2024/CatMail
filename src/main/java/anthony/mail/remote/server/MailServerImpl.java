package anthony.mail.remote.server;

import anthony.mail.usable.Account;
import anthony.mail.usable.Mail;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static anthony.mail.usable.ErrorCodes.*;

public class MailServerImpl extends UnicastRemoteObject implements MailServer {


    private final String USER_ACCOUNT_PATH = "../../../data/server/users.ser";
    private final String MESSAGE_PATH = "../../../data/server/messages.ser";

    public MailServerImpl(String name) throws RemoteException{
        super();

        try{
            Naming.rebind(name, this);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    @Override
    public void setup() throws RemoteException {

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

        Account account = new Account(name, email, password);
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
    public Mail[] RecievedEmail(Account account) throws RemoteException {
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

                    if(obj.getTo().equals(account.getEmail()))mails.add(obj);

                } catch (EOFException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        return (Mail[]) mails.toArray();
    }

    @Override
    public Mail[] SentEmail(Account account) throws RemoteException {
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

                    if(obj.getFrom().equals(account.getEmail()))mails.add(obj);

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
