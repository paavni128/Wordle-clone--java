import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public abstract class Game {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;

    private final String answer;
    private final Evaluator evaluator;
    private final List<Guess> guesses = new ArrayList<>();
    private final WordRepository words;

    public Game(String answer, Evaluator evaluator, WordRepository words) {
        this.answer = answer.toLowerCase();
        this.evaluator = evaluator;
        this.words = words;
    }

    public Guess submit(String input) throws InvalidWordException {
        String word = input.toLowerCase();
        if (word.length() != WORD_LENGTH)
            throw new InvalidWordException("Word must be " + WORD_LENGTH + " letters long.");
        if (!word.matches("[a-z]+"))
            throw new InvalidWordException("Use letters only.");
        if (!words.isValid(word))
            throw new InvalidWordException("Not a valid word.");

        validate(word);   // mode-specific rule, decided by the subclass

        Guess g = new Guess(word, evaluator.evaluate(word, answer));
        guesses.add(g);
        return g;
    }

    // Each mode defines its own extra rules
    protected abstract void validate(String word) throws InvalidWordException;

    protected List<Guess> getGuesses() { return guesses; }

    public boolean isWon() {
        return !guesses.isEmpty()
            && guesses.get(guesses.size() - 1).getWord().equals(answer);
    }
    public boolean isOver() { return isWon() || guesses.size() >= MAX_ATTEMPTS; }
    public String getAnswer() { return answer; }
    public int getAttemptCount() { return guesses.size(); }

    //keyboard state is the best state of each letter across all guesses
    public Map<Character,LetterState> getKeyboardStates() {
        Map<Character,LetterState> states = new HashMap<>();
        for (Guess g : guesses) {
            for(Tile t: g.getTiles()) {
                LetterState current = states.get(t.getLetter());
                if (current==null || t.getState().compareTo(current) < 0) {
                    states.put(t.getLetter(), t.getState());
                }
            }
        }
        return states;
    }
}
