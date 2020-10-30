package ui;

import java.util.List;
import javax.swing.*;

import model.PlayerAverage;

public class UiImpl implements Ui {

    SimpleWindow simpleWindow;


    public UiImpl() {
        simpleWindow = new SimpleWindow("MindGames");
    }

    @Override
    public String getString() {
        return simpleWindow.getString();
    }

    @Override
    public void addString(String s) {
        simpleWindow.addString(s);
    }

    @Override
    public void clear() {
        simpleWindow.clear();
    }

    @Override
    public void exit() {
        simpleWindow.exit();
    }

    @Override
    public boolean displayResultAndConfirmContinuation(String resultDialogue) {
        int response = JOptionPane.showConfirmDialog(null,
                resultDialogue + "\nContinue?");
        return response == JOptionPane.YES_OPTION ? true : false;
    }

    @Override
    public void addPlayerList(List<PlayerAverage> topTenPlayerAverage) {
        int pos = 1;
        for (PlayerAverage p : topTenPlayerAverage) {
            addString(String.format("%3d %-10s%5.2f%n", pos, p.getName(), p.getAverage()));
            if (pos++ == 10) break;
        }
    }
}
