
import javax.swing.SwingUtilities;

public class GameLauncher {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Load the word repository
            WordRepository words = new WordRepository();

            // Create the evaluator
            Evaluator evaluator = new StandardEvaluator();

            // Select a random answer
            String answer = words.getRandomAnswer();

            // Start Normal Mode for the first test
            Game game = new HardGame(answer, evaluator);
            // Create player statistics
            PlayerStats stats = new PlayerStats();

            // Open the game window
            GameWindow window = new GameWindow(game, stats, words);

            window.setVisible(true);
        });
    }
}
