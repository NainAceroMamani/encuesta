package com.nain.encuesta.models;

public class User {

    private String id;
    private String email;
    private String username;
    private String role;
    private String category;
    private String image_profile;
    private long timestamp;

    public User() {

    }

    public User(String id, String email, String username, String role, String image_profile, long timestamp, String category) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.image_profile = image_profile;
        this.timestamp = timestamp;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage_profile() { return image_profile; }

    public void setImage_profile(String image_profile) {
        this.image_profile = image_profile;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }
}
