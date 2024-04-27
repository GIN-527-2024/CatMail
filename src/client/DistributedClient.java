package client;

//import java.rmi.RMISecurityManager;

import server.MailServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DistributedClient {
    public static void main(String[] args){
        //noinspection removal
//        System.setSecurityManager(new RMISecurityManager());

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1177);

            MailServer remoteObject = (MailServer) registry.lookup("RemoteInterface");

//            System.out.println(remoteObject.validateAccount("anthony", "hamid"));


        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }


    }

}
