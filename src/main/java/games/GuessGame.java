package games;

import common.GuessResult;
import common.HighScoreStrategy;
import common.ValidationResponse;

public class GuessGame implements Game {

    ValidationResponse validationResponse;
    private int range;
    private int numberOfGuesses;
    private int correctAnswer;
    private int closeLimit;

    @Override
    public void newGame() {
        numberOfGuesses = 0;
        range = 100;
        closeLimit = 10;
        makeCorrectAnswer();
        validationResponse = new ValidationResponse();
    }

    @Override
    public ValidationResponse validateGuess(String input) {
        resetResponse();
        if (inputIsAnInteger(input)) {
            return generateValidationResponse(Integer.parseInt(input));
        } else {
            validationResponse.setGuessResult(GuessResult.WRONG);
            validationResponse.setMessage("Guess is not a number. Try again.");
            return validationResponse;
        }
    }

    private void resetResponse() {
        validationResponse.setMessage(null);
        validationResponse.setGuessResult(null);
    }

    private ValidationResponse generateValidationResponse(int guess) {
        numberOfGuesses++;
        boolean isClose = Math.abs(guess - correctAnswer) <= closeLimit;
        if (guess < correctAnswer) {
            validationResponse.setGuessResult(GuessResult.WRONG);
            validationResponse.setMessage(isClose ? "tooSmallButClose" : "tooSmall");
        } else if (guess > correctAnswer) {
            validationResponse.setGuessResult(GuessResult.WRONG);
            validationResponse.setMessage(isClose ? "tooLargeButClose" : "tooLarge");
        } else {
            validationResponse.setGuessResult(GuessResult.CORRECT);

        }
        return validationResponse;
    }

    private boolean inputIsAnInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    @Override
    public String getSummaryDialogue() {
        return "Correct, it took " + getNumberOfGuesses() + " guesses";
    }

    @Override
    public String getPublicName() {
        return "GuessTheNumber";
    }

    @Override
    public Games getApplicationName() {
        return Games.GUESSING_GAME;
    }

    @Override
    public String getCorrectAnswer() {
        return String.valueOf(correctAnswer);
    }

    @Override
    public HighScoreStrategy getHighScoreStrategy() {
        return HighScoreStrategy.LOW_RESULT_IS_BEST;
    }

    private void makeCorrectAnswer() {
        correctAnswer = ((int) (Math.random() * range + 1));
    }

}
