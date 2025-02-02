package checkprocess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileWriter;
import java.io.PrintWriter;

public class NetworkDeviceScanner extends JFrame {
    private JTextField subnetField;
    private JTextArea resultArea;
    private JButton scanButton, exportButton;
    private StringBuilder scanResults = new StringBuilder();

    public NetworkDeviceScanner() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Network Device Scanner");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Subnet (e.g., 192.168.1.):"));
        subnetField = new JTextField(10);
        subnetField.setText("192.168.1.");
        inputPanel.add(subnetField);

        scanButton = new JButton("Scan");
        scanButton.addActionListener(new ScanAction());
        inputPanel.add(scanButton);

        exportButton = new JButton("Export");
        exportButton.addActionListener(new ExportAction());
        inputPanel.add(exportButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private class ScanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resultArea.setText("Scanning network...\n");
            scanResults.setLength(0);
            scanResults.append("Scanning network...\n");
            String subnet = subnetField.getText().trim();
            ExecutorService executor = Executors.newFixedThreadPool(20);

            for (int i = 1; i <= 255; i++) {
                String host = subnet + i;
                executor.execute(() -> scanHost(host));
            }
            executor.shutdown();
        }
    }

    private void scanHost(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isReachable(100)) {
                String result = host + " (" + address.getCanonicalHostName() + ") is ONLINE\n";
                SwingUtilities.invokeLater(() -> {
                    resultArea.append(result);
                    scanResults.append(result);
                });
            }
        } catch (IOException ignored) {
        }
    }

    private class ExportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("scan_results.txt"))) {
                writer.write(scanResults.toString());
                JOptionPane.showMessageDialog(null, "Results exported successfully!", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkDeviceScanner::new);
    }
}