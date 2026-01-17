package components;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String address;

    public User(int id, String username, String email, String password, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() {return password;}
    public String getAddress() {return address;}

}