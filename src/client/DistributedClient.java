package client;

//import java.rmi.RMISecurityManager;

import server.MailServer;
import usable.Mail;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DistributedClient {
    public static void main(String[] args){
        //noinspection removal
//        System.setSecurityManager(new RMISecurityManager());

        try {
            MailServer mailServerProxy = MailClient.initiateConnection("localhost", "RemoteInterface");
            UserInterface.displayMenu(mailServerProxy);

//            System.out.println(remoteObject.validateAccount("anthony", "hamid"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
