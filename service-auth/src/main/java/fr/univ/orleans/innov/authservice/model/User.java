package fr.univ.orleans.innov.authservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class User {
    private static final String[] ROLES_ADMIN = {"ROLE_USER","ROLE_ADMIN"};
    private static final String[] ROLES_USER  = {"ROLE_USER"};

    @Id
    @Size(min = 2)
    private String username;
    @NotEmpty
    @Size(min = 2)
    private String password;
    private boolean isAdmin;

    protected User() {
        isAdmin = false;
    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String[] getRoles() {
        return isAdmin ? ROLES_ADMIN : ROLES_USER;
    }
}
