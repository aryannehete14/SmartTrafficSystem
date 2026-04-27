package SmartTrafficSystem.demo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.List;

public class TrafficUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Smart Traffic System");
        frame.setSize(500, 450); // Increased height slightly for better padding fit
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- PADDING & LAYOUT IMPROVEMENTS ---
        // Create a main panel to hold everything with a 20px padding around the edges
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10)); // 10px horizontal and vertical gaps
        frame.add(mainPanel);

        JTextField vehicleField = new JTextField();
        JTextField speedField = new JTextField();
        JTextField zoneField = new JTextField();
        JCheckBox emergencyBox = new JCheckBox("Emergency Vehicle");

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JButton processBtn = new JButton("Process Event");
        // Make the button look a bit more distinct
        processBtn.setBackground(new Color(70, 130, 180));
        processBtn.setForeground(Color.WHITE);
        processBtn.setFocusPainted(false);

        // Add Components to the padded mainPanel
        mainPanel.add(new JLabel("Vehicle ID:"));
        mainPanel.add(vehicleField);

        mainPanel.add(new JLabel("Speed (km/h):"));
        mainPanel.add(speedField);

        mainPanel.add(new JLabel("Zone:"));
        mainPanel.add(zoneField);

        mainPanel.add(emergencyBox);
        mainPanel.add(new JLabel("")); // Spacer

        mainPanel.add(processBtn);

        // Placing output area inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane);

        // Button Logic
        processBtn.addActionListener(e -> {
        try {
            String id = vehicleField.getText();
            double speed = Double.parseDouble(speedField.getText());
            String zone = zoneField.getText();
            boolean isEmergency = emergencyBox.isSelected();

            SmartTrafficSystem.VehicleEvent event =
                    new SmartTrafficSystem.VehicleEvent(
                            id, speed, zone, isEmergency,
                            System.currentTimeMillis()
                    );

            List<SmartTrafficSystem.VehicleEvent> events = Arrays.asList(event);

            List<SmartTrafficSystem.ViolationRecord> violations =
                    events.stream()
                            .filter(SmartTrafficSystem.TrafficRules.violationFilter)
                            .map(ev -> new SmartTrafficSystem.ViolationRecord(
                                    ev.vehicleId,
                                    ev.speed,
                                    ev.zone,
                                    SmartTrafficSystem.TrafficRules.fineCalculator.apply(ev.speed)
                            ))
                            .toList();

            if (violations.isEmpty()) {
                outputArea.setText("No violation detected.");
                outputArea.setForeground(new Color(0, 128, 0));
            } else {
                SmartTrafficSystem.ViolationRecord v = violations.get(0);

                // ✅ SAVE TO DATABASE
                try {
                    Connection con = DBConnection.getConnection();

                    String query = "INSERT INTO violations (vehicle_id, speed, zone, fine, is_emergency) VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, v.vehicleId);
                    pst.setDouble(2, v.speed);
                    pst.setString(3, v.zone);
                    pst.setInt(4, v.fine);
                    pst.setBoolean(5, isEmergency);

                    pst.executeUpdate();
                    con.close();

                } catch (Exception dbEx) {
                    dbEx.printStackTrace();
                }

                // Show output
                outputArea.setText(v.toString());
                outputArea.setForeground(Color.RED);
            }

        } catch (NumberFormatException ex) {
            outputArea.setText("Error: Speed must be a numeric value!");
            outputArea.setForeground(Color.ORANGE);
        } catch (Exception ex) {
            outputArea.setText("Invalid input!");
            outputArea.setForeground(Color.ORANGE);
        }
    });

        frame.setLocationRelativeTo(null); // Centers the window on screen
        frame.setVisible(true);
    }
}