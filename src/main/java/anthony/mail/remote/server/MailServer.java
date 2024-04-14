package anthony.mail.remote.server;


public interface MailServer extends java.rmi.Remote{

    public void setup() throws java.rmi.RemoteException;
    public void registerEmail() throws java.rmi.RemoteException;
    public boolean validateAccount(String email, String password) throws java.rmi.RemoteException;


}

