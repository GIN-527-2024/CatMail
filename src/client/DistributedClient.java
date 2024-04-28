package client;

//import java.rmi.RMISecurityManager;

import server.MailServer;
import usable.Account;
import usable.Mail;

import java.sql.Timestamp;

public class DistributedClient {
    public static void main(String[] args){
//        System.setSecrityManager(new RMISecurityManager());

        try {


            MailServer mailServerProxy = MailClient.initiateConnection("localhost", "RemoteInterface");
//            Account me = new Account("thierry@hotmail.com", "123456Th!");
//            MailClient.refreshInbox(me);
            UserInterface.displayMenu(mailServerProxy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
