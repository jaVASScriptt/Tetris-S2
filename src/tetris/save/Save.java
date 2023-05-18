package tetris.save;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Save implements Comparable<Save> {
    public static final int MAX_NUMBER_OF_SAVE = 5;
    public static final String JSON_EMPTY = "{saves:[]}";
    private final String name;
    private double score;
    private final String date;
    private final String heure;

    public Save(String name, double score, String date, String heure) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.heure = heure;
    }

    public String toJSON() {
        return String.format("{name: \"%s\", score: %s, date: \"%s\", heure: \"%s\"}", name, score, date, heure);
    }

    public static List<Save> filterByUserName(List<Save> list, String user) {
        return list.stream().filter(save -> save.name.equals(user)).collect(Collectors.toList());
    }

    public static void persist(List<Save> saves, Save save) {
        List<Save> userSaves = filterByUserName(new ArrayList<>(saves), save.getName());

        userSaves.forEach(saves::remove);
        userSaves.add(save);
        userSaves.sort(Collections.reverseOrder());

        while (userSaves.size() > MAX_NUMBER_OF_SAVE) {
            userSaves.remove(userSaves.size() - 1);
        }

        saves.addAll(userSaves);
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        DecimalFormat format = new DecimalFormat("#######0.0");
        return score >= 1000 ? format.format(score / 1000)+ "k" : String.valueOf((int) score);
    }

    public String getDate() {
        return date;
    }

    public String getHeure() {
        return heure;
    }

    public void incrementScore(int nbLigne, int multiplicateur) {
        switch (nbLigne){
            case 1 -> score += 40 * multiplicateur;
            case 2 -> score += 100 * multiplicateur;
            case 3 -> score += 300 * multiplicateur;
            case 4 -> score += 1200 * multiplicateur;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Save that = (Save) o;

        return this.name.equals(that.name) && this.score == that.score && this.date.equals(that.date) && this.heure.equals(that.heure);
    }

    @Override
    public int compareTo(Save that) {
        return Double.compare(this.score, that.score);
    }
}
