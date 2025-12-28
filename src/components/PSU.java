package components;
public class PSU extends Product {
    private String modular;
    private int wattage;
    private String rating;
    private String formFactor;

    public PSU(){
        super();
    }

    public PSU(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String modular, int wattage, String rating, String formFactor) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.modular = modular;
        this.wattage = wattage;
        this.rating = rating;
        this.formFactor = formFactor;
    }

    public String getModular() {
        return modular;
    }

    public int getWattage() {
        return wattage;
    }

    public String getRating() {
        return rating;
    }

    public String getFormFactor() {
        return formFactor;
    }
}
