package com.example.huni.weekendplaner.Main;

public class ListDataEvent {
    public String address;
    public String author;
    public String descriptionOfEvent;
    public String start_date;
    public String end_date;
    public String name;

    public ListDataEvent() {
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescriptionOfEvent(String descriptionOfEvent) {
        this.descriptionOfEvent = descriptionOfEvent;
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
        return descriptionOfEvent;
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
}
