package img;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
class FileClient {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Client - Gửi ảnh");
        JButton sendButton = new JButton("Chọn ảnh và gửi");
        frame.add(sendButton);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        sendButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (Socket socket = new Socket("localhost", 5000);
                     FileInputStream fileIn = new FileInputStream(file);
                     OutputStream out = socket.getOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileIn.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Đã gửi ảnh thành công!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
