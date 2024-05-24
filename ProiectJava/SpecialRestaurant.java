public class SpecialRestaurant {
    private int id;
    private Restaurant restaurant;
    private String specialitate;

    public SpecialRestaurant(int id, Restaurant restaurant, String specialitate) {
        this.id = id;
        this.restaurant = restaurant;
        this.specialitate = specialitate;
    }

    public SpecialRestaurant(Restaurant restaurant, String specialitate) {
        this.restaurant = restaurant;
        this.specialitate = specialitate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getSpecialitate() {
        return specialitate;
    }

    public void setSpecialitate(String specialitate) {
        this.specialitate = specialitate;
    }

    @Override
    public String toString() {
        return "SpecialRestaurant{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", specialitate='" + specialitate + '\'' +
                '}';
    }
}
