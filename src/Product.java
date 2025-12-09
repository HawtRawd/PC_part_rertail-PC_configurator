public abstract class Product {
    protected int id;
    protected String name;
    protected String manufacturer;
    protected double price;
    protected int stock;
    protected ComponentType category;
    public Product(int id, String name, String manufacturer, double price, int stock, ComponentType category) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.stock = stock;
        this.category = category;

    }

    public abstract String getTechSpecsSummary();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ComponentType getCategory() {
        return category;
    }

    public void setCategory(ComponentType category) {
        this.category = category;
    }
}
