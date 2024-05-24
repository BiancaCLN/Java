import java.util.List;

public class Controller {
    private DatabaseUtil databaseUtil;

    public Controller(DatabaseUtil databaseUtil) {
        this.databaseUtil = databaseUtil;
    }

    public boolean authenticateClient(String nume, String password) {
        return databaseUtil.authenticateClient(nume, password);
    }

    public void saveClientWithPassword(Client client, String password) {
        databaseUtil.saveClientWithPassword(client, password);
    }

    public void saveRestaurant(Restaurant restaurant) {
        databaseUtil.saveRestaurant(restaurant);
    }

    public void saveFelDeMancare(FelDeMancare felDeMancare) {
        databaseUtil.saveFelDeMancare(felDeMancare);
    }

    public void saveSpecialRestaurant(SpecialRestaurant specialRestaurant) {
        databaseUtil.saveSpecialRestaurant(specialRestaurant);
    }

    public int saveComanda(Comanda comanda) {
        return databaseUtil.saveComanda(comanda);
    }

    public Comanda getComandaById(int comandaId) {
        return databaseUtil.getComandaById(comandaId);
    }

    public List<Restaurant> getAllRestaurants() {
        return databaseUtil.getAllRestaurants();
    }

    public List<FelDeMancare> getFeluriDeMancareByRestaurant(int restaurantId) {
        return databaseUtil.getFeluriDeMancareByRestaurant(restaurantId);
    }

    public boolean areThereAnyRestaurants() {
        return databaseUtil.areThereAnyRestaurants();
    }

    public Restaurant getRestaurantById(int id) {
        return databaseUtil.getRestaurantById(id);
    }

    public int getRestaurantId(int id) {
        return databaseUtil.getRestaurantId(id);
    }
}
