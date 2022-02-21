package impiccato;


import java.io.IOException;

public class ClientTCPIMPICCATO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientStrIMPICCATO cliente = new ClientStrIMPICCATO();
        cliente.connetti();

        while (true) {
            try {
                cliente.comunica();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
    

