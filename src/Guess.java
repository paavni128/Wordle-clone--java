public class Guess {
    private final String word;
    private final Tile[] tiles;

    public Guess(String word, Tile[] tiles) {
        this.word = word;
        this.tiles = tiles;
    }
    public String getWord() { return word; }
    public Tile[] getTiles() { return tiles; }
}