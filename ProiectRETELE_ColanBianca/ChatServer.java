import java.io.*;  // Importa clasele necesare pentru operatiuni de intrare/iesire
import java.net.*;  // Importa clasele necesare pentru operatiuni de retea
import java.util.*;  // Importa clasele necesare pentru structuri de date

public class ChatServer {
    private static int port = 12345;  // Portul implicit pe care serverul asculta conexiunile

    private static Set<ClientHandler> clientHandlers = new HashSet<>();  // Un set pentru a pastra toti clientii conectati

    public static void main(String[] args) {
        // Verifica daca un port a fost specificat ca argument de linie de comanda
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);  // Converteste argumentul de linie de comanda la intreg
            } catch (NumberFormatException e) {
                System.out.println("Port invalid, utilizand portul implicit 12345");  // Mesaj in caz de port invalid
            }
        }

        System.out.println("Server started on port " + port);  // Mesaj pentru a indica portul pe care serverul a pornit
        try (ServerSocket serverSocket = new ServerSocket(port)) {  // Creeaza un ServerSocket care asculta pe portul specificat
            while (true) {
                Socket clientSocket = serverSocket.accept();  // Accepta conexiuni de la clienti
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers);  // Creeaza un nou handler pentru client
                clientHandlers.add(clientHandler);  // Adauga handlerul clientului in setul de clienti
                new Thread(clientHandler).start();  // Porneste un nou fir de executie pentru a gestiona clientul
            }
        } catch (IOException e) {
            e.printStackTrace();  // Afiseaza detaliile erorii in caz de exceptie
        }
    }
}
