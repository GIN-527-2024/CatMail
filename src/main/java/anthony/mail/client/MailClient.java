package anthony.mail.client;
import anthony.mail.remote.server.MailServer;
import anthony.mail.usable.*;

import java.io.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
public class MailClient {
    static MailServer mailServerProxy;
    public static MailServer initiateConnection(String address, String name) {
        try {
            MailServer serverProxy = (MailServer) Naming.lookup("rmi://" +address +"/" +name);
            mailServerProxy = serverProxy;
            return serverProxy;
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
            return null;
        }
    }

    public static boolean loginRemote(Account user) {
        try {
            if(!mailServerProxy.login(user)) {
                System.out.println("invalid username or password");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
        }
        return false;
    }

    public static boolean registerRemote(Account user) {
        try {
            if(!mailServerProxy.validateAccount(user.getEmail(), user.getPassword())) {
                System.out.println("invalid format.");
                return false;
            }
            if(!mailServerProxy.register(user)) {
                System.out.println("email already exists.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
            return false;
        }
        return true;
    }

    public static void sendEmailRemote(Mail mail) {
        try {
            if(mailServerProxy.send(mail)) {
                System.out.println("Email sent successfully.");
                return;
            }
            System.out.println("Could not send email (invalid format)");
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
        }
    }
    // a way to end the connection when the client has finished.
    public static void destroyConnection(){
        mailServerProxy=null;
    }

    // a way to get the client emails
    public static Mail[] getEmails(Account user){
        try {
            return mailServerProxy.getEmails(user);
        } catch (Exception e) {
            System.err.println("Remote Exception: " +e);
        }
        return null;
    }
    public static void saveDraft(Mail mail){
        //save the draft on the client side
        try{
            FileOutputStream f=new FileOutputStream("drafts.txt"); //the object willbe written on drafts.txt
            ObjectOutputStream o=new ObjectOutputStream(f);
            o.writeObject(mail);
            o.close();
            f.close();
        }
        catch(IOException e){
            System.out.println(e);

        }

    }


}
