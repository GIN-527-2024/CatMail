package anthony.mail.remote.server;




public interface MailServer extends java.rmi.Remote{


    //MAY NOT BE USED
    public void setup() throws java.rmi.RemoteException;
    public int registerEmail(String name, String email, String password) throws java.rmi.RemoteException;
    public boolean validateAccount(String email, String password) throws java.rmi.RemoteException;



    public anthony.mail.usable.Mail[] RecievedEmail(anthony.mail.usable.Account account) throws java.rmi.RemoteException;
    public anthony.mail.usable.Mail[] SentEmail(anthony.mail.usable.Account account) throws java.rmi.RemoteException;



}

