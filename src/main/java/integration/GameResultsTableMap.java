package integration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import games.Games;

public class GameResultsTableMap {

        private static final Map<Games, String> gameResultsTableMap;

        static {
            Map<Games,String> staticMap = new HashMap<>();
            staticMap.put(Games.MOO, "results");
            staticMap.put(Games.GUESSING_GAME, "results_mastermind");
            gameResultsTableMap = Collections.unmodifiableMap(staticMap);
        }

        public static Map<Games, String> getGameTableMap() {
            return gameResultsTableMap;
        }


}
