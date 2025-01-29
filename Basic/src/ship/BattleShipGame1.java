package ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class BattleShipGame1 {

    private static final int GRID_SIZE = 5;
    private JFrame frame;
    private JButton[][] buttons;
    private boolean[][] shipPositions;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isMyTurn = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleShipGame1().start());
    }

    public void start() {
        String[] options = {"Host Game", "Join Game"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an option", "Battle Ship Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        try {
            if (choice == 0) {
                ServerSocket serverSocket = new ServerSocket(12345);
                JOptionPane.showMessageDialog(null, "Waiting for opponent to connect...");
                socket = serverSocket.accept();
                isMyTurn = true;
            } else {
                String serverAddress = JOptionPane.showInputDialog("Enter server address:");
                socket = new Socket(serverAddress, 12345);
            }

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            setupGame();
            new Thread(this::listenForOpponent).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupGame() {
        frame = new JFrame("Battle Ship Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        shipPositions = new boolean[GRID_SIZE][GRID_SIZE];

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setEnabled(false);
                buttons[i][j].addActionListener(new CellClickListener(i, j));
                gridPanel.add(buttons[i][j]);
            }
        }

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        placeShips();

        if (isMyTurn) {
            enableButtons();
        }
    }

    private void placeShips() {
        int shipsToPlace = 3;
        while (shipsToPlace > 0) {
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);
            if (!shipPositions[row][col]) {
                shipPositions[row][col] = true;
                shipsToPlace--;
            }
        }
    }

    private void enableButtons() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!buttons[i][j].isEnabled()) {
                    buttons[i][j].setEnabled(true);
                }
            }
        }
    }

    private void disableButtons() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void listenForOpponent() {
        try {
            while (true) {
                String message = in.readLine();
                if (message.startsWith("MOVE")) {
                    String[] parts = message.split(",");
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);
                    if (shipPositions[row][col]) {
                        buttons[row][col].setBackground(Color.RED);
                        out.println("HIT," + row + "," + col);
                    } else {
                        buttons[row][col].setBackground(Color.BLUE);
                        out.println("MISS," + row + "," + col);
                    }
                    isMyTurn = true;
                    enableButtons();
                } else if (message.startsWith("HIT") || message.startsWith("MISS")) {
                    String[] parts = message.split(",");
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);
                    buttons[row][col].setBackground(message.startsWith("HIT") ? Color.RED : Color.BLUE);
                    disableButtons();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CellClickListener implements ActionListener {
        private final int row;
        private final int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isMyTurn) {
                return;
            }

            buttons[row][col].setEnabled(false);
            out.println("MOVE," + row + "," + col);
            isMyTurn = false;
            disableButtons();
        }
    }
}
