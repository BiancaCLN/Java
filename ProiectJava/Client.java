public class Client {
    private int id;
    private String nume;
    private String adresa;
    private String password; // Adăugăm câmpul pentru parolă

    // Constructorul pentru nume și adresă
    public Client(String nume, String adresa) {
        this.nume = nume;
        this.adresa = adresa;
    }

    // Constructorul pentru id, nume, adresa și parola
    public Client(int id, String nume, String adresa, String password) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.password = password;
    }

    // Constructorul pentru id, nume și adresa
    public Client(int id, String nume, String adresa) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
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

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                '}';
    }
}
