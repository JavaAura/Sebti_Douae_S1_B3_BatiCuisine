package org.baticuisine.entities;

import java.util.List;

public class Client {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private Boolean isProfessional;
    private List<Project> projects;

    public Client() {
    }

    public Client(int id, String name, String address, String phoneNumber, Boolean isProfessional) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isProfessional = isProfessional;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getProfessional() {
        return isProfessional;
    }

    public void setProfessional(Boolean professional) {
        isProfessional = professional;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
