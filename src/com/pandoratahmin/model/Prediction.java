package com.pandoratahmin.model;

public class Prediction {
    private int id;
    private int userId;
    private int raceId;
    
    private boolean didNotAttend;
    
    // Saf Tahmin Verileri
    private String[] qualiPred;
    private String[] racePred;
    private String[] sprintQPred;
    private String[] sprintRPred;
    private String fastestLapPred;
    
    private int pointsEarned;

    // --- Arayüz (UI) için Geçici (Hesaplanan) Değerler ---
    private int qualiPoints;
    private int racePoints;
    private int sprintQPoints;
    private int sprintRPoints;
    private int raceRightGuess;
    private int sprintRightGuess;
    private boolean isPodiumCorrect;

    public Prediction(int userId, int raceId) {
        this.userId = userId;
        this.raceId = raceId;
        this.didNotAttend = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public int getRaceId() { return raceId; }

    public boolean isDidNotAttend() { return didNotAttend; }
    public void setDidNotAttend(boolean didNotAttend) { this.didNotAttend = didNotAttend; }

    public String[] getQualiPred() { return qualiPred; }
    public void setQualiPred(String[] qualiPred) { this.qualiPred = qualiPred; }

    public String[] getRacePred() { return racePred; }
    public void setRacePred(String[] racePred) { this.racePred = racePred; }

    public String[] getSprintQPred() { return sprintQPred; }
    public void setSprintQPred(String[] sprintQPred) { this.sprintQPred = sprintQPred; }

    public String[] getSprintRPred() { return sprintRPred; }
    public void setSprintRPred(String[] sprintRPred) { this.sprintRPred = sprintRPred; }

    public String getFastestLapPred() { return fastestLapPred; }
    public void setFastestLapPred(String fastestLapPred) { this.fastestLapPred = fastestLapPred; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    // Geçici UI Değerleri Getter/Setter'ları
    public int getQualiPoints() { return qualiPoints; }
    public void setQualiPoints(int qualiPoints) { this.qualiPoints = qualiPoints; }

    public int getRacePoints() { return racePoints; }
    public void setRacePoints(int racePoints) { this.racePoints = racePoints; }

    public int getSprintQPoints() { return sprintQPoints; }
    public void setSprintQPoints(int sprintQPoints) { this.sprintQPoints = sprintQPoints; }

    public int getSprintRPoints() { return sprintRPoints; }
    public void setSprintRPoints(int sprintRPoints) { this.sprintRPoints = sprintRPoints; }

    public int getRaceRightGuess() { return raceRightGuess; }
    public void setRaceRightGuess(int raceRightGuess) { this.raceRightGuess = raceRightGuess; }

    public int getSprintRightGuess() { return sprintRightGuess; }
    public void setSprintRightGuess(int sprintRightGuess) { this.sprintRightGuess = sprintRightGuess; }

    public boolean isPodiumCorrect() { return isPodiumCorrect; }
    public void setPodiumCorrect(boolean podiumCorrect) { this.isPodiumCorrect = podiumCorrect; }
}