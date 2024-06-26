package com.dci.a3m.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String authority;

    // Mapping to the User entity
    @OneToOne(mappedBy = "authority", cascade = CascadeType.ALL)
    private User user;

    // Constructors
    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }


    // Getters and Setters


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
