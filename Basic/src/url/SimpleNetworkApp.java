package url;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleNetworkApp {

    public static void main(String[] args) {
        // Tạo JFrame
        JFrame frame = new JFrame("Simple Network App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Tạo các thành phần giao diện
        JLabel urlLabel = new JLabel("Enter URL:");
        JTextField urlField = new JTextField();
        JButton fetchButton = new JButton("Fetch");
        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        // Tạo layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(urlLabel, BorderLayout.WEST);
        topPanel.add(urlField, BorderLayout.CENTER);
        topPanel.add(fetchButton, BorderLayout.EAST);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Xử lý sự kiện nút Fetch
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urlString = urlField.getText().trim();
                if (urlString.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a URL!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Xử lý tải nội dung từ URL
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    in.close();

                    // Hiển thị nội dung lên JTextArea
                    contentArea.setText(content.toString());
                } catch (Exception ex) {
                    contentArea.setText("Error fetching content: " + ex.getMessage());
                }
            }
        });

        // Hiển thị JFrame
        frame.setVisible(true);
    }
}

