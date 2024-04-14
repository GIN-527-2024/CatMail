package anthony.mail.remote.server;

import anthony.mail.usable.Account;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class MailServerImpl extends UnicastRemoteObject implements MailServer {

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
    public void registerEmail() throws RemoteException {

        Scanner input = new Scanner(System.in);
        String email ;
        String name  ;
        String password ;
        final int MIN_NAME_SIZE = 3;

        //code to validate the full name
        do{
            System.out.println("Enter your name: ");
            name = input.next();
        }while(name.length() <= MIN_NAME_SIZE );


        boolean validAccount;
        do {

            System.out.println("Enter your email: ");
            email = input.next();
            validAccount = validateEmail(email);

            if(!validAccount) System.err.println("The email is not in correct format:" +
                    "\nFormat: local-part@domain-part  (domain part includes the suffixes .com .edu");
        } while (!validAccount);

        boolean passwordMatch;
        boolean passwordformat;
        do{
            String dupPassword ;
            System.out.println("Enter your password");
            password = input.next();
            System.out.println("Enter you password again");
            dupPassword = input.next();

            passwordMatch = password.equals(dupPassword);
            passwordformat = isValidPassword(password);

            if(!passwordMatch) System.err.println("The passwords don't match" +
                    "\nTry Again....");
            if(!passwordformat) System.err.println("The passwords don't match" +
                    "\nTry Again....");

        }while(!passwordMatch ||  !passwordformat);

        Account account = new Account(name, email, password);
        saveEmail(account);

        System.out.println("Your account is successfully created");

    }

    @Override
    public boolean validateAccount(String email, String password) throws RemoteException {
        return false;
    }

    //this method will search in the users file to check if the user is found
    private boolean userFound(String userEmail)  {
        return false;
    }

    public static boolean isValidPassword(String password) {
        // Minimum 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Contains at least one special character (!, @, #, or $)
        if (!password.matches(".*[!@#$].*")) {
            return false;
        }

        // All criteria passed, password is valid
        return true;
    }



    private void saveEmail(Account email){

    }





}
