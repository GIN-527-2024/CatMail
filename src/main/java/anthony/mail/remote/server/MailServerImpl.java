package anthony.mail.remote.server;

import anthony.mail.usable.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;  
import java.io.IOException;  


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
    public void register(Account user ) throws RemoteException {

        String email=user.getEmail() ;
        // String name =user.getFullName() ;
        // String password=user.getPassword() ;

        //code to validate the full name
        save(user);
        new File("/users/"+email).mkdirs(); //here i am making a directory with a name property the email of the user.
       

        System.out.println("Your account is successfully created");

    }
    public boolean send(Mail mail) throws RemoteException{
      String to =mail.getTo();
      String from =mail.getFrom();
       new File("/users/"+to+"/"+from).mkdirs();
       File f = new File("/users/" + to + "/" + from + "/"+mail.getSubject()+".txt");
        // i think it would be better instead of subject if we add an id for each mail and store the mail by id

      try{
         FileOutputStream fos= new FileOutputStream(f);
      ObjectOutputStream obj =new ObjectOutputStream(fos);
      obj.writeObject(mail);
        obj.close();
        fos.close();
        return true;
      }catch(IOException e){
          System.out.println(e);
          return false;
      }
    

    }

    @Override
    public boolean validateAccount(String email, String password) throws RemoteException {
        return false;
    }

    //this method will search in the users file to check if the user is found
    private boolean userFound(String userEmail)  {
        return false;
    }
    public Mail[] getEmails() throws RemoteException{
        return null;
    }

    public boolean saveDraft(Mail draft) throws RemoteException{
        return false;
    }
   


    private void save(Account user){

    }





}
