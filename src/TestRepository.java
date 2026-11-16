

public class TestRepository {

    public static void main(String[] args) {

        WordRepository repo = new WordRepository(
            "data/answers.txt",
            "data/valid_words.txt"
        );

        // Test random answer
        String answer = repo.getRandomAnswer();

        System.out.println("Random Answer: " + answer);

        // Test the selected answer
        System.out.println("Is random answer valid? "
            + repo.isValidWord(answer));

        // Test uppercase
        System.out.println("Is uppercase valid? "
            + repo.isValidWord(answer.toUpperCase()));

        // Test invalid word
        System.out.println("Is hello valid? "
            + repo.isValidWord("hello"));
    }
}
