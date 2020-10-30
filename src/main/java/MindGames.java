import ui.Ui;
import ui.UiImpl;
import integration.GameDAO;
import integration.GameDAOImpl;

public class MindGames {

    public static void main(String[] args) {

        Ui ui = new UiImpl();
        GameDAO gameDAO = new GameDAOImpl();
        MindGamesController controller = new MindGamesController(ui, gameDAO);
        controller.setDevMode(true);
        controller.startLauncher();
    }
}