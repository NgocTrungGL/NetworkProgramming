package guessnumber;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ServerApp {
    private JFrame frame;
    private JTextArea textArea;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int targetNumber;

    public ServerApp() {
        frame = new JFrame("Server - Đoán số");
        textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            serverSocket = new ServerSocket(12345);
            textArea.append("Server đang chờ kết nối...\n");
            clientSocket = serverSocket.accept();
            textArea.append("Client đã kết nối!\n");

            // Tạo số ngẫu nhiên từ 1 đến 100
            targetNumber = (int) (Math.random() * 100) + 1;
            textArea.append("Số ngẫu nhiên đã được tạo.\n");

            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = reader.readLine()) != null) {
                int guess = Integer.parseInt(message);
                if (guess == targetNumber) {
                    writer.println("Chúc mừng! Bạn đã đoán đúng.");
                    textArea.append("Client đoán đúng: " + guess + "\n");
                    break;
                } else if (guess < targetNumber) {
                    writer.println("Số lớn hơn.");
                } else {
                    writer.println("Số nhỏ hơn.");
                }
                textArea.append("Client đoán: " + guess + "\n");
            }
        } catch (IOException | NumberFormatException e) {
            textArea.append("Lỗi: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerApp::new);
    }
}
