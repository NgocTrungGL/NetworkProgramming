package guessnumber;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientApp {
    private JFrame frame;
    private JTextField inputField;
    private JTextArea textArea;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientApp() {
        frame = new JFrame("Client - Đoán số");
        textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        inputField = new JTextField(20);
        JButton sendButton = new JButton("Gửi");

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(sendButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        sendButton.addActionListener(e -> sendGuess());

        try {
            socket = new Socket("localhost", 12345);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            textArea.append("Không thể kết nối tới server.\n");
        }
    }

    private void sendGuess() {
        String guess = inputField.getText();
        if (!guess.isEmpty()) {
            writer.println(guess);
            inputField.setText("");
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                textArea.append("Server: " + message + "\n");
            }
        } catch (IOException e) {
            textArea.append("Lỗi khi nhận tin nhắn từ server.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientApp::new);
    }
}
