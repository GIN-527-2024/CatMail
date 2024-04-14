package anthony.mail.remote.client;



public interface MailClient extends java.rmi.Remote{

    public anthony.mail.remote.server.MailServer connectToServer(String ip, String name) throws java.rmi.RemoteException;

    public void newMail() throws java.rmi.RemoteException;

    //store as a draft in a local file
    public void saveMail() throws java.rmi.RemoteException;
    public void sendMail() throws java.rmi.RemoteException;
    //returns true if exits new mails to display
    public boolean refresh() throws java.rmi.RemoteException;
    public boolean login() throws java.rmi.RemoteException;
    public void signup() throws java.rmi.RemoteException;
    public void boot () throws java.rmi.RemoteException;





}
