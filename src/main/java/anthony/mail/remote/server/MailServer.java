package anthony.mail.remote.server;
import java.rmi.*;
import anthony.mail.usable.Account;
import anthony.mail.usable.Mail;


public interface MailServer extends java.rmi.Remote{


    //MAY NOT BE USED
    public void setup() throws java.rmi.RemoteException;
    public int registerEmail(String name, String email, String password) throws java.rmi.RemoteException;
    public boolean validateAccount(String email, String password) throws java.rmi.RemoteException;

    public boolean send(Mail mail) throws RemoteException;
    public Mail[] getEmails(Account user) throws RemoteException; // should we give as argument for this function the user account or we should create a token after user log in
    //draft gonna be saved on the client side
    //public boolean saveDraft(Mail draft) throws RemoteException;
    //setStatus(User,status)   
    // he leh bedna nestaamela darure l user yaaref iza l tene off aw la2? 
    //bs ysir l user l tene on byaamel getEmail w byetla3lo kel shi emails nba3atlo


    public anthony.mail.usable.Mail[] RecievedEmail(anthony.mail.usable.Account account) throws java.rmi.RemoteException;
    public anthony.mail.usable.Mail[] SentEmail(anthony.mail.usable.Account account) throws java.rmi.RemoteException;



}

