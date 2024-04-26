package client;

public class Main {
    public static void main(String[] args) {
       MailClient.initiateConnection(args[0], args[1]); 
       UserInterface.displayMenu(MailClient.mailServerProxy);

       MailClient.destroyConnection();
}
}
