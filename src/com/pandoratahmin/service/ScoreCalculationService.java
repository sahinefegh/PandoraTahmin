package com.pandoratahmin.service;

import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.model.Prediction;
import com.pandoratahmin.model.Race;

import java.util.Arrays;
import java.util.List;

public class ScoreCalculationService {

    private SettingsDAO settingsDAO;

    public ScoreCalculationService() {
        this.settingsDAO = new SettingsDAO();
    }

    public void calculatePoints(Prediction pred, Race race) {
        if (pred.isDidNotAttend()) {
            resetPoints(pred);
            return;
        }

        int ptsRight = settingsDAO.getSettingAsInt("POINTS_RIGHT", 5);
        int ptsAbove = settingsDAO.getSettingAsInt("POINTS_ABOVE", 1);
        int ptsBelow = settingsDAO.getSettingAsInt("POINTS_BELOW", -1);
        int ptsDnf = settingsDAO.getSettingAsInt("POINTS_DNF", -2);
        int ptsDnq = settingsDAO.getSettingAsInt("POINTS_DNQ", -5);
        int ptsDnr = settingsDAO.getSettingAsInt("POINTS_DNR", -15);
        int ptsPodium = settingsDAO.getSettingAsInt("POINTS_PODIUM", 3);

        calculateQuali(pred, race, ptsRight, ptsAbove, ptsDnq);
        calculateRace(pred, race, ptsRight, ptsAbove, ptsBelow, ptsDnf, ptsDnr, ptsPodium);

        if (race.hasSprint()) {
            calculateSprintQuali(pred, race, ptsRight, ptsAbove, ptsDnq);
            calculateSprintRace(pred, race, ptsRight, ptsAbove, ptsBelow, ptsDnf, ptsDnr, ptsPodium);
        } else {
            pred.setSprintQPoints(0);
            pred.setSprintRPoints(0);
        }

        int total = pred.getQualiPoints() + pred.getRacePoints() + pred.getSprintQPoints() + pred.getSprintRPoints();
        pred.setPointsEarned(total);
    }

    // --- QUALI HESAPLAMASI ---
    private void calculateQuali(Prediction pred, Race race, int ptsRight, int ptsAbove, int ptsDnq) {
        if (pred.getQualiPred() == null || pred.getQualiPred().length == 0) { // Tahmin yapmadı (Q Yazmadı)
            pred.setQualiPoints(ptsDnq);
            return;
        }

        int points = 0;
        List<String> actualResults = Arrays.asList(race.getQualiResult());
        String[] preds = pred.getQualiPred();

        for (int i = 0; i < preds.length; i++) {
            if (preds[i] == null || preds[i].trim().equals("X"))
                continue;

            int actualPos = actualResults.indexOf(preds[i]);

            if (actualPos == i) {
                points += ptsRight;
            } else if (actualPos >= 0 && actualPos < i) {
                points += ptsAbove;
            }
        }
        pred.setQualiPoints(points);
    }

    // --- YARIŞ HESAPLAMASI ---
    private void calculateRace(Prediction pred, Race race, int ptsRight, int ptsAbove, int ptsBelow, int ptsDnf,
            int ptsDnr, int ptsPodium) {
        if (pred.getRacePred() == null || pred.getRacePred().length == 0) { // Tahmin yapmadı (R Yazmadı)
            pred.setRacePoints(ptsDnr);
            pred.setRaceRightGuess(0);
            return;
        }

        int points = 0;
        int rightGuess = 0;

        List<String> actualResults = Arrays.asList(race.getRaceResult());
        List<String> dnfList = race.getDnfs() != null ? Arrays.asList(race.getDnfs()) : Arrays.asList();
        String[] preds = pred.getRacePred();

        for (int i = 0; i < preds.length; i++) {
            String driver = preds[i];
            if (driver == null || driver.trim().equals("X") || driver.trim().isEmpty())
                continue;

            int actualPos = actualResults.indexOf(driver);

            if (actualPos == i) {
                points += ptsRight;
                rightGuess++;
            } else if (actualPos >= 0 && actualPos < i) {
                points += ptsAbove;
            } else if (actualPos > i) {
                points += ptsBelow;
            } else if (dnfList.contains(driver)) {
                points += ptsDnf;
            } else {
                points += ptsBelow;
            }
        }

        // Doğru Podyum Bonus Puanı
        boolean isPodiumCorrect = true;
        for (int i = 0; i < 3; i++) {
            if (!preds[i].equals(actualResults.get(i))) {
                isPodiumCorrect = false;
                break;
            }
        }
        if (isPodiumCorrect) {
            points += ptsPodium;
            pred.setPodiumCorrect(true);
        } else {
            pred.setPodiumCorrect(false);
        }

        pred.setRacePoints(points);
        pred.setRaceRightGuess(rightGuess);
    }

    // --- SPRINT SIRALAMA HESAPLAMASI ---
    private void calculateSprintQuali(Prediction pred, Race race, int ptsRight, int ptsAbove, int ptsDnq) {
        if (pred.getSprintQPred() == null || pred.getSprintQPred().length == 0) {
            pred.setSprintQPoints(ptsDnq);
            return;
        }

        int points = 0;
        List<String> actualResults = Arrays.asList(race.getSprintQualiResult());
        String[] preds = pred.getSprintQPred();

        for (int i = 0; i < preds.length; i++) {
            if (preds[i] == null || preds[i].trim().equals("X"))
                continue;

            int actualPos = actualResults.indexOf(preds[i]);
            if (actualPos == i) {
                points += ptsRight;
            } else if (actualPos >= 0 && actualPos < i) {
                points += ptsAbove;
            }
        }
        pred.setSprintQPoints(points);
    }

    // --- SPRINT YARIŞ HESAPLAMASI ---
    private void calculateSprintRace(Prediction pred, Race race, int ptsRight, int ptsAbove, int ptsBelow, int ptsDnf,
            int ptsDnr, int ptsPodium) {
        if (pred.getSprintRPred() == null || pred.getSprintRPred().length == 0) {
            pred.setSprintRPoints(ptsDnr);
            pred.setSprintRightGuess(0);
            return;
        }

        int points = 0;
        int rightGuess = 0;

        List<String> actualResults = Arrays.asList(race.getSprintResult());
        List<String> dnfList = race.getSprintDnfs() != null ? Arrays.asList(race.getSprintDnfs()) : Arrays.asList();
        String[] preds = pred.getSprintRPred();

        for (int i = 0; i < preds.length; i++) {
            String driver = preds[i];
            if (driver == null || driver.trim().equals("X") || driver.trim().isEmpty())
                continue;

            int actualPos = actualResults.indexOf(driver);

            if (actualPos == i) {
                points += ptsRight;
                rightGuess++;
            } else if (actualPos >= 0 && actualPos < i) {
                points += ptsAbove;
            } else if (actualPos > i) {
                points += ptsBelow;
            } else if (dnfList.contains(driver)) {
                points += ptsDnf;
            } else {
                points += ptsBelow;
            }
        }

        boolean isPodiumCorrect = true;
        for (int i = 0; i < 3; i++) {
            if (!preds[i].equals(actualResults.get(i))) {
                isPodiumCorrect = false;
                break;
            }
        }
        if (isPodiumCorrect) {
            points += ptsPodium;
        }

        pred.setSprintRPoints(points);
        pred.setSprintRightGuess(rightGuess);
    }

    private void resetPoints(Prediction pred) {
        pred.setQualiPoints(0);
        pred.setRacePoints(0);
        pred.setSprintQPoints(0);
        pred.setSprintRPoints(0);
        pred.setPointsEarned(0);
        pred.setRaceRightGuess(0);
        pred.setSprintRightGuess(0);
    }
}