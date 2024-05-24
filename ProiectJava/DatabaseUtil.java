import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/java_log";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodă pentru a salva un nou client cu parolă
    public static void saveClientWithPassword(Client client, String password) {
        String sql = "INSERT INTO Client (nume, adresa, password) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getNume());
            statement.setString(2, client.getAdresa());
            statement.setString(3, password);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                client.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodă pentru a autentifica un client
    public static boolean authenticateClient(String nume, String password) {
        String sql = "SELECT * FROM Client WHERE nume = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nume);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveRestaurant(Restaurant restaurant) {
        String sql = "INSERT INTO Restaurant (nume) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, restaurant.getNume());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                restaurant.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean areThereAnyRestaurants() {
        String sql = "SELECT COUNT(*) FROM Restaurant";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT id, nume FROM Restaurant";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                restaurants.add(new Restaurant(id, nume));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public static List<FelDeMancare> getFeluriDeMancareByRestaurant(int restaurantId) {
        List<FelDeMancare> feluriDeMancare = new ArrayList<>();
        String sql = "SELECT id, nume, pret FROM FelDeMancare WHERE restaurant_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    double pret = resultSet.getDouble("pret");
                    feluriDeMancare.add(new FelDeMancare(id, nume, pret));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feluriDeMancare;
    }

    public static void saveFelDeMancare(FelDeMancare felDeMancare) {
        String sql = "INSERT INTO FelDeMancare (nume, pret, restaurant_id) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, felDeMancare.getNume());
            statement.setDouble(2, felDeMancare.getPret());
            statement.setInt(3, felDeMancare.getRestaurant().getId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                felDeMancare.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int saveComanda(Comanda comanda) {
        String sql = "INSERT INTO Comanda (client_id, restaurant_id, data_si_ora) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, getClientId(comanda.getClient()));
            statement.setInt(2, comanda.getRestaurant().getId());
            statement.setTimestamp(3, new Timestamp(comanda.getDataSiOra().getTime()));
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int comandaId = generatedKeys.getInt(1);
                saveComandaFelDeMancare(comandaId, comanda.getFeluriDeMancare());
                return comandaId; // Returnăm ID-ul comenzii
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Returnăm -1 în caz de eroare
    }

    private static void saveComandaFelDeMancare(int comandaId, List<FelDeMancare> feluriDeMancare) {
        String sql = "INSERT INTO ComandaFelDeMancare (comanda_id, fel_de_mancare_id) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (FelDeMancare felDeMancare : feluriDeMancare) {
                statement.setInt(1, comandaId);
                statement.setInt(2, felDeMancare.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getClientId(Client client) throws SQLException {
        String sql = "SELECT id FROM Client WHERE nume = ? AND adresa = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getNume());
            statement.setString(2, client.getAdresa());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return 0;
                }
            }
        }
    }

    public static Client getClientByName(String name) {
        String sql = "SELECT * FROM Client WHERE nume = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String adresa = resultSet.getString("adresa");
                    String password = resultSet.getString("password");
                    return new Client(id, nume, adresa, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRestaurantId(int restaurantId) {
        String sql = "SELECT id FROM Restaurant WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1; // Returnează -1 dacă restaurantul nu este găsit
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Returnează -1 în caz de eroare
        }
    }

    public static Restaurant getRestaurantById(int id) {
        String sql = "SELECT id, nume FROM Restaurant WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int restaurantId = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    return new Restaurant(restaurantId, nume);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Comanda getComandaById(int comandaId) {
        String sql = "SELECT c.id, c.data_si_ora, cli.nume AS client_nume, cli.adresa AS client_adresa, "
                + "r.id AS restaurant_id, r.nume AS restaurant_nume "
                + "FROM Comanda c "
                + "JOIN Client cli ON c.client_id = cli.id "
                + "JOIN Restaurant r ON c.restaurant_id = r.id "
                + "WHERE c.id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, comandaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp dataSiOra = resultSet.getTimestamp("data_si_ora");
                    String clientNume = resultSet.getString("client_nume");
                    String clientAdresa = resultSet.getString("client_adresa");
                    int restaurantId = resultSet.getInt("restaurant_id");
                    String restaurantNume = resultSet.getString("restaurant_nume");

                    Client client = new Client(clientNume, clientAdresa);
                    Restaurant restaurant = new Restaurant(restaurantId, restaurantNume);
                    Comanda comanda = new Comanda(client, restaurant);
                    comanda.setDataSiOra(new Date(dataSiOra.getTime()));

                    List<FelDeMancare> feluriDeMancare = getFeluriDeMancareByComandaId(comandaId);
                    comanda.setFeluriDeMancare(feluriDeMancare);

                    return comanda;
                } else {
                    throw new SQLException("Comanda not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<FelDeMancare> getFeluriDeMancareByComandaId(int comandaId) {
        List<FelDeMancare> feluriDeMancare = new ArrayList<>();
        String sql = "SELECT f.id, f.nume, f.pret "
                + "FROM ComandaFelDeMancare cf "
                + "JOIN FelDeMancare f ON cf.fel_de_mancare_id = f.id "
                + "WHERE cf.comanda_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, comandaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    double pret = resultSet.getDouble("pret");
                    feluriDeMancare.add(new FelDeMancare(id, nume, pret));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feluriDeMancare;
    }

    public static void saveSpecialRestaurant(SpecialRestaurant specialRestaurant) {
        String sqlSpecialRestaurant = "INSERT INTO SpecialRestaurant (restaurant_id, specialitate) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statementSpecialRestaurant = connection.prepareStatement(sqlSpecialRestaurant, Statement.RETURN_GENERATED_KEYS)) {
                statementSpecialRestaurant.setInt(1, specialRestaurant.getRestaurant().getId());
                statementSpecialRestaurant.setString(2, specialRestaurant.getSpecialitate());
                statementSpecialRestaurant.executeUpdate();

                ResultSet generatedKeys = statementSpecialRestaurant.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int specialRestaurantId = generatedKeys.getInt(1);
                    specialRestaurant.setId(specialRestaurantId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
