

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {

    private JLabel[][] tiles = new JLabel[6][5];

    private int currentRow = 0;
    private int currentCol = 0;

    // Game connections
    private Game game;
    private PlayerStats stats;
    private WordRepository words;

    private boolean gameFinished = false;

    // New UI components
    private JComboBox<String> modeSelector;
    private JButton reloadButton;

    public GameWindow(Game game, PlayerStats stats, WordRepository words) {

        this.game = game;
        this.stats = stats;
        this.words = words;

        this.stats.loadStats();

        setTitle("Wordle Game");
        setSize(500, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("WORDLE", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));

        // Top panel containing heading and controls
        JPanel topPanel = new JPanel(new BorderLayout(5, 10));
        topPanel.add(heading, BorderLayout.NORTH);

        // Mode dropdown
        modeSelector = new JComboBox<>(
            new String[]{"Normal Mode", "Hard Mode"}
        );

        // Select the current game's mode
        if (game instanceof HardGame) {
            modeSelector.setSelectedItem("Hard Mode");
        } else {
            modeSelector.setSelectedItem("Normal Mode");
        }

        modeSelector.setFont(new Font("Arial", Font.PLAIN, 14));

        // Reload button
        reloadButton = new JButton("↻ Reload");
        reloadButton.setFont(new Font("Arial", Font.BOLD, 14));
        reloadButton.setFocusable(false);

        // Controls panel
        JPanel controlsPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 5)
        );

        controlsPanel.add(new JLabel("Game Mode:"));
        controlsPanel.add(modeSelector);
        controlsPanel.add(reloadButton);

        topPanel.add(controlsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

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

        // On-screen keyboard
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

        // Reload button action
        reloadButton.addActionListener(e -> restartGame());

        // Dropdown action
        modeSelector.addActionListener(e -> restartGame());

        // Physical keyboard input
        setupKeyboardInput();
    }

    // Create a new game using the selected mode
    private Game createNewGame() {

        String answer = words.getRandomAnswer();
        Evaluator evaluator = new StandardEvaluator();

        String selectedMode =
            (String) modeSelector.getSelectedItem();

        if ("Hard Mode".equals(selectedMode)) {
            return new HardGame(answer, evaluator);
        }

        return new NormalGame(answer, evaluator);
    }

    // Restart the game
    private void restartGame() {

        // Create a completely new game
        game = createNewGame();

        // Reset game status
        currentRow = 0;
        currentCol = 0;
        gameFinished = false;

        // Clear all tiles
        for (int i = 0; i < 6; i++) {

            for (int j = 0; j < 5; j++) {

                tiles[i][j].setText("");
                tiles[i][j].setBackground(Color.WHITE);
                tiles[i][j].setForeground(Color.BLACK);
            }
        }

        // Return keyboard focus to the game
        getRootPane().requestFocusInWindow();
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

    // Physical keyboard input
    private void setupKeyboardInput() {

        JRootPane root = getRootPane();

        InputMap inputMap = root.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap actionMap = root.getActionMap();

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

        // Backspace
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

        // Enter
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

    // Add a letter
    private void addLetter(char ch) {

        if (!gameFinished && currentRow < 6 && currentCol < 5) {

            tiles[currentRow][currentCol].setText(
                String.valueOf(ch).toUpperCase()
            );

            currentCol++;
        }
    }

    // Delete a letter
    private void deleteLetter() {

        if (!gameFinished && currentRow < 6 && currentCol > 0) {

            currentCol--;

            tiles[currentRow][currentCol].setText("");
        }
    }

    // Submit guess
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

        StringBuilder guessBuilder = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            guessBuilder.append(
                tiles[currentRow][i].getText().toLowerCase()
            );
        }

        String word = guessBuilder.toString();

        // Validate word
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

            Guess guess = game.submit(word);

            Tile[] result = guess.getTiles();

            // Colour evaluated tiles
            for (int i = 0; i < result.length; i++) {

                tiles[currentRow][i].setBackground(
                    getTileColor(result[i].getState())
                );

                tiles[currentRow][i].setForeground(Color.WHITE);
            }

            currentRow++;
            currentCol = 0;

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

    // Tile colours
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

    // Finish game and update statistics
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
