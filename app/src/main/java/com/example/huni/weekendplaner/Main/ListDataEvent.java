package com.example.huni.weekendplaner.Main;

//ListDataEvent class is used to read and write data form the database

public class ListDataEvent {
    private String address;
    private String author;
    private String description;
    private String start_date;
    private String end_date;
    private String name;
    private String img;
    private String id;

    public ListDataEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescriptionOfEvent(String descriptionOfEvent) {
        this.description = descriptionOfEvent;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setNameOfEvent(String nameOfEvent) {
        this.name = nameOfEvent;
    }

    public String getAddress() {
        return address;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescriptionOfEvent() {
        return description;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getNameOfEvent() {
        return name;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String image) {
        this.img = image;
    }

}
