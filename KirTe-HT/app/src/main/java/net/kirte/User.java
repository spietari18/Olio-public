package net.kirte;

public class User {
    private String name;
    private String email;
    private String phone;
    private final String id;
    private final String session;
    private String message;

    /*
    public User(String name) {
        this.name = name;
        this.email = "testmail@mail.com";
        this.phone = "0400554649";
        this.id = "1";
        this.session = "sessID";
    } */

    public User(String session, String expires, String userId, String version, String links, String message) {
        this.session = session;
        this.id = userId;
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSession() {
        return session;
    }

    public String getId() { return id; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public void setEmail(String email) {this.email = email; }
}
