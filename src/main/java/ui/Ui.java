package ui;

import java.util.List;

import model.PlayerAverage;

public interface Ui {

    String getString();

    void addString(String s);

    void clear();

    void exit();

    boolean displayResultAndConfirmContinuation(String summaryDialogue);

    void addPlayerList(List<PlayerAverage> topTenPlayerAverage);
}
