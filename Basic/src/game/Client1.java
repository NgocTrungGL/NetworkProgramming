package game;

// Client.java
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client1 {
    private static JButton[] buttons = new JButton[9];
    private static boolean isMyTurn = false;
    private static String myMark = "";
    private static PrintWriter out;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].setEnabled(false);
            panel.add(buttons[i]);

            int index = i;
            buttons[i].addActionListener(e -> makeMove(index));
        }

        frame.add(panel);
        frame.setVisible(true);

        String serverAddress = JOptionPane.showInputDialog(frame, "Enter Server Address:", "localhost");
        int port = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Port:", "12345"));

        try {
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        handleServerMessage(message);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Connection lost.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private static void handleServerMessage(String message) {
        if (message.startsWith("START")) {
            myMark = message.split(" ")[1];
            isMyTurn = myMark.equals("X");
            for (JButton button : buttons) {
                button.setEnabled(true);
            }
        } else if (message.startsWith("MOVE")) {
            String[] parts = message.split(" ");
            int index = Integer.parseInt(parts[1]);
            String mark = parts[2];
            buttons[index].setText(mark);
            buttons[index].setEnabled(false);
            isMyTurn = myMark.equals(mark) ? false : true;
        } else if (message.startsWith("WIN") || message.startsWith("DRAW")) {
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
    }

    private static void makeMove(int index) {
        if (isMyTurn && buttons[index].getText().isEmpty()) {
            buttons[index].setText(myMark);
            buttons[index].setEnabled(false);
            isMyTurn = false;
            out.println("MOVE " + index);
        }
    }
}