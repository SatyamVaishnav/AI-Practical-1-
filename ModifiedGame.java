import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ModifiedGame {
    private JFrame frame;
    private JLabel currentNumberLabel;
    private JTextField userInput;
    public int currentNumber = 50;
    private static final int MAX_DEPTH = 5;

    private boolean isHumanTurn;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ModifiedGame game = new ModifiedGame();
                game.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ModifiedGame() {
        initialize();
        showWelcomeScreen();
        chooseStartingPlayer();
        if (!isHumanTurn) {
            computerMove();
        }
    }

    private void computerMove() {
        int bestMove = findBestMove(currentNumber);
        currentNumber -= bestMove;
        currentNumberLabel.setText("Current Number: " + currentNumber);

        JOptionPane.showMessageDialog(frame, "Computer subtracts " + bestMove + ".", "Computer Move", JOptionPane.INFORMATION_MESSAGE);
        makeMove(bestMove);
    }


    private void chooseStartingPlayer() {
        int option = JOptionPane.showOptionDialog(frame, "Who should start the game?", "Choose Starting Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Human", "Computer"}, "Human");

        isHumanTurn = option == 0;
    }


    private int findBestMove(int currentNumber) {
        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;
        for (int i = 2; i * i <= currentNumber; i++) {
            if (isSquare(i)) {
                int currentValue = minimax(currentNumber - i * i, 0, false);
                if (currentValue > bestValue) {
                    bestValue = currentValue;
                    bestMove = i * i;
                }
            }
        }
        if (bestMove == -1) {
            for (int i = 2; i * i <= currentNumber; i++) {
                if (isSquare(i)) {
                    bestMove = i * i;
                    break;
                }
            }
        }
        return bestMove;
    }

    private int minimax(int currentNumber, int depth, boolean isMaximizingPlayer) {
        int gameOverStatus = isGameOver();
        if (gameOverStatus != 0) {
            return gameOverStatus;
        }

        if (depth >= MAX_DEPTH) {
            return evaluateState(currentNumber);
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (int i = 2; i * i <= currentNumber; i++) {
                if (isSquare(i)) {
                    int value = minimax(currentNumber - i * i, depth + 1, false);
                    bestValue = Math.max(bestValue, value);
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int i = 2; i * i <= currentNumber; i++) {
                if (isSquare(i)) {
                    int value = minimax(currentNumber - i * i, depth + 1, true);
                    bestValue = Math.min(bestValue, value);
                }
            }
            return bestValue;
        }
    }


    private int evaluateState(int currentNumber) {
        if (currentNumber % 2 == 0) {
            return -1;
        } else {
            return 1;
        }
    }


    static boolean isSquare(int num) {
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num && sqrt != 0 && sqrt != 1;
    }


    private void displayWinner(String winner) {
        JOptionPane.showMessageDialog(frame, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWelcomeScreen() {
        JOptionPane.showMessageDialog(null, "Welcome to the Modified Game!\n\n" +
                "In this game, you and the computer will take turns subtracting a square of an integer from the current number.\n" +
                "The player who brings the current number to 0 wins!\n\n" +
                "To play, simply enter a number in the box below and click 'Make Move'.\n\n" +
                "Click 'OK' to start the game.", "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showRules() {
        JOptionPane.showMessageDialog(frame, "Rules:\n\n" +
                "1. The game starts with a current number of " + currentNumber + ".\n" +
                "2. Players take turns subtracting a square of an integer (other than 0 or 1) from the current number.\n" +
                "3. The player who brings the current number to 0 wins the game.\n" +
                "4. The computer will always play optimally and try to win the game.\n" +
                "5. You can choose the maximum depth of the minimax algorithm to adjust the difficulty.", "Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initialize() {
        frame = new JFrame("Modified Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        currentNumberLabel = new JLabel("Current Number: " + currentNumber);
        frame.add(currentNumberLabel);

        userInput = new JTextField(10);
        frame.add(userInput);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userMove();
            }
        });
        frame.add(enterButton);

        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRules();
            }
        });
        frame.add(rulesButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.add(exitButton);
    }


    private int isGameOver() {
        if (currentNumber == 0) {
            return 1; // Human player wins
        }

        // Check if there are any valid moves left
        for (int i = 2; i * i <= currentNumber; i++) {
            if (isSquare(i)) {
                return 0; // Game is still ongoing
            }
        }

        // No valid moves left, computer wins
        return -1;
    }


    private void displayWinner(int winner) {
        if (winner == 1) {
            JOptionPane.showMessageDialog(frame, "Congratulations, you win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else if (winner == -1) {
            JOptionPane.showMessageDialog(frame, "Sorry, computer wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void makeMove(int move) {
        currentNumber -= move;
        currentNumberLabel.setText("Current Number: " + currentNumber);
        int gameOverStatus = isGameOver();
        if (gameOverStatus != 0) {
            displayWinner(gameOverStatus);
            startNewGame();
        } else {
            computerMove();
        }
    }

    private void startNewGame() {
        currentNumber = 50;
        currentNumberLabel.setText("Current Number: " + currentNumber);

        // Reset the turn to human player
        isHumanTurn = true;

        // Choose starting player
        int option = JOptionPane.showOptionDialog(frame, "Who should start the game?", "Choose Starting Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Human", "Computer"}, "Human");

        if (option == 1) {
            // If computer starts, find and subtract the best move
            isHumanTurn = false;
            int bestMove = findBestMove(currentNumber);
            currentNumber -= bestMove;
            currentNumberLabel.setText("Current Number: " + currentNumber);
            JOptionPane.showMessageDialog(frame, "Computer subtracts " + bestMove + ".", "Computer Move", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void userMove() {
        int userMove;
        try {
            userMove = Integer.parseInt(userInput.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid square of an integer.");
            return;
        }
        if (userMove <= 1 || userMove >= currentNumber || !isSquare(userMove)) {
            JOptionPane.showMessageDialog(frame, "Invalid move. Please enter a valid square of an integer.");
            return;
        }

        currentNumber -= userMove;
        currentNumberLabel.setText("Current Number: " + currentNumber);
        int gameOverStatus = isGameOver();
        if (gameOverStatus != 0) {
            displayWinner(gameOverStatus);
        } else {
            if (isHumanTurn) {
                isHumanTurn = false;
                computerMove();
            } else {
                isHumanTurn = true;
            }
        }
    }
}