import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ui.Ui;
import common.ValidationResponse;
import games.Game;
import games.Games;
import common.GuessResult;
import integration.GameDAO;
import model.Player;
import model.PlayerAverage;

public class MindGamesController {

    private Ui ui;
    private GameDAO gameDAO;
    private Player player;

    private boolean isTestMode;
    GameController gameController;

    public MindGamesController(Ui ui, GameDAO gameDAO) {
        this.ui = ui;
        this.gameDAO = gameDAO;
        gameController = new GameController();
    }

    public void startLauncher() {
        loginPlayer();
        showLauncherIntro();
        Game game = selectGame();
        gameController.runGame(game);
    }

    private void showLauncherIntro() {
        ui.addString("SELECT GAME:" + "\n");
        Arrays.asList(Games.values())
                .forEach(game -> ui.addString(game.toString() + "\n"));
    }

    private Game selectGame() {
        String input = "";
        boolean gameIsSelected = false;
        while (!gameIsSelected) {
            input = ui.getString();
            gameIsSelected = validateGameSelection(input);
            if (!gameIsSelected) {
                ui.addString("No such game. Try again.");
            }
        }
        return GameFactory.getGame(Games.valueOf(input.toUpperCase()));
    }

    private boolean validateGameSelection(String selectedGame) {
        try {
            Games.valueOf(selectedGame.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void setDevMode(boolean testMode) {
        isTestMode = testMode;
    }

    private void loginPlayer() {
        ui.addString("Enter your user name:\n");
        Optional<Player> player = gameDAO.getPlayerByName(ui.getString());
        if (player.isPresent()) {
            this.player = player.get();
        } else {
            ui.addString("User not in database, please register with admin");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                ui.exit();
                e.printStackTrace();
            }
            ui.exit();
        }
    }

    public class GameController {

        private String currentGuess = "";
        private ValidationResponse validationResponse;

        public void runGame(Game game) {
            boolean runGame = true;
            while (runGame) {
                game.newGame();
                showGameIntro(game);
                GuessResult guessResult = null;
                while (guessResult != GuessResult.CORRECT) {
                    makeGuess();
                    validateGuess(game);
                    guessResult = getGuessResult();
                    printValidationMessageIfAny();
                }
                runGame = !closeGameRound(game);
            }
        }

        public void showGameIntro(Game game) {
            ui.clear();
            ui.addString("New game: " + game.getPublicName() + "\n");
            if (isTestMode) {
                ui.addString("For practice, number is: " + game.getCorrectAnswer() + "\n");
            }
        }

        public boolean closeGameRound(Game game) {
            boolean roundTerminated = false;
            saveGameScore(game);
            showTopTen(game);
            if (!ui.displayResultAndConfirmContinuation(game.getSummaryDialogue())) {
                ui.exit();
                roundTerminated = true;
            }
            return roundTerminated;
        }

        public void saveGameScore(Game game) {
            gameDAO.insertGameScore(game.getNumberOfGuesses(), player, game.getApplicationName());
        }

        public void makeGuess() {
            currentGuess = ui.getString();
            ui.addString(currentGuess + "\n");
        }

        private void validateGuess(Game game) {
            validationResponse = game.validateGuess(currentGuess);
        }

        private GuessResult getGuessResult() {
            return validationResponse.getGuessResult();
        }

        public void printValidationMessageIfAny() {
            if (validationResponse.getMessage() != null && !validationResponse.getMessage().isEmpty()) {
                ui.addString(validationResponse.getMessage() + "\n");
            }
        }

        public void showTopTen(Game game) {
            List<PlayerAverage> topTenPlayerAverage = getTopTen(game);
            ui.addString("Top Ten List\n    Player     Average\n");
            int pos = 1;
            for (PlayerAverage p : topTenPlayerAverage) {
                ui.addString(String.format("%3d %-10s%5.2f%n", pos++, p.getName(), p.getAverage()));
            }
        }

        public List<PlayerAverage> getTopTen(Game game) {
            return gameDAO.getTopTenPlayerAverage(game.getApplicationName(), game.getHighScoreStrategy());
        }
    }

}
