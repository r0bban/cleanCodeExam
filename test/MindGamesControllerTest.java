import java.util.ArrayList;
import java.util.Arrays;

import common.GuessResult;
import common.HighScoreStrategy;
import common.ValidationResponse;
import games.Game;
import games.Games;
import model.PlayerAverage;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ui.Ui;
import integration.GameDAO;
import integration.GameDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ui.UiImpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MindGamesControllerTest {

    GameDAO mockGameDAO;
    Ui mockUi;
    MindGamesController controller;

    @BeforeEach
    void setUp() {
        mockGameDAO = Mockito.mock(GameDAOImpl.class);
        mockUi = Mockito.mock(UiImpl.class);
        controller = new MindGamesController(mockUi, mockGameDAO);
    }

    @Test
    void startLauncher() {
    }

    /**
     * Test to make 1 round with 2 wrong and 1 correct guess.
     */
    @Test
    void runGameOneRound() {
        int numberOfGuesses = 3;
        int gameRounds = 2;
        Game mockGame = Mockito.mock(Game.class);
        when(mockGame.validateGuess(any())).thenAnswer(new Answer() {
            private int count = 1;
            public Object answer(InvocationOnMock invocation) {
                ValidationResponse response = new ValidationResponse();
                response.setMessage("message");
                if (count == numberOfGuesses || count == numberOfGuesses*gameRounds) {
                    response.setGuessResult(GuessResult.CORRECT);
                    count++;
                } else {
                    response.setGuessResult(GuessResult.WRONG);
                    count++;
                }
                return response;

            }
        });
        when(mockUi.displayResultAndConfirmContinuation(any())).thenAnswer(new Answer() {
            private int count = 1;
            public Object answer(InvocationOnMock invocation) {
                boolean response;
                if (count++ == gameRounds) {
                    response = false;
                } else {
                    response = true;
                }
                return response;

            }
        });
        controller.gameController.runGame(mockGame);

        verify(mockUi, times(gameRounds)).clear();
        verify(mockUi, times(numberOfGuesses*gameRounds)).getString();
        verify(mockUi, times((numberOfGuesses*2+2)*gameRounds)).addString(any());
        verify(mockGame, times(gameRounds)).newGame();
        verify(mockGame, times(gameRounds)).getPublicName();
        verify(mockGame, times(numberOfGuesses*gameRounds)).validateGuess(any());
    }

    /**
     * Test to make 2 round with 2 wrong and 1 correct guess.
     */
    @Test
    void runGameTwoRounds() {
        int numberOfGuesses = 3;
        int gameRounds = 2;
        Game mockGame = Mockito.mock(Game.class);
        when(mockGame.validateGuess(any())).thenAnswer(new Answer() {
            private int count = 1;
            public Object answer(InvocationOnMock invocation) {
                ValidationResponse response = new ValidationResponse();
                response.setMessage("message");
                if (count == numberOfGuesses || count == numberOfGuesses*gameRounds) {
                    response.setGuessResult(GuessResult.CORRECT);
                    count++;
                } else {
                    response.setGuessResult(GuessResult.WRONG);
                    count++;
                }
                return response;

            }
        });
        when(mockUi.displayResultAndConfirmContinuation(any())).thenAnswer(new Answer() {
            private int count = 1;
            public Object answer(InvocationOnMock invocation) {
                boolean response;
                if (count++ == gameRounds) {
                    response = false;
                } else {
                    response = true;
                }
                return response;

            }
        });
        controller.gameController.runGame(mockGame);

        verify(mockUi, times(gameRounds)).clear();
        verify(mockUi, times(numberOfGuesses*gameRounds)).getString();
        verify(mockUi, times((numberOfGuesses*2+2)*gameRounds)).addString(any());
        verify(mockGame, times(gameRounds)).newGame();
        verify(mockGame, times(gameRounds)).getPublicName();
        verify(mockGame, times(numberOfGuesses*gameRounds)).validateGuess(any());
    }

    @Test
    void showTopTen(){
        Game mockGame = Mockito.mock(Game.class);
        when(mockGame.getApplicationName()).thenReturn(Games.MOO);
        when(mockGame.getHighScoreStrategy()).thenReturn(HighScoreStrategy.LOW_RESULT_IS_BEST);
        when(mockGameDAO.getTopTenPlayerAverage(mockGame.getApplicationName(), mockGame.getHighScoreStrategy()))
                .thenReturn(new ArrayList<PlayerAverage>(Arrays.asList(
                        new PlayerAverage("Player", 3.0),
                        new PlayerAverage("Player2", 4.0))));
        controller.gameController.showTopTen(mockGame);
        verify(mockUi, times(3)).addString(any());
        verify(mockGameDAO, times(1)).getTopTenPlayerAverage(any(), any());

    }

    @Test
    void setTestMode() {
    }
}