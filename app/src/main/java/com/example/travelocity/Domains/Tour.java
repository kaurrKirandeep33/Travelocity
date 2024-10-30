package com.example.travelocity.Domains;

import java.io.Serializable;

public class Tour implements Serializable {
    private String id;
    private String title;
    private String location;
    private String description;
    private int bed;
    private boolean guide;
    private double score;
    private String pic;
    private boolean wifi;
    private Double price;
    private String category;

    public Tour() {
    }

    public Tour(String id, String title, String location, String description, int bed, boolean guide, double score, String pic, boolean wifi, Double price, String category) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.bed = bed;
        this.guide = guide;
        this.score = score;
        this.pic = pic;
        this.wifi = wifi;
        this.price = price;
        this.category = category;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isGuide() {
        return guide;
    }

    public void setGuide(boolean guide) {
        this.guide = guide;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
