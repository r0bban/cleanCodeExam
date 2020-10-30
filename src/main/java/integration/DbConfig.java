package integration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import games.Games;

public class DbConfig {

    public static final String DB_URL = "jdbc:mysql://localhost/Moo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Stockholm";
    public static final String DB_USER = "moo_admin";
    public static final String DB_PASS = "pass";
    public static final Map<Games, String> gameResultsTableMap;

    static {
        Map<Games, String> staticMap = new HashMap<>();
        staticMap.put(Games.MOO, "results");
        staticMap.put(Games.GUESSING_GAME, "results_guessing_game");
        gameResultsTableMap = Collections.unmodifiableMap(staticMap);
    }

}
