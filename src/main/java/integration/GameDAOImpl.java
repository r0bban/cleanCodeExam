package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import games.Games;
import common.HighScoreStrategy;
import model.Player;
import model.PlayerAverage;

public class GameDAOImpl implements GameDAO {
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement getPlayerByName;

    public GameDAOImpl() {
        try {
            connection = DriverManager.getConnection(DbConfig.DB_URL,DbConfig.DB_USER,DbConfig.DB_PASS);
            statement = connection.createStatement();
            getPlayerByName = connection.prepareStatement("SELECT * FROM players WHERE name = ?");

        } catch (SQLException exception) {
            throw new RuntimeException("Problem with database connection" + exception);
        }
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        try {
            getPlayerByName.setString(1, name);
            resultSet = getPlayerByName.executeQuery();
            if (resultSet.next()){
                Player player = new Player(resultSet.getInt("id"), resultSet.getString("name"));
                return Optional.of(player);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Couldn't find player due to database connection" + exception);
        }
        return Optional.empty();
    }

    @Override
    public List<PlayerAverage> getTopTenPlayerAverage(Games game, HighScoreStrategy highScoreStrategy) {
        String resultTable = DbConfig.gameResultsTableMap.get(game);
        String sorting = getSortingFromHighScoreStrategy(highScoreStrategy);
        List<PlayerAverage> topTenList = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT name, AVG(result) as avg_result\n" +
                    "FROM players\n" +
                    "INNER JOIN " + resultTable +"\n" +
                    "ON " + resultTable + ".player = players.id\n" +
                    "GROUP BY players.name\n" +
                    "ORDER BY avg_result " + sorting + "\n" +
                    "LIMIT 10;");
            while (resultSet.next()) {
                topTenList.add(new PlayerAverage(resultSet.getString("name"), resultSet.getDouble("avg_result")));
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Problem to get TopResults due to database connection: " + exception);
        }
        return topTenList;

    }

    @Override
    public boolean insertGameScore(int result, Player player, Games game) {
        String resultTable = DbConfig.gameResultsTableMap.get(game);
        try {
            return statement.executeUpdate("INSERT INTO "
                    + resultTable
                    + " (result, player) "
                    +"VALUES (" + result + ", " + player.getId() + ")") > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Problem to store result due to database connection: " + exception);
        }
    }

    private String getSortingFromHighScoreStrategy(HighScoreStrategy strategy){
        return strategy == HighScoreStrategy.LOW_RESULT_IS_BEST ? "ASC" : "DESC";
    }

}
