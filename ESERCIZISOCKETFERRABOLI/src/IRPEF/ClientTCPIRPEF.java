package IRPEF;


public class ClientTCPIRPEF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientStrIRPEF cliente = new ClientStrIRPEF();
        cliente.connetti();

        while (true) {
            cliente.comunica();
        }


    }

}
