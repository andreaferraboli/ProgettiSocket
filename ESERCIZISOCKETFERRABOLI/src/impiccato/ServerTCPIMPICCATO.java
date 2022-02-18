
package impiccato;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class ServerTCPIMPICCATO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int serverPort = 6789;
        ServerSocket serverSocket = new ServerSocket(serverPort);

        while(true)
        {
            System.out.println("Server in attesa...");
            Socket newClient = serverSocket.accept();
            System.out.println("Client connesso...");
            ServerThreadIMPICCATO clientConnection = new ServerThreadIMPICCATO(newClient);
            clientConnection.start();

        }
    }

}
