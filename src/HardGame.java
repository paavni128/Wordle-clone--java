public class HardGame extends Game {
    public HardGame(String answer, Evaluator evaluator, WordRepository words) {
        super(answer, evaluator, words);
    }

    @Override
    protected void validate(String word) throws InvalidWordException {
        for (Guess g : getGuesses()) {
            Tile[] tiles = g.getTiles();
            for (int i = 0; i < tiles.length; i++) {
                char c = tiles[i].getLetter();
                if (tiles[i].getState() == LetterState.CORRECT && word.charAt(i) != c)
                    throw new InvalidWordException(
                        "Position " + (i + 1) + " must be '" + c + "'.");
                if (tiles[i].getState() == LetterState.PRESENT && word.indexOf(c) < 0)
                    throw new InvalidWordException("Guess must contain '" + c + "'.");
            }
        }
    }
}