package on_off;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerStatusChecker {

    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main frame
        JFrame frame = new JFrame("Server Status Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Create input fields and labels
        JLabel labelAddress = new JLabel("Server Address:");
        JTextField textAddress = new JTextField();

        JLabel labelPort = new JLabel("Port:");
        JTextField textPort = new JTextField();

        JButton checkButton = new JButton("Check Status");
        JLabel resultLabel = new JLabel(" ", SwingConstants.CENTER);

        // Set layout
        frame.setLayout(new GridLayout(5, 1, 10, 10));
        frame.add(labelAddress);
        frame.add(textAddress);
        frame.add(labelPort);
        frame.add(textPort);
        frame.add(checkButton);
        frame.add(resultLabel);

        // Add action listener to the button
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String address = textAddress.getText().trim();
                String portText = textPort.getText().trim();

                if (address.isEmpty() || portText.isEmpty()) {
                    resultLabel.setText("Please fill in both fields.");
                    return;
                }

                try {
                    int port = Integer.parseInt(portText);
                    boolean isReachable = checkServerStatus(address, port);

                    if (isReachable) {
                        resultLabel.setText("Server is Online.");
                        resultLabel.setForeground(Color.GREEN);
                    } else {
                        resultLabel.setText("Server is Offline.");
                        resultLabel.setForeground(Color.RED);
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid port number.");
                    resultLabel.setForeground(Color.RED);
                } catch (UnknownHostException ex) {
                    resultLabel.setText("Unknown host: " + address);
                    resultLabel.setForeground(Color.RED);
                } catch (IOException ex) {
                    resultLabel.setText("Error: " + ex.getMessage());
                    resultLabel.setForeground(Color.RED);
                }
            }
        });

        // Set frame visible
        frame.setVisible(true);
    }

    private static boolean checkServerStatus(String address, int port) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(address, port), 3000); // Timeout set to 3000 ms
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

