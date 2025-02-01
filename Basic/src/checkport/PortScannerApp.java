package checkport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class PortScannerApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(PortScannerGUI::new);
    }
}

class PortScannerGUI extends JFrame {
    private JTextField ipField;
    private JTextField startPortField, endPortField;
    private JTextArea resultArea;
    private JButton scanButton;

    public PortScannerGUI() {
        setTitle("Port Scanner");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("IP Address / Host:"));
        ipField = new JTextField("localhost");
        inputPanel.add(ipField);

        inputPanel.add(new JLabel("Start Port:"));
        startPortField = new JTextField("1");
        inputPanel.add(startPortField);

        inputPanel.add(new JLabel("End Port:"));
        endPortField = new JTextField("100");
        inputPanel.add(endPortField);

        scanButton = new JButton("Scan");
        scanButton.setPreferredSize(new Dimension(10, 10));
        scanButton.addActionListener(new ScanAction());

        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(inputPanel, BorderLayout.NORTH);
        add(scanButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class ScanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resultArea.setText("Scanning...\n");
            String host = ipField.getText().trim();
            int startPort, endPort;
            try {
                startPort = Integer.parseInt(startPortField.getText().trim());
                endPort = Integer.parseInt(endPortField.getText().trim());
            } catch (NumberFormatException ex) {
                resultArea.setText("Invalid port numbers!");
                return;
            }

            new Thread(() -> scanPorts(host, startPort, endPort)).start();
        }
    }

    private void scanPorts(String host, int startPort, int endPort) {
        for (int port = startPort; port <= endPort; port++) {
            try (Socket socket = new Socket(host, port)) {
                resultArea.append("Port " + port + " is OPEN\n");
            } catch (IOException ignored) {
                resultArea.append("Port " + port + " is CLOSED\n");
            }
        }
        resultArea.append("Scanning completed.\n");
    }
}
