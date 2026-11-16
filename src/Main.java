import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Wordle!");
        System.out.println("Select game mode:");
        System.out.println("1. Normal");
        System.out.println("2. Hard");
        System.out.print("Enter choice (1 or 2): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        WordRepository repo = new WordRepository(); // Assuming you have a WordRepository class
        Game game;
        if (choice == 1) {
            game = new NormalGame(repo.getRandomAnswer(), new StandardEvaluator(), repo);
        } else {
            game = new HardGame(repo.getRandomAnswer(), new StandardEvaluator(), repo);
        }

        while (!game.isOver()) {
            System.out.print("Enter word: ");
            try {
                print(game.submit(sc.next()));
            } catch (InvalidWordException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println(game.isWon()
            ? "You guessed the word!"
            : "Out of attempts. The word was: " + game.getAnswer());
    }

    private static void print(Guess g) {
        for (Tile t : g.getTiles()) {
            String color = switch (t.getState()) {
                case CORRECT -> "\u001B[42m";   // green background
                case PRESENT -> "\u001B[43m";   // yellow background
                default      -> "\u001B[100m";  // grey background
            };
            System.out.print(color + " " + Character.toUpperCase(t.getLetter()) + " \u001B[0m");
        }
        System.out.println();
    }
}