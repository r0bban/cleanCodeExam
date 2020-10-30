package games;

import common.GuessResult;
import common.HighScoreStrategy;
import common.ValidationResponse;

public class Moo implements Game {

    String correctAnswer;
    ValidationResponse validationResponse;
    int numberOfGuesses;
    int cows = 0, bulls = 0;

    /**
     * This setter is only to enable testing against a known correct answer.
     *
     * @param correctAnswer the String object to be set as correct answer for testing purpose.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getPublicName() {
        return "Moo";
    }

    @Override
    public Games getApplicationName() {
        return Games.MOO;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public HighScoreStrategy getHighScoreStrategy() {
        return HighScoreStrategy.LOW_RESULT_IS_BEST;
    }

    @Override
    public void newGame() {
        makeCorrectAnswer();
        validationResponse = new ValidationResponse();
    }
    private void makeCorrectAnswer() {
        String goal = "";
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            String randomDigit = "" + random;
            while (goal.contains(randomDigit)) {
                random = (int) (Math.random() * 10);
                randomDigit = "" + random;
            }
            goal = goal + randomDigit;
        }
        correctAnswer = goal;
    }

    @Override
    public ValidationResponse validateGuess(String guess) {
        if(isValidInput(guess)){
            numberOfGuesses++;
            countBullsAndCows(guess);
            generateGuessResult();
            generateValidationResponseMessage();
        } else {
            validationResponse.setGuessResult(GuessResult.WRONG);
            validationResponse.setMessage("Invalid guess. Guess 4 numbers.");
        }
        return validationResponse;
    }

    private boolean isValidInput(String input) {
        return input.length() == 4;
    }

    private void countBullsAndCows(String guess) {
        bulls = 0;
        cows = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (correctAnswer.charAt(i) == guess.charAt(j)) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }
    }

    private void generateGuessResult() {
        validationResponse.setGuessResult(
                bulls == 4 ? GuessResult.CORRECT : GuessResult.WRONG
        );

    }

    private void generateValidationResponseMessage() {
        String resultMessage = "";
        for (int i = 0; i < bulls; i++) {
            resultMessage = resultMessage + "B";
        }
        resultMessage = resultMessage + ",";
        for (int i = 0; i < cows; i++) {
            resultMessage = resultMessage + "C";
        }
        validationResponse.setMessage(resultMessage);
    }

    @Override
    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    @Override
    public String getSummaryDialogue() {
        return "Correct, it took " + getNumberOfGuesses() + " guesses";
    }



}




