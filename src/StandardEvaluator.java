public class StandardEvaluator implements Evaluator {
    @Override
    public Tile[] evaluate(String guess, String answer) {
        int n = answer.length();
        LetterState[] states = new LetterState[n];
        int[] unused = new int[26];               // leftover answer letters

        // Pass 1: greens, and count the answer letters not matched
        for (int i = 0; i < n; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                states[i] = LetterState.CORRECT;
            } else {
                unused[answer.charAt(i) - 'a']++;
            }
        }
        // Pass 2: yellows only while leftovers remain
        for (int i = 0; i < n; i++) {
            if (states[i] != null) continue;
            int idx = guess.charAt(i) - 'a';
            if (unused[idx] > 0) {
                states[i] = LetterState.PRESENT;
                unused[idx]--;
            } else {
                states[i] = LetterState.ABSENT;
            }
        }

        Tile[] tiles = new Tile[n];
        for (int i = 0; i < n; i++) tiles[i] = new Tile(guess.charAt(i), states[i]);
        return tiles;
    }
}