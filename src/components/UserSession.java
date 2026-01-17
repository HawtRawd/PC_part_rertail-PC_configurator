package components;

import java.util.ArrayList;
import java.util.List;

public class UserSession {

    private static UserSession instance;

    private User currentUser;

    private List<models.CartItem> cart;

    private UserSession() {
        this.cart = new ArrayList<>();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void addToCart(models.CartItem item) {
        this.cart.add(item);
    }

    public void removeFromCart(models.CartItem item) {
        this.cart.remove(item);
    }

    public List<models.CartItem> getCart() {
        return this.cart;
    }

    public void clearCart() {
        this.cart.clear();
    }

    public double getCartTotal() {
        return cart.stream().mapToDouble(models.CartItem::getPrice).sum();
    }

    public void logout() {
        this.currentUser = null;
        this.cart.clear();
        instance = null;
    }
}