import java.io.*;  // Importa clasele necesare pentru operatiuni de intrare/iesire
import java.net.*;  // Importa clasele necesare pentru operatiuni de retea
import javax.swing.*;  // Importa clasele necesare pentru interfața grafica
import java.awt.*;  // Importa clasele necesare pentru layout-ul interfeței grafice
import java.awt.event.*;  // Importa clasele necesare pentru evenimente de acțiune

public class ChatClient1 {
    private static String serverAddress = "localhost";  // Adresa serverului
    private static int serverPort = 12345;  // Portul serverului

    private Socket socket;  // Socket pentru conexiunea clientului
    private PrintWriter out;  // PrintWriter pentru trimiterea mesajelor catre server
    private BufferedReader in;  // BufferedReader pentru primirea mesajelor de la server
    private DefaultListModel<String> messageListModel;  // Modelul listei pentru mesaje
    private JTextField textField;  // Campul de text pentru introducerea mesajelor
    private JButton sendButton;  // Butonul de trimitere a mesajelor
    private JFrame frame;  // Fereastra principala a clientului

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                serverPort = Integer.parseInt(args[0]);  // Converteste argumentul de linie de comanda la intreg
            } catch (NumberFormatException e) {
                System.out.println("Port invalid, utilizand portul implicit 12345");  // Mesaj in caz de port invalid
            }
        }
        SwingUtilities.invokeLater(() -> new ChatClient1().createAndShowGUI());  // Creeaza si afiseaza interfata grafica pe thread-ul de GUI
    }

    public void createAndShowGUI() {
        frame = new JFrame("Chat Client 1");  // Creeaza fereastra principala
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Seteaza actiunea de inchidere
        frame.setSize(500, 700);  // Seteaza dimensiunea ferestrei

        JPanel panel = new JPanel();  // Creeaza un panou principal
        panel.setLayout(new BorderLayout());  // Seteaza layout-ul panoului la BorderLayout

        messageListModel = new DefaultListModel<>();  // Creeaza modelul listei pentru mesaje
        JList<String> messageList = new JList<>(messageListModel);  // Creeaza lista de mesaje folosind modelul
        messageList.setFont(new Font("Arial", Font.PLAIN, 14));  // Seteaza fontul listei de mesaje
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Permite selectarea unui singur element
        JScrollPane scrollPane = new JScrollPane(messageList);  // Adauga lista intr-un JScrollPane
        panel.add(scrollPane, BorderLayout.CENTER);  // Adauga JScrollPane-ul in centrul panoului principal

        JPanel inputPanel = new JPanel();  // Creeaza un panou pentru introducerea mesajelor
        inputPanel.setLayout(new BorderLayout());  // Seteaza layout-ul panoului la BorderLayout
        textField = new JTextField();  // Creeaza campul de text pentru introducerea mesajelor
        sendButton = new JButton("Send");  // Creeaza butonul de trimitere a mesajelor

        inputPanel.add(textField, BorderLayout.CENTER);  // Adauga campul de text in centrul panoului de introducere
        inputPanel.add(sendButton, BorderLayout.EAST);  // Adauga butonul de trimitere in partea dreapta a panoului de introducere
        panel.add(inputPanel, BorderLayout.SOUTH);  // Adauga panoul de introducere in partea de sud a panoului principal

        frame.add(panel);  // Adauga panoul principal in fereastra
        frame.setVisible(true);  // Afiseaza fereastra

        textField.addActionListener(e -> sendMessage());  // Adauga un ActionListener pentru campul de text
        sendButton.addActionListener(e -> sendMessage());  // Adauga un ActionListener pentru butonul de trimitere

        connectToServer();  // Conecteaza clientul la server
    }

    private void sendMessage() {
        String message = textField.getText();  // Preia textul din campul de text
        if (message != null && !message.trim().isEmpty()) {
            out.println("Client 1: " + message);  // Trimite mesajul la server
            messageListModel.addElement("You: " + message);  // Adauga mesajul in lista de mesaje
            textField.setText("");  // Sterge textul din campul de text
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket(serverAddress, serverPort);  // Creeaza o conexiune la server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Initializeaza BufferedReader-ul pentru primirea mesajelor
            out = new PrintWriter(socket.getOutputStream(), true);  // Initializeaza PrintWriter-ul pentru trimiterea mesajelor

            new Thread(() -> {  // Creeaza un nou fir de executie pentru primirea mesajelor de la server
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        messageListModel.addElement(serverMessage);  // Adauga mesajul de la server in lista de mesaje
                    }
                } catch (IOException e) {
                    e.printStackTrace();  // Afiseaza detaliile erorii in caz de exceptie
                }
            }).start();  // Porneste firul de executie
        } catch (IOException e) {
            e.printStackTrace();  // Afiseaza detaliile erorii in caz de exceptie
        }
    }
}
