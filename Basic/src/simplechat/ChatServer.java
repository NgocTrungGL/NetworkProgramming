package simplechat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
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
        JFrame frame = new JFrame("Server");
        JTextArea textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            textArea.append("Server đang chạy...\n");
            Socket clientSocket = serverSocket.accept(); // Chờ kết nối từ Client
            textArea.append("Client đã kết nối!\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                textArea.append("Client: " + message + "\n");
                out.println("Server nhận được: " + message);
            }
            textArea.append("Client đã ngắt kết nối.\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Lỗi khi khởi động Server: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
