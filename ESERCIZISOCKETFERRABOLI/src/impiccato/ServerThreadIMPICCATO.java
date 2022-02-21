package impiccato;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class ServerThreadIMPICCATO extends Thread {
    ServerSocket server = null;
    int tentativi = 9;
    Socket client = null;
    String stringaRicevuta = null;
    String secretWord = generateSecretWord();
    String guessedWord = genarateGuessedWord(secretWord);
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThreadIMPICCATO(Socket client) throws IOException {
        this.client = client;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream(this.client.getOutputStream());
    }

    public void comunica() throws Exception {
//        System.out.println(inDalClient.readLine());
//        if (inDalClient.readLine().equals("gioca"))
//            outVersoClient.writeBytes("parola da indovinare:"+guessedWord+"\n");
        while (tentativi != 0) {
            stringaRicevuta = inDalClient.readLine().toLowerCase(Locale.ROOT);
            if (stringaRicevuta.equals("fine")) {
                outVersoClient.writeBytes("grazie per aver giocato...la parola era:" + secretWord + "\n");
                close();
            } else if (stringaRicevuta.equals("ricomincia")) {
                secretWord = generateSecretWord();
                guessedWord = genarateGuessedWord(secretWord);
                outVersoClient.writeBytes("parola da indovinare:" + guessedWord + "\n");
                tentativi = 9;
            } else {
                if (checkAttempt(stringaRicevuta.charAt(0))) {
                    outVersoClient.writeBytes("complimenti hai indovinato la parola " + secretWord + "\n");
                    close();
                } else {
                    outVersoClient.writeBytes("parola da indovinare:" + guessedWord + " numero tentativi rimasti:" + tentativi + "\n");
                    tentativi--;
                }
            }

        }
        outVersoClient.writeBytes("numero tentativi finito...la parola era:" + secretWord + "\n");


    }

    private void close() throws IOException {
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    private boolean checkAttempt(char charAt) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            if (charAt == secretWord.charAt(i))
                temp.append(charAt);
            else {
                temp.append(guessedWord.charAt(i));
            }
        }
        guessedWord = temp.toString();
        return guessedWord.equals(secretWord);
    }

    private String genarateGuessedWord(String secretWord) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            if (i == 0 || i == secretWord.length() - 1)
                word.append(secretWord.charAt(i));
            else
                word.append("_");
        }
        return word.toString();
    }

    public String generateSecretWord() throws IOException {
        String randomWord = "";

        List<String> words;
        BufferedReader reader = new BufferedReader(new FileReader("ESERCIZISOCKETFERRABOLI/src/impiccato/dizionario.txt"));
        String line = reader.readLine();
        words = new ArrayList<String>();
        while (line != null) {
            String[] wordsLine = line.split(" ");
            words.addAll(Arrays.asList(wordsLine));
            line = reader.readLine();
        }

        Random rand = new Random(System.currentTimeMillis());
        randomWord = words.get(rand.nextInt(words.size()));

        return randomWord;
    }

    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
