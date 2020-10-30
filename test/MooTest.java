import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import common.GuessResult;
import common.HighScoreStrategy;
import common.ValidationResponse;
import games.Games;
import games.Moo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MooTest {

    Moo mooGame;

    @BeforeEach
    void setUp() {
        mooGame = new Moo();
    }

    @Test
    void getPublicName() {
        assertNotEquals("", mooGame.getPublicName());
        assertNotEquals(null, mooGame.getPublicName());
    }

    @Test
    void getApplicationName() {
        assertEquals(Games.MOO, mooGame.getApplicationName());
    }

    @Test
    void getCorrectAnswer() {
        mooGame.newGame();
        assertTrue(isCorrectFormat(mooGame.getCorrectAnswer()));
        mooGame.setCorrectAnswer("1234");
        assertEquals(mooGame.getCorrectAnswer(), "1234");
    }

    boolean isCorrectFormat(String answer) {
        try {
            Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            return false;
        }
        if (answer.length() > 4) return false;
        for (int i = 0; i < answer.length() - 1; i++) {
            boolean isUniqueChar = !answer.substring(i + 1, 4)
                    .contains(String.valueOf(answer.charAt(i)));
            if (!isUniqueChar) {
                return false;
            }
        }
        return true;
    }

    @Test
    void getHighScoreStrategy() {
        assertEquals(HighScoreStrategy.LOW_RESULT_IS_BEST, mooGame.getHighScoreStrategy());
    }

    /**
     * Testing that newGame creates a new ValidationResponse object and that
     * correctAnswer is set with a reasonable level of randomness (by default
     * less than 10% of duplicates over 100 times is accepted).
     */
    @Test
    void newGame() {
        double acceptedDuplicatePercentage = 10.0;
        int testRounds = 100;
        List<String> correctAnswerList = new ArrayList();

        for (int i = 0; i < testRounds; i++) {
            mooGame.newGame();
            correctAnswerList.add(mooGame.getCorrectAnswer());
        }

        assertTrue(acceptedDuplicatePercentage >= getDuplicatesInPercent(correctAnswerList));

        for (String correctAnswer :
                correctAnswerList) {
            assertTrue(isCorrectFormat(correctAnswer));
        }
    }

    double getDuplicatesInPercent(List listToCheck) {
        double allElements = listToCheck.size();
        double duplicates = allElements - new HashSet<>(listToCheck).size();
        return (duplicates / allElements) * 100;
    }

    @Test
    void validateGuess() {
        mooGame.newGame();
        mooGame.setCorrectAnswer("1234");

        String guess = "5678";
        ValidationResponse response = mooGame.validateGuess(guess);
        assertEquals(",", response.getMessage());
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "12345678";
        response = mooGame.validateGuess(guess);
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "1";
        response = mooGame.validateGuess(guess);
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "2134";
        response = mooGame.validateGuess(guess);
        assertEquals("BB,CC", response.getMessage());
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "1342";
        response = mooGame.validateGuess(guess);
        assertEquals("B,CCC", response.getMessage());
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "3421";
        response = mooGame.validateGuess(guess);
        assertEquals(",CCCC", response.getMessage());
        assertEquals(GuessResult.WRONG, response.getGuessResult());

        guess = "1234";
        response = mooGame.validateGuess(guess);
        assertEquals("BBBB,", response.getMessage());
        assertEquals(GuessResult.CORRECT, response.getGuessResult());
    }

    @Test
    void getNumberOfGuesses() {
        mooGame.newGame();
        mooGame.setCorrectAnswer("1234");
        for (int i = 0; i < 10; i++) {
            mooGame.validateGuess("5678");
        }
        mooGame.validateGuess("1234");
        assertEquals(11, mooGame.getNumberOfGuesses());
    }
}