import games.Game;
import games.Games;
import games.GuessGame;
import games.Moo;

public class GameFactory {

    static Game moo, guessingGame;

    public static Game getGame(Games applicationName){
        switch (applicationName) {
            case MOO:
                return moo == null ? moo = new Moo() : moo;
            case GUESSING_GAME:
                return guessingGame == null ? new GuessGame() : guessingGame;
            default:
                throw new IllegalStateException("Unexpected value: " + applicationName);
        }
    }

}
