public class NormalGame extends Game {
    public NormalGame(String answer, Evaluator evaluator) {
        super(answer, evaluator);
    }

    @Override
    protected void validate(String word) {
        // no extra rules in normal mode
    }
}