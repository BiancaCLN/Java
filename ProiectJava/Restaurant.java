import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    protected String nume;
    private List<FelDeMancare> meniu;

    public Restaurant(int id, String nume) {
        this.id = id;
        this.nume = nume;
        this.meniu = new ArrayList<>();
    }

    public Restaurant(String nume) {
        this.nume = nume;
        this.meniu = new ArrayList<>();
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

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<FelDeMancare> getMeniu() {
        return meniu;
    }

    public void adaugaFelDeMancare(String nume, double pret) {
        FelDeMancare fel = new FelDeMancare(nume, pret, this);
        meniu.add(fel);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", meniu=" + meniu +
                '}';
    }
}
