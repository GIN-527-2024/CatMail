package client;

//import java.rmi.RMISecurityManager;

import server.MailServer;
import usable.Mail;

public class DistributedClient {
    public static void main(String[] args){
//        System.setSecrityManager(new RMISecurityManager());

        try {
          
            MailServer mailServerProxy = MailClient.initiateConnection("localhost", "RemoteInterface");
            UserInterface.displayMenu(mailServerProxy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
