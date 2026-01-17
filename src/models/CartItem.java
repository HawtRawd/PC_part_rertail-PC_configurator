package models;

import components.Build;
import components.Product;

public class CartItem {
    private String name;
    private double price;
    private String imageUrl;

    private Product product;
    private Build build;

    public CartItem(Product product) {
        this.product = product;
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public CartItem(Build build) {
        this.build = build;
        this.name = build.getBuildName() + " (Custom PC)";
        this.price = build.calculateTotal();
        this.imageUrl = (build.getPcCase() != null) ? build.getPcCase().getImageUrl() : null;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public Product getProduct() { return product; }
    public Build getBuild() { return build; }
}