package com.odyssey.user;

import com.odyssey.locations.Location;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                )
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String fullname;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="location_id", referencedColumnName = "id")
    private Location location;
    @Column(nullable = false) private String avatar;

    public User() {}

    public User(String fullName, String username, String email, String password, Integer location, String avatar) {
        this.fullname = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.avatar = avatar;
    }

    public User(Integer id, String fullName, String username, String email, String password, Integer location, String avatar) {
        this.id = id;
        this.fullname = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(fullname, user.fullname) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(location, user.location) && Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, username, email, password, location, avatar);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
