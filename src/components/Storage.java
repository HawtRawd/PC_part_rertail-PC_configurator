package components;

public class Storage extends Product {

    private String type;
    private String connectivity;
    private String capacityGB;

    public Storage(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String type, String connectivity, String capacityGB) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.type = type;
        this.connectivity = connectivity;
        this.capacityGB = capacityGB;
    }

    public String getType() {
        return type;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public String getCapacityGB() {
        return capacityGB;
    }
}
