package file;


// Server nhận file
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Server nhận file
public class FileServer {
    public static void main(String[] args) {
        int port = 5000;
        String saveDir = "D:/received_files";
        new File(saveDir).mkdirs(); // Tạo thư mục nếu chưa có

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang lắng nghe trên cổng " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new FileReceiver(socket, saveDir)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileReceiver implements Runnable {
    private Socket socket;
    private String saveDir;

    public FileReceiver(Socket socket, String saveDir) {
        this.socket = socket;
        this.saveDir = saveDir;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             FileOutputStream fos = new FileOutputStream(saveDir + "/" + dis.readUTF())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            System.out.println("File đã nhận thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}