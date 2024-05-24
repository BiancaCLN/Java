import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comanda {
    private Client client;
    private Restaurant restaurant;
    private List<FelDeMancare> feluriDeMancare;
    private Date dataSiOra;

    public Comanda(Client client, Restaurant restaurant) {
        this.client = client;
        this.restaurant = restaurant;
        this.feluriDeMancare = new ArrayList<>();
        this.dataSiOra = new Date();
    }

    public void adaugaFelDeMancare(FelDeMancare felDeMancare) {
        feluriDeMancare.add(felDeMancare);
    }

    public List<FelDeMancare> getFeluriDeMancare() {
        return feluriDeMancare;
    }

    public void setFeluriDeMancare(List<FelDeMancare> feluriDeMancare) {
        this.feluriDeMancare = feluriDeMancare;
    }

    public Date getDataSiOra() {
        return dataSiOra;
    }

    public void setDataSiOra(Date dataSiOra) {
        this.dataSiOra = dataSiOra;
    }

    public Client getClient() {
        return client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "client=" + client +
                ", restaurant=" + restaurant +
                ", feluriDeMancare=" + feluriDeMancare +
                ", dataSiOra=" + dataSiOra +
                '}';
    }
}
