public class FelDeMancare implements Comparable<FelDeMancare> {
    private int id;
    private String nume;
    private double pret;
    private Restaurant restaurant; // Adăugăm restaurantul pentru relația de agregare

    public FelDeMancare(int id, String nume, double pret, Restaurant restaurant) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.restaurant = restaurant;
    }

    public FelDeMancare(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public FelDeMancare(String nume, double pret, Restaurant restaurant) {
        this.nume = nume;
        this.pret = pret;
        this.restaurant = restaurant;
    }

    public FelDeMancare(int id, String nume, double pret) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public int compareTo(FelDeMancare other) {
        return Double.compare(this.pret, other.pret);
    }

    @Override
    public String toString() {
        return "FelDeMancare{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", pret=" + pret +
                '}';
    }
}
