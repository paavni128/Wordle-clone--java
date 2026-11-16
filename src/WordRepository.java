

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordRepository {

    private ArrayList<String> answers;
    private HashSet<String> validWords;
    private Random random;

    // Default constructor
    public WordRepository() {
        this("data/answers.txt", "data/valid_words.txt");
    }

    // Constructor
    public WordRepository(String answerFile, String validFile) {
        answers = new ArrayList<>();
        validWords = new HashSet<>();
        random = new Random();

        try {
            List<String> answerList =
                    Files.readAllLines(Paths.get(answerFile));

            for (String word : answerList) {
                word = word.trim().toLowerCase();
                if (word.matches("[a-z]{5}")) {
                    answers.add(word);
                }
            }

            List<String> validList =
                    Files.readAllLines(Paths.get(validFile));

            for (String word : validList) {
                word = word.trim().toLowerCase();
                if (word.matches("[a-z]{5}")) {
                    validWords.add(word);
                }
            }

            if (answers.isEmpty()) {
                throw new IOException("No answer words found.");
            }

        }
         catch (IOException e) {
            throw new RuntimeException("Could not load word files.", e);
        }
    }

    // Select a random answer
    public String getRandomAnswer() {
        int index = random.nextInt(answers.size());
        return answers.get(index);
    }

    // Check whether the guessed word is valid
    public boolean isValid(String word) {
        if (word == null) {
            return false;
        }

        word = word.trim().toLowerCase();

        return word.matches("[a-z]{5}")
                && validWords.contains(word);
    }

    // Keep compatibility with the original method
    public boolean isValidWord(String word) {
        return isValid(word);
    }
}
