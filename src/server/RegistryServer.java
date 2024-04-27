package server;

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




        try {

            Registry registry = LocateRegistry.createRegistry(1177);

            MailServerImpl remoteObject = new MailServerImpl() ;
            registry.rebind("RemoteInterface", remoteObject);


            System.out.println("The system is up");
            printPrivateIP();
            printPublicIP();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }

    }

    //prints only the private ip of the wifi adapter
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
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
            System.out.println("IP Address: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }
        ;
    }
}


