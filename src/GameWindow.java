

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {

    private JLabel[][] tiles = new JLabel[6][5];

    private int currentRow = 0;
    private int currentCol = 0;

    // Connection to Person A's game logic
    private Game game;
    private PlayerStats stats;
    private WordRepository words;

    private boolean gameFinished = false;
    public GameWindow(Game game, PlayerStats stats, WordRepository words) {

    this.game = game;
    this.stats = stats;
    this.words = words;

        // Load previous player statistics
        this.stats.loadStats();

        // Window settings
        setTitle("Wordle Game");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("WORDLE", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));
        add(heading, BorderLayout.NORTH);

        // Grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(6, 5, 8, 8));
        gridPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 50, 20, 50)
        );

        // Create 6 rows and 5 columns
        for (int i = 0; i < 6; i++) {

            for (int j = 0; j < 5; j++) {

                tiles[i][j] = new JLabel("", JLabel.CENTER);

                tiles[i][j].setFont(
                    new Font("Arial", Font.BOLD, 24)
                );

                tiles[i][j].setBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 2)
                );

                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(Color.WHITE);

                gridPanel.add(tiles[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // On-screen keyboard panel
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridLayout(3, 1, 5, 5));
        keyboardPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 15, 10)
        );

        keyboardPanel.add(createKeyboardRow("QWERTYUIOP"));
        keyboardPanel.add(createKeyboardRow("ASDFGHJKL"));

        // Third keyboard row
        JPanel thirdRow = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 2, 2)
        );

        thirdRow.add(createActionButton("ENTER", "ENTER"));
        thirdRow.add(createKeyboardRow("ZXCVBNM"));
        thirdRow.add(createActionButton("DEL", "BACKSPACE"));

        keyboardPanel.add(thirdRow);

        add(keyboardPanel, BorderLayout.SOUTH);

        // Physical keyboard input
        setupKeyboardInput();
    }

    // Create a row of letter buttons
    private JPanel createKeyboardRow(String letters) {

        JPanel row = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 2, 2)
        );

        for (char ch : letters.toCharArray()) {

            JButton button = new JButton(String.valueOf(ch));

            button.setPreferredSize(new Dimension(38, 48));
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setMargin(new Insets(2, 2, 2, 2));
            button.setFocusable(false);

            button.addActionListener(e -> addLetter(ch));

            row.add(button);
        }

        return row;
    }

    // Create ENTER and DEL buttons
    private JButton createActionButton(String text, String action) {

        JButton button = new JButton(text);

        if (action.equals("ENTER")) {
            button.setPreferredSize(new Dimension(85, 48));
        } else {
            button.setPreferredSize(new Dimension(55, 48));
        }

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setFocusable(false);

        button.addActionListener(e -> {

            if (action.equals("ENTER")) {
                submitRow();
            } else {
                deleteLetter();
            }
        });

        return button;
    }

    // Physical keyboard input using Swing key bindings
    private void setupKeyboardInput() {

        JRootPane root = getRootPane();

        InputMap inputMap = root.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap actionMap = root.getActionMap();

        // Letter keys A-Z
        // Letter keys A-Z
for (int i = 0; i < 26; i++) {

    int keyCode = KeyEvent.VK_A + i;
    char letter = (char) ('A' + i);

    String keyName = "letter_" + letter;

    inputMap.put(
        KeyStroke.getKeyStroke(keyCode, 0),
        keyName
    );

    actionMap.put(keyName, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addLetter(letter);
        }
    });
}

        // Backspace key
        inputMap.put(
            KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
            "delete"
        );

        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLetter();
            }
        });

        // Enter key
        inputMap.put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "submit"
        );

        actionMap.put("submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRow();
            }
        });
    }

    // Add a letter to the current tile
    private void addLetter(char ch) {

        if (!gameFinished && currentRow < 6 && currentCol < 5) {

            tiles[currentRow][currentCol].setText(
                String.valueOf(ch).toUpperCase()
            );

            currentCol++;
        }
    }

    // Delete the previous letter
    private void deleteLetter() {

        if (!gameFinished && currentRow < 6 && currentCol > 0) {

            currentCol--;

            tiles[currentRow][currentCol].setText("");
        }
    }

    // Submit the current guess to Person A's Game class
    private void submitRow() {

        if (gameFinished) {
            return;
        }

        if (currentCol < 5) {

            JOptionPane.showMessageDialog(
                this,
                "Enter all 5 letters first!"
            );

            return;
        }

        // Collect the five letters
        StringBuilder guessBuilder = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            guessBuilder.append(
                tiles[currentRow][i].getText().toLowerCase()
            );
        }
        String word = guessBuilder.toString();

// Check whether the word exists in valid_words.txt
if (!words.isValid(word)) {

    JOptionPane.showMessageDialog(
        this,
        "Not a valid word.",
        "Invalid Guess",
        JOptionPane.WARNING_MESSAGE
    );

    return;
}

try {

            // Submit guess to Person A's game logic
            Guess guess = game.submit(word);

            // Display evaluated tiles
            Tile[] result = guess.getTiles();

            for (int i = 0; i < result.length; i++) {

                tiles[currentRow][i].setBackground(
                    getTileColor(result[i].getState())
                );

                tiles[currentRow][i].setForeground(Color.WHITE);
            }

            // Move to the next row
            currentRow++;
            currentCol = 0;

            // Check whether the game has ended
            if (game.isOver()) {
                finishGame();
            }

        } catch (InvalidWordException e) {

            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Invalid Guess",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // Convert LetterState into a tile colour
    private Color getTileColor(LetterState state) {

        switch (state) {

            case CORRECT:
                return new Color(106, 170, 100);

            case PRESENT:
                return new Color(201, 180, 88);

            case ABSENT:
                return new Color(120, 124, 126);

            default:
                return Color.WHITE;
        }
    }

    // Update statistics when the game ends
    private void finishGame() {

        gameFinished = true;

        boolean won = game.isWon();

        stats.updateStats(won);
        stats.saveStats();

        if (won) {

            JOptionPane.showMessageDialog(
                this,
                "Congratulations! You guessed the word!"
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Game Over! The correct word was: "
                + game.getAnswer().toUpperCase()
            );
        }
    }
}
