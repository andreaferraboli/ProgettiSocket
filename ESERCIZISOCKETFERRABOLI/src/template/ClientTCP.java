package template;




public class ClientTCP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientStr cliente = new ClientStr();
        cliente.connetti();

        while(true) {
            cliente.comunica();
        }

        
    }
    
}
