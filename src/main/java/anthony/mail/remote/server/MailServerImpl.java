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
    public boolean register(Account user ) throws RemoteException {

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
    public boolean validateAccount(String email, String password) throws RemoteException {
        return false;
    }

    //this method will search in the users file to check if the user is found
    private boolean userFound(String userEmail)  {
        return false;
    }
    public Mail[] getEmails(Account user) throws RemoteException{
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
        }
        
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
   


    private void save(Account user){
        try{
            FileOutputStream fos = new FileOutputStream("/users/"+user.getEmail()+"/info.txt");
            ObjectOutputStream obj = new ObjectOutputStream(fos);
            obj.writeObject(user);
            obj.close();
            fos.close();
        }catch(IOException e){
            System.out.println(e);
        }

    }





}
