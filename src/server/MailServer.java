package server;
import java.rmi.*;
import java.sql.Timestamp;

import usable.Account;
import usable.Mail;


public interface MailServer extends Remote{


    //MAY NOT BE USED
    public void setup() throws RemoteException;
    public int registerEmail(String name, String email, String password) throws RemoteException;
    public boolean validateAccount(String email, String password) throws RemoteException;

    public boolean send(Mail mail) throws RemoteException;
    public Mail[] getEmails(Account user) throws RemoteException; // should we give as argument for this function the user account or we should create a token after user log in
    //draft gonna be saved on the client side
    //public boolean saveDraft(Mail draft) throws RemoteException;
    //setStatus(User,status)   
    // he leh bedna nestaamela darure l user yaaref iza l tene off aw la2? 
    //bs ysir l user l tene on byaamel getEmail w byetla3lo kel shi emails nba3atlo


    public Mail[] RecievedEmail(Account account, Timestamp newest) throws RemoteException;
    public Mail[] SentEmail(Account account, Timestamp newest) throws RemoteException;



}

