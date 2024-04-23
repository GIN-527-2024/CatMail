package anthony.mail.remote.client;

import anthony.mail.remote.server.MailServer;
import anthony.mail.usable.Account;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class MailClientImpl extends UnicastRemoteObject implements MailClient{


    private ArrayList<Account> account = new ArrayList<Account>();
    private MailServer domain;



    public MailClientImpl(String name) throws RemoteException{
        super();

        try{
            Naming.rebind(name, this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public MailServer connectToServer(String ip, String slashName) throws RemoteException {
        MailServer server = null;

        try {
            server = (MailServer) Naming.lookup("rmi://" + ip + slashName);
        } catch (NotBoundException | MalformedURLException e ) {

        }
        return  server;

    }

    @Override
    public void newMail() throws RemoteException {

    }

    @Override
    public void saveMail() throws RemoteException {

    }

    @Override
    public void sendMail() throws RemoteException {

    }

    @Override
    public boolean refresh() throws RemoteException {
        return false;
    }

    @Override
    public boolean login() throws RemoteException {

        Scanner input = new Scanner(System.in);
        MailServer server = null;
        boolean validAccount;

        String serverName = "/mailserver";
        boolean connectionEstablished;

        String ip;
        do{
            System.out.println("Enter the server logical Address (ip)");
            ip = input.next();
            server = connectToServer(ip,serverName);
            connectionEstablished = server != null;
            if(!connectionEstablished) System.err.println("Unable to connect to the server" +
                    "\nTry another ip or check if the server is on ...");
        }while(!connectionEstablished);


        String email, password;

        System.out.println("Enter you email:");
        email = input.next();

        System.out.println("Enter you password");
        password = input.next();
        validAccount = server.validateAccount(email, password);

        //connecting to the server, we can change this later according to our code
        if(validAccount) domain = server;


        return validAccount;
    }

    @Override
    public void signup() throws RemoteException {
        Scanner input = new Scanner(System.in);

        String serverName = "/mailserver";
        MailServer server = null;
        boolean connectionEstablished;

        String ip;
        do{
            System.out.println("Enter the server logical Address (ip)");
            ip = input.next();
            server = connectToServer(ip,serverName);
            connectionEstablished = server != null;
            if(!connectionEstablished) System.err.println("Unable to connect to the server" +
                    "\nTry another ip or check if the server is on ...");
        }while(!connectionEstablished);



        //server.registerEmail();

    }

    @Override
    public void boot() throws RemoteException {

    }

    private void saveAccountInCache(Account account){

    }


}

