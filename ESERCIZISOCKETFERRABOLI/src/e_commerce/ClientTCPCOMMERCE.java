package e_commerce;


public class ClientTCPCOMMERCE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientStrCOMMERCE cliente = new ClientStrCOMMERCE();
        cliente.connetti();

        while (true) {
            cliente.comunica();
        }


    }

}
