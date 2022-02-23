package IRPEF;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPIRPEF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int serverPort = 6788;
        ServerSocket serverSocket = new ServerSocket(serverPort);

        while (true) {
            System.out.println("Server in attesa...");
            Socket newClient = serverSocket.accept();
            System.out.println("Client connesso...");
            ServerThreadIRPEF clientConnection = new ServerThreadIRPEF(newClient);
            clientConnection.start();

        }
    }

}
