package client;


import usable.TextColor;

import static usable.TextColor.printColored;

import server.MailServer;
import usable.Account;
import usable.Mail;

import java.sql.Timestamp;

public class DistributedClient {
    public static void main(String[] args){

        if(args.length != 2)
            printColored(TextColor.RED_BOLD, "Wrong number of arguments" +
                    "\nTry again but with 2 arguments only....");
        else{

            try {

                MailClient.initiateConnection(args[0], args[1]);
                UserInterface.displayMenu(MailClient.mailServerProxy);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
