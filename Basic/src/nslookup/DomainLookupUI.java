package nslookup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class DomainLookupUI {
    public static void main(String[] args) {
        try {
            // Đặt Look and Feel thành Nimbus (nếu khả dụng)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Nếu Nimbus không khả dụng, giữ nguyên Look and Feel mặc định
            System.out.println("Không thể cài đặt Nimbus Look and Feel.");
        }

        // Tạo JFrame chính
        JFrame frame = new JFrame("Tra cứu IP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 250);
        frame.setLayout(new BorderLayout());

        // Tạo panel chứa các thành phần đầu vào
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("TÊN MIỀN:");
        JTextField textField = new JTextField();
        JButton lookupButton = new JButton("Tra cứu IP");
        JButton clearButton = new JButton("Xóa");

        // Căn chỉnh kích thước
        label.setPreferredSize(new Dimension(80, 30));
        lookupButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setPreferredSize(new Dimension(80, 30));

        // Thêm thành phần vào topPanel
        topPanel.add(label, BorderLayout.WEST);
        topPanel.add(textField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(lookupButton);
        buttonPanel.add(clearButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Tạo khu vực hiển thị kết quả
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Thêm sự kiện cho nút "Tra cứu IP"
        lookupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText(""); // Xóa kết quả trước đó
                String domain = textField.getText().trim();
                if (domain.isEmpty()) {
                    resultArea.setText("Vui lòng nhập tên miền trước khi tra cứu.");
                    return;
                }
                try {
                    InetAddress inetAddress = InetAddress.getByName(domain);
                    String ip = inetAddress.getHostAddress();
                    resultArea.append("Tên miền: " + domain + "\n");
                    resultArea.append("Địa chỉ IP: " + ip + "\n");
                } catch (Exception exception) {
                    resultArea.append("Không tìm thấy địa chỉ IP cho tên miền: " + domain + "\n");
                }
            }
        });

        // Thêm sự kiện cho nút "Xóa"
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");  // Xóa ô nhập
                resultArea.setText(""); // Xóa khu vực kết quả
            }
        });

        // Thêm các thành phần vào frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Đặt frame ra giữa màn hình
        frame.setLocationRelativeTo(null);

        // Hiển thị frame
        frame.setVisible(true);
    }
}
