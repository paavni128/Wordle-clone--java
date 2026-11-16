
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordRepository {

    private ArrayList<String> answers;
    private HashSet<String> validWords;
    private Random random;

    // Constructor
    public WordRepository(String answerFile, String validFile)
            throws IOException {

        answers = new ArrayList<>();
        validWords = new HashSet<>();
        random = new Random();

        // Load answer words
        List<String> answerList = Files.readAllLines(
                Paths.get(answerFile));

        for (String word : answerList) {
            word = word.trim().toLowerCase();

            if (word.matches("[a-z]{5}")) {
                answers.add(word);
            }
        }

        // Load valid guess words
        List<String> validList = Files.readAllLines(
                Paths.get(validFile));

        for (String word : validList) {
            word = word.trim().toLowerCase();

            if (word.matches("[a-z]{5}")) {
                validWords.add(word);
            }
        }
    }

    // Select a random answer
    public String getRandomAnswer() {
        int index = random.nextInt(answers.size());
        return answers.get(index);
    }

    // Check whether the guessed word is valid
    public boolean isValidWord(String word) {
        if (word == null) {
            return false;
        }

        word = word.trim().toLowerCase();

        return word.matches("[a-z]{5}")
                && validWords.contains(word);
    }
}
