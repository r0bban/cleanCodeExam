package common;

public class ValidationResponse {
    GuessResult guessResult;
    String message;

    public GuessResult getGuessResult() {
        return guessResult;
    }

    public void setGuessResult(GuessResult guessResult) {
        this.guessResult = guessResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "guessResult=" + guessResult +
                ", message='" + message + '\'' +
                '}';
    }
}
