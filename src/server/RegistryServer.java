package server;

import usable.TextColor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

public class RegistryServer {
    public static void main(String[] args) {

        final int PORT = 1177;

        try {

            Registry registry = LocateRegistry.createRegistry(PORT);

            MailServerImpl remoteObject = new MailServerImpl() ;
            registry.rebind("RemoteInterface", remoteObject);


            //to color all the following text in gold
            System.out.print(TextColor.GOLD.getCode());

            System.out.println("The system is up");
            printPrivateIP();
            //in some networks for example at "USEK" the values of private and public ips won't be the same
            //while in others such as the at home, they will give the same results
            printPublicIP();

            //reset the color to white
            System.out.println(TextColor.reset());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }

    }

    //prints only the private ip of the Wi-Fi adapter
    private static void printPrivateIP(){

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.getName().startsWith("w") && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address.isSiteLocalAddress()) { // Check if it's a private address
                            System.out.println("Private IP Address of WiFi interface: " + address.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Socket exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private static void printPublicIP(){
        // Print the public IP address
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Public  IP Address of WiFi interface: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }

    }
}


