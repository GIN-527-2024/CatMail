package client;

//import java.rmi.RMISecurityManager;

import server.MailServer;

public class DistributedClient {
    public static void main(String[] args){
        //noinspection removal
//        System.setSecurityManager(new RMISecurityManager());


            MailServer remoteObject = MailClient.initiateConnection("localhost", "RemoteInterface");
            UserInterface.displayMenu(remoteObject);
//            System.out.println(remoteObject.validateAccount("anthony", "hamid"));
            


    }

}
