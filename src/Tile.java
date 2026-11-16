public class Tile {
    private final char letter;
    private final LetterState state;

    public Tile(char letter, LetterState state) {
        this.letter = letter;
        this.state = state;
    }
    public char getLetter() { return letter; }
    public LetterState getState() { return state; }
}