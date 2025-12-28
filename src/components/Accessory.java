package components;

public class Accessory extends Product{

    private String accessoryType;

    public Accessory(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String accessory_type) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.accessoryType = accessory_type;
    }

    public Accessory(){
        super();
    }

    public String getAccessory_type() {
        return accessoryType;
    }
}
