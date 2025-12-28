package components;

public class Cooler extends Product {
    private String type;
    private String socket;

    public Cooler(){
        super();
    }

    public Cooler(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String type, String socket) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.type = type;
        this.socket = socket;
    }

    public String getType() {
        return type;
    }

    public String getSocket() {
        return socket;
    }
}
