package games;

import common.HighScoreStrategy;
import common.ValidationResponse;

public interface Game {

    void newGame();
    ValidationResponse validateGuess(String guess);
    int getNumberOfGuesses();
    String getSummaryDialogue();
    String getPublicName();
    Games getApplicationName();
    String getCorrectAnswer();
    HighScoreStrategy getHighScoreStrategy();

}
