package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;

    public ChatClient() {
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    out.println(message);
                    inputField.setText("");
                }
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            out = new PrintWriter(output, true);

            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append(message + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Không thể kết nối tới server.\n");
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}

