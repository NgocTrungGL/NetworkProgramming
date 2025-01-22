package simplechat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus Look and Feel không khả dụng.");
        }

        // Create GUI
        JFrame frame = new JFrame("Client");
        JTextArea textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Gửi");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 12345); // Kết nối tới Server
            textArea.append("Kết nối thành công tới Server.\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread để nhận tin nhắn từ Server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        textArea.append("Server: " + message + "\n");
                    }
                } catch (IOException e) {
                    textArea.append("Mất kết nối tới Server.\n");
                }
            }).start();

            // Gửi tin nhắn tới Server
            sendButton.addActionListener((ActionEvent e) -> {
                String message = inputField.getText().trim();
                if (!message.isEmpty()) {
                    out.println(message); // Gửi tin nhắn
                    textArea.append("Bạn: " + message + "\n");
                    inputField.setText("");
                }
            });

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể kết nối tới Server: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
