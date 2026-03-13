package com.pandoratahmin.model;

public class User {
    
    private int id;
    private String name;
    private String team;
    private int points;

    public User(int id, String name, String team) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.points = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    @Override
    public String toString() {
        return name;
    }
}