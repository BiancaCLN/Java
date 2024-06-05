import java.io.*;  // Importa clasele necesare pentru operatiuni de intrare/iesire
import java.net.*;  // Importa clasele necesare pentru operatiuni de retea
import java.util.*;  // Importa clasele necesare pentru utilizarea structurilor de date
import java.util.concurrent.locks.*;  // Importa clasele necesare pentru utilizarea obiectelor de blocare

public class ClientHandler implements Runnable {
    private Socket clientSocket;  // Socket pentru conexiunea clientului
    private PrintWriter out;  // PrintWriter pentru trimiterea mesajelor catre client
    private BufferedReader in;  // BufferedReader pentru primirea mesajelor de la client
    private Set<ClientHandler> clientHandlers;  // Set pentru pastrarea tuturor clientilor conectati
    private Lock lock = new ReentrantLock();  // Obiect Lock pentru sincronizarea accesului la clientHandlers

    // Constructorul initializeaza socket-ul clientului si setul de clientHandlers
    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.clientSocket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try {
            // Initializare BufferedReader si PrintWriter pentru comunicare
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            // Citeste mesajele de la client si le transmite catre toti clientii conectati
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                broadcastMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();  // Afiseaza detaliile erorii in caz de exceptie
        } finally {
            try {
                clientSocket.close();  // Inchide socket-ul clientului
            } catch (IOException e) {
                e.printStackTrace();  // Afiseaza detaliile erorii in caz de exceptie
            }
            lock.lock();  // Blocheaza accesul la clientHandlers
            try {
                clientHandlers.remove(this);  // Elimina acest handler din setul de clientHandlers
            } finally {
                lock.unlock();  // Deblocheaza accesul la clientHandlers
            }
        }
    }

    // Metoda pentru transmiterea mesajului catre toti clientii conectati
    private void broadcastMessage(String message) {
        lock.lock();  // Blocheaza accesul la clientHandlers
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler != this) {
                    clientHandler.out.println(message);  // Trimite mesajul catre toti clientii conectati
                }
            }
        } finally {
            lock.unlock();  // Deblocheaza accesul la clientHandlers
        }
    }
}
