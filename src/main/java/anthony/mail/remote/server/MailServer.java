package anthony.mail.remote.server;
import java.rmi.*;
import anthony.mail.usable.Account;
import anthony.mail.usable.Mail;

public interface MailServer extends Remote{

    public void setup() throws RemoteException;
    public boolean login(Account user) throws RemoteException;
    public boolean register(Account user) throws RemoteException;
    public boolean validateAccount(String email, String password) throws RemoteException;

    public boolean send(Mail mail) throws RemoteException;
    public Mail[] getEmails() throws RemoteException; // should we give as argument for this function the user account or we should create a token after user log in
    public boolean saveDraft(Mail draft) throws RemoteException;
    //setStatus(User,status)   
    // he leh bedna nestaamela darure l user yaaref iza l tene off aw la2? 
    //bs ysir l user l tene on byaamel getEmail w byetla3lo kel shi emails nba3atlo

}

