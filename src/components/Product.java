package components;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String imageUrl;

    // Foreign Key IDs (Used for database Logic)
    private int manufacturerId;
    private int categoryId;

    // Display Names (Fetched via SQL JOINs)
    private String manufacturerName;
    private String categoryName;

    // Constructor
    public Product(int id, String name, double price, int stock, String imageUrl,
                   int manufacturerId, String manufacturerName,
                   int categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public int getManufacturerId() { return manufacturerId; }
    public String getManufacturerName() { return manufacturerName; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
}