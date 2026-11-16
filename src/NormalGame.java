public class NormalGame extends Game {
    public NormalGame(String answer, Evaluator evaluator,WordRepository words) {
        super(answer, evaluator, words);
    }

    @Override
    protected void validate(String word) {
        // no extra rules in normal mode
    }
}