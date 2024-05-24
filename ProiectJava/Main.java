import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        Client authenticatedClient = null;
        Controller service = new Controller(new DatabaseUtil());

        // Autentificare la început
        while (!authenticated) {
            System.out.println("1. Autentificare");
            System.out.println("2. Creare cont nou");
            System.out.println("3. Ieșire");
            int optiune = scanner.nextInt();
            scanner.nextLine(); // consumă linia rămasă

            switch (optiune) {
                case 1:
                    System.out.println("Introduceți numele:");
                    String nume = scanner.nextLine();
                    System.out.println("Introduceți parola:");
                    String password = scanner.nextLine();
                    authenticated = service.authenticateClient(nume, password);
                    if (authenticated) {
                        authenticatedClient = DatabaseUtil.getClientByName(nume);
                        System.out.println("Autentificare reușită!");
                    } else {
                        System.out.println("Nume sau parolă incorectă.");
                    }
                    CSVLogger.log("Autentificare");
                    break;

                case 2:
                    System.out.println("Introduceți numele pentru noul cont:");
                    String newNume = scanner.nextLine();
                    System.out.println("Introduceți adresa pentru noul cont:");
                    String newAdresa = scanner.nextLine();
                    System.out.println("Introduceți parola pentru noul cont:");
                    String newPassword = scanner.nextLine();
                    Client newClient = new Client(newNume, newAdresa);
                    service.saveClientWithPassword(newClient, newPassword);
                    System.out.println("Cont creat cu succes. Acum vă puteți autentifica.");
                    CSVLogger.log("Creare cont nou");
                    break;

                case 3:
                    System.out.println("La revedere!");
                    CSVLogger.log("Ieșire");
                    return;

                default:
                    System.out.println("Opțiune invalidă.");
                    CSVLogger.log("Opțiune invalidă.");
            }
        }

        // Bucla principală după autentificare
        while (true) {
            System.out.println("1. Adaugă restaurant");
            System.out.println("2. Adaugă fel de mâncare");
            System.out.println("3. Adaugă restaurant special");
            System.out.println("4. Plasează comandă");
            System.out.println("5. Vizualizează toate restaurantele");
            System.out.println("6. Vizualizează felurile de mâncare ale unui restaurant");
            System.out.println("7. Vizualizează comandă");
            System.out.println("8. Ieșire");
            int optiune = scanner.nextInt();
            scanner.nextLine(); // consumă linia rămasă

            switch (optiune) {
                case 1:
                    System.out.println("Introduceți numele restaurantului:");
                    String numeRestaurant = scanner.nextLine();
                    Restaurant restaurant = new Restaurant(numeRestaurant);
                    service.saveRestaurant(restaurant);
                    System.out.println("Restaurant adăugat cu succes.");
                    CSVLogger.log("Adauga Restaurant");
                    break;

                case 2:
                    if (!service.areThereAnyRestaurants()) {
                        System.out.println("Mai întâi adăugați un restaurant.");
                        break;
                    }

                    List<Restaurant> restaurants = service.getAllRestaurants();
                    System.out.println("Selectați un restaurant prin ID:");
                    for (Restaurant r : restaurants) {
                        System.out.println("ID: " + r.getId() + " - Nume: " + r.getNume());
                    }

                    int restaurantId = scanner.nextInt();
                    scanner.nextLine(); // consumă linia rămasă

                    System.out.println("Introduceți numele felului de mâncare:");
                    String numeFel = scanner.nextLine();
                    System.out.println("Introduceți prețul felului de mâncare:");
                    double pretFel = scanner.nextDouble();
                    scanner.nextLine(); // consumă linia rămasă

                    FelDeMancare felDeMancare = new FelDeMancare(numeFel, pretFel);
                    service.saveFelDeMancare(felDeMancare);
                    System.out.println("Fel de mâncare adăugat cu succes.");
                    CSVLogger.log("Adauga Fel de Mâncare");
                    break;

                case 3:
                    System.out.println("Introduceți ID-ul restaurantului:");
                    int restaurantIdForSpecial = scanner.nextInt();
                    scanner.nextLine(); // consumă linia rămasă

                    System.out.println("Introduceți specialitatea restaurantului:");
                    String specialitate = scanner.nextLine();
                    Restaurant existingRestaurant = service.getRestaurantById(restaurantIdForSpecial);
                    if (existingRestaurant != null) {
                        SpecialRestaurant specialRestaurant = new SpecialRestaurant(existingRestaurant, specialitate);
                        service.saveSpecialRestaurant(specialRestaurant);
                        System.out.println("Restaurant special adăugat cu succes.");
                        CSVLogger.log("Adauga Restaurant Special");
                    } else {
                        System.out.println("Restaurantul nu a fost găsit.");
                    }
                    break;

                case 4:
                    if (!service.areThereAnyRestaurants()) {
                        System.out.println("Mai întâi adăugați un restaurant.");
                        break;
                    }

                    List<Restaurant> restaurantsForOrder = service.getAllRestaurants();
                    System.out.println("Selectați un restaurant prin ID:");
                    for (Restaurant r : restaurantsForOrder) {
                        System.out.println("ID: " + r.getId() + " - Nume: " + r.getNume());
                    }

                    int restaurantIdForOrder = scanner.nextInt();
                    scanner.nextLine(); // consumă linia rămasă

                    int restaurantCheck = service.getRestaurantId(restaurantIdForOrder);
                    if (restaurantCheck == -1) {
                        System.out.println("Restaurantul selectat nu există.");
                        break;
                    }

                    Set<FelDeMancare> feluriDeMancare = new TreeSet<>(service.getFeluriDeMancareByRestaurant(restaurantIdForOrder));
                    if (feluriDeMancare.isEmpty()) {
                        System.out.println("Nu există feluri de mâncare pentru acest restaurant.");
                        break;
                    }

                    System.out.println("Selectați felurile de mâncare (introduceți ID-urile separate prin virgulă):");
                    for (FelDeMancare fel : feluriDeMancare) {
                        System.out.println("ID: " + fel.getId() + " - Nume: " + fel.getNume() + " - Pret: " + fel.getPret());
                    }

                    String[] feluriSelectate = scanner.nextLine().split(",");
                    List<FelDeMancare> feluriComandate = new ArrayList<>();
                    for (String id : feluriSelectate) {
                        int felId = Integer.parseInt(id.trim());
                        for (FelDeMancare fel : feluriDeMancare) {
                            if (fel.getId() == felId) {
                                feluriComandate.add(fel);
                                break;
                            }
                        }
                    }

                    Restaurant selectedRestaurant = new Restaurant(restaurantIdForOrder, "");
                    Comanda comanda = new Comanda(authenticatedClient, selectedRestaurant); // Folosim clientul autentificat
                    comanda.setFeluriDeMancare(feluriComandate);
                    int comandaIdNou = service.saveComanda(comanda); // Salvăm comanda și obținem ID-ul acesteia
                    if (comandaIdNou != -1) {
                        System.out.println("Comanda plasată cu succes. ID-ul comenzii este: " + comandaIdNou);
                    } else {
                        System.out.println("A apărut o eroare la plasarea comenzii.");
                    }
                    CSVLogger.log("Plaseaza Comanda");
                    break;

                case 5:
                    List<Restaurant> allRestaurants = service.getAllRestaurants();
                    System.out.println("Toate restaurantele:");
                    for (Restaurant res : allRestaurants) {
                        System.out.println("ID: " + res.getId() + " - Nume: " + res.getNume());
                    }
                    CSVLogger.log("Vizualizeaza Toate Restaurantele");
                    break;

                case 6:
                    System.out.println("Introduceți ID-ul restaurantului:");
                    int idRestaurant = scanner.nextInt();
                    scanner.nextLine(); // consumă linia rămasă
                    Set<FelDeMancare> feluri = new TreeSet<>(service.getFeluriDeMancareByRestaurant(idRestaurant));
                    if (feluri.isEmpty()) {
                        System.out.println("Nu există feluri de mâncare pentru acest restaurant.");
                    } else {
                        System.out.println("Feluri de mâncare pentru restaurantul cu ID-ul " + idRestaurant + ":");
                        for (FelDeMancare fel : feluri) {
                            System.out.println("ID: " + fel.getId() + " - Nume: " + fel.getNume() + " - Pret: " + fel.getPret());
                        }
                    }
                    CSVLogger.log("Vizualizeaza Feluri de Mâncare");
                    break;

                case 7:
                    System.out.println("Introduceți ID-ul comenzii:");
                    int comandaId = scanner.nextInt();
                    scanner.nextLine(); // consumă linia rămasă
                    Comanda comandaVizualizata = service.getComandaById(comandaId);
                    if (comandaVizualizata != null) {
                        System.out.println("Comanda găsită:");
                        System.out.println(comandaVizualizata);
                    } else {
                        System.out.println("Comanda nu a fost găsită.");
                    }
                    CSVLogger.log("Vizualizeaza Comanda");
                    break;

                case 8:
                    System.out.println("La revedere!");
                    CSVLogger.log("Iesire");
                    return;

                default:
                    System.out.println("Opțiune invalidă.");
                    CSVLogger.log("Opțiune Invalidă");
            }
        }
    }
}
