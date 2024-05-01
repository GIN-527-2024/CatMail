# Email Server and Client implementation using Java RMI
## This project implements a java rmi server and client that communicate with each other and performs basic operations.

This project is buiilt using Java RMI :
* Uses Registry class in the registry library to perform successful communication between server and client
* Store users and mail in text files (users are only stored from the server side while mails are stored on both side)
* Uses java Serializable interface to store object : Mails , Accounts .
* AppendableObject streams were sed to be able to add many object in the same ser file
* Object Oriented approach
* Basic operations include: Sending ,receiving emails  and saving drafts for clients.

## How to install this project and execute 
Clone this project to your github repo .<br/>
To execute the server you need to run the **RegistryServer** class .<br/>
To execute the client you need to run the **DistributedClient** class.<br/>
Make sure to build the whole project using eclipse,maven,gradle or anything else<br/>
Client data in the client side are stored in the Client_data folder which will be created if not available in the same directory of the client <br/>
Server data are stored in the Server_Data folder which will be created if doesn't exist in the same folder of the server <br/>
