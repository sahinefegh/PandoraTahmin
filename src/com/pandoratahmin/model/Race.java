package com.pandoratahmin.model;

public class Race {
    private int id;
    private String raceName;
    private boolean hasSprint;
    private boolean isCompleted;

    private String[] qualiResult;
    private String[] raceResult;
    private String[] sprintQualiResult;
    private String[] sprintResult;
    private String fastestLap;
    private String[] dnfs;
    private String[] sprintDnfs;

    public Race(int id, String raceName, boolean hasSprint) {
        this.id = id;
        this.raceName = raceName;
        this.hasSprint = hasSprint;
        this.isCompleted = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRaceName() { return raceName; }
    public void setRaceName(String raceName) { this.raceName = raceName; }

    public boolean hasSprint() { return hasSprint; }
    public void setSprint(boolean hasSprint) { this.hasSprint = hasSprint; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String[] getQualiResult() { return qualiResult; }
    public void setQualiResult(String[] qualiResult) { this.qualiResult = qualiResult; }

    public String[] getRaceResult() { return raceResult; }
    public void setRaceResult(String[] raceResult) { this.raceResult = raceResult; }

    public String[] getSprintQualiResult() { return sprintQualiResult; }
    public void setSprintQualiResult(String[] sprintQualiResult) { this.sprintQualiResult = sprintQualiResult; }

    public String[] getSprintResult() { return sprintResult; }
    public void setSprintResult(String[] sprintResult) { this.sprintResult = sprintResult; }

    public String getFastestLap() { return fastestLap; }
    public void setFastestLap(String fastestLap) { this.fastestLap = fastestLap; }

    public String[] getDnfs() { return dnfs; }
    public void setDnfs(String[] dnfs) { this.dnfs = dnfs; }

    public String[] getSprintDnfs() { return sprintDnfs; }
    public void setSprintDnfs(String[] sprintDnfs) { this.sprintDnfs = sprintDnfs; }

    // JList ve JComboBox'larda nesnenin referansı yerine adının yazmasını sağlar
    @Override
    public String toString() {
        return raceName;
    }
}