import enums.ComponentType;

public class PSU extends Product {
    private String rating;
    private int wattage;
    private String modular;

    public PSU(int id, String name, String manufacturer, double price, int stock, ComponentType category, String rating, int wattage, String modular) {
        super(id, name, manufacturer, price, stock, category);
        this.rating = rating;
        this.wattage = wattage;
        this.modular = modular;
    }

    @Override
    public String getTechSpecsSummary() {
        return "Rating: " + getRating() + " Wattage: " + getWattage() + " Modular: " + getModular();
    }

    public String getRating() {
        return rating;
    }

    public int getWattage() {
        return wattage;
    }

    public String getModular() {
        return modular;
    }
}
