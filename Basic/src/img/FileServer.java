package img;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

// Server nhận file và hiển thị ảnh
class FileServer {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Server - Nhận ảnh");
        JLabel label = new JLabel("Chưa nhận ảnh", SwingConstants.CENTER);
        frame.add(label);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server đang chờ kết nối...");
            Socket socket = serverSocket.accept();
            System.out.println("Client đã kết nối!");

            // Nhận file ảnh
            InputStream in = socket.getInputStream();
            File receivedFile = new File("received_image.jpg");
            FileOutputStream fileOut = new FileOutputStream(receivedFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
            fileOut.close();

            // Hiển thị ảnh trên giao diện
            ImageIcon imageIcon = new ImageIcon("received_image.jpg");
            label.setIcon(imageIcon);
            label.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
