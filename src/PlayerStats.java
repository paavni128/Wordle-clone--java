
import java.io.*;

public class PlayerStats {

    private int gamesPlayed;
    private int gamesWon;
    private int currentStreak;
    private int maxStreak;

    // Constructor
    public PlayerStats() {
        gamesPlayed = 0;
        gamesWon = 0;
        currentStreak = 0;
        maxStreak = 0;
    }

    // Update player statistics
    public void updateStats(boolean won) {

        gamesPlayed++;

        if (won) {
            gamesWon++;
            currentStreak++;

            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
        } else {
            currentStreak = 0;
        }
    }

    // Save statistics to a file
    public void saveStats() {
        try {
            FileWriter writer = new FileWriter("data/stats.txt");

            writer.write(gamesPlayed + "\n");
            writer.write(gamesWon + "\n");
            writer.write(currentStreak + "\n");
            writer.write(maxStreak + "\n");

            writer.close();

            System.out.println("Statistics saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving statistics.");
        }
    }

    // Load statistics from a file
    public void loadStats() {
        File file = new File("data/stats.txt");

        if (!file.exists()) {
            return;
        }

        try {
            BufferedReader reader =
                new BufferedReader(new FileReader(file));

            gamesPlayed = Integer.parseInt(reader.readLine());
            gamesWon = Integer.parseInt(reader.readLine());
            currentStreak = Integer.parseInt(reader.readLine());
            maxStreak = Integer.parseInt(reader.readLine());

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading statistics.");
        }
    }

    // Display statistics
    public void displayStats() {

        System.out.println("----- PLAYER STATISTICS -----");
        System.out.println("Games Played: " + gamesPlayed);
        System.out.println("Games Won: " + gamesWon);
        System.out.println("Current Streak: " + currentStreak);
        System.out.println("Maximum Streak: " + maxStreak);

        double winPercentage = 0;

        if (gamesPlayed > 0) {
            winPercentage = (gamesWon * 100.0) / gamesPlayed;
        }

        System.out.println("Win Percentage: "
                           + winPercentage + "%");
    }
}
