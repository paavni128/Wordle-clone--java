
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {

    private JLabel[][] tiles = new JLabel[6][5];

    private int currentRow = 0;
    private int currentCol = 0;

    public GameWindow() {

        // Window settings
        setTitle("Wordle Game");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("WORDLE", JLabel.CENTER);

        heading.setFont(
            new Font("Arial", Font.BOLD, 36)
        );

        add(heading, BorderLayout.NORTH);

        // Grid panel
        JPanel gridPanel = new JPanel();

        gridPanel.setLayout(
            new GridLayout(6, 5, 8, 8)
        );

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
                    BorderFactory.createLineBorder(
                        Color.GRAY, 2
                    )
                );

                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(Color.WHITE);

                gridPanel.add(tiles[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // On-screen keyboard panel
        JPanel keyboardPanel = new JPanel();

        keyboardPanel.setLayout(
            new GridLayout(3, 1, 5, 5)
        );

        keyboardPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 15, 10)
        );

        // First keyboard row
        keyboardPanel.add(
            createKeyboardRow("QWERTYUIOP")
        );

        // Second keyboard row
        keyboardPanel.add(
            createKeyboardRow("ASDFGHJKL")
        );

        // Third keyboard row
        JPanel thirdRow = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 2, 2)
        );

        thirdRow.add(
            createActionButton("ENTER", "ENTER")
        );

        thirdRow.add(
            createKeyboardRow("ZXCVBNM")
        );

        thirdRow.add(
            createActionButton("DEL", "BACKSPACE")
        );

        keyboardPanel.add(thirdRow);

        add(keyboardPanel, BorderLayout.SOUTH);

        // Physical keyboard input
        addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {

                char ch = e.getKeyChar();

                if (Character.isLetter(ch)) {
                    addLetter(ch);
                }
            }

            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    deleteLetter();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitRow();
                }
            }
        });

        setFocusable(true);
    }

    // Create a row of letter buttons
    private JPanel createKeyboardRow(String letters) {

        JPanel row = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 2, 2)
        );

        for (char ch : letters.toCharArray()) {

            JButton button = new JButton(
                String.valueOf(ch)
            );

            button.setPreferredSize(
                new Dimension(38, 48)
            );

            button.setFont(
                new Font("Arial", Font.BOLD, 16)
            );

            button.setMargin(
                new Insets(2, 2, 2, 2)
            );

            button.setFocusable(false);

            // Button click action
            button.addActionListener(e -> addLetter(ch));

            row.add(button);
        }

        return row;
    }

    // Create ENTER and DEL buttons
    private JButton createActionButton(
            String text, String action) {

        JButton button = new JButton(text);

        if (action.equals("ENTER")) {

            button.setPreferredSize(
                new Dimension(85, 48)
            );

        } else {

            button.setPreferredSize(
                new Dimension(55, 48)
            );
        }

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setMargin(
            new Insets(2, 2, 2, 2)
        );

        button.setFocusable(false);

        button.addActionListener(e -> {

            if (action.equals("ENTER")) {
                submitRow();
            }

            else if (action.equals("BACKSPACE")) {
                deleteLetter();
            }
        });

        return button;
    }

    // Add a letter to the current tile
    private void addLetter(char ch) {

        if (currentRow < 6 && currentCol < 5) {

            tiles[currentRow][currentCol].setText(
                String.valueOf(ch).toUpperCase()
            );

            currentCol++;
        }
    }

    // Delete the previous letter
    private void deleteLetter() {

        if (currentRow < 6 && currentCol > 0) {

            currentCol--;

            tiles[currentRow][currentCol].setText("");
        }
    }

    // Move to the next row
    private void submitRow() {

        if (currentRow < 6 && currentCol == 5) {

            currentRow++;
            currentCol = 0;

        } else if (currentCol < 5) {

            JOptionPane.showMessageDialog(
                this,
                "Enter all 5 letters first!"
            );
        }

        if (currentRow == 6) {

            JOptionPane.showMessageDialog(
                this,
                "No more attempts!"
            );
        }
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            GameWindow window = new GameWindow();

            window.setVisible(true);

            window.requestFocusInWindow();
        });
    }
}
