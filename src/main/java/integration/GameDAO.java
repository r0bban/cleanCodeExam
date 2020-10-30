package integration;

import java.util.List;
import java.util.Optional;

import games.Games;
import common.HighScoreStrategy;
import model.Player;
import model.PlayerAverage;

public interface GameDAO {
    boolean insertGameScore(int result, Player player, Games game);
    List<PlayerAverage> getTopTenPlayerAverage(Games game, HighScoreStrategy highScoreStrategy);
    Optional<Player> getPlayerByName(String name);
}
