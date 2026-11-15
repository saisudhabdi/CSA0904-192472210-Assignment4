import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HospitalAppointmentDesk extends JFrame {
    private final JComboBox<String> departmentBox;
    private final JComboBox<String> doctorBox;
    private final JTextField patientField;
    private final JLabel statusLabel;
    private final Map<String, java.util.List<String>> doctors;

    public HospitalAppointmentDesk() {
        setTitle("Hospital Appointment Desk");
        setSize(520, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        doctors = new LinkedHashMap<>();
        doctors.put("Cardiology", Arrays.asList("Dr. Kumar", "Dr. Priya"));
        doctors.put("Neurology", Arrays.asList("Dr. Ravi", "Dr. Meena"));
        doctors.put("Orthopaedics", Arrays.asList("Dr. Anand", "Dr. Divya"));
        doctors.put("General Medicine", Arrays.asList("Dr. Arun", "Dr. Lakshmi"));

        departmentBox = new JComboBox<>(doctors.keySet().toArray(new String[0]));
        doctorBox = new JComboBox<>();
        patientField = new JTextField(18);
        statusLabel = new JLabel("Select a department and doctor.");

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Patient name:"));
        form.add(patientField);
        form.add(new JLabel("Department:"));
        form.add(departmentBox);
        form.add(new JLabel("Doctor:"));
        form.add(doctorBox);

        JButton bookButton = new JButton("Book Appointment");
        JButton clearButton = new JButton("Clear");

        JPanel buttons = new JPanel();
        buttons.add(bookButton);
        buttons.add(clearButton);

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        departmentBox.addActionListener(e -> refreshDoctors());
        bookButton.addActionListener(e -> bookAppointment());
        clearButton.addActionListener(e -> {
            patientField.setText("");
            statusLabel.setText("Form cleared.");
        });

        refreshDoctors();
    }

    private void refreshDoctors() {
        String department = (String) departmentBox.getSelectedItem();
        doctorBox.removeAllItems();
        if (department != null) {
            for (String doctor : doctors.get(department)) {
                doctorBox.addItem(doctor);
            }
            statusLabel.setText("Doctors loaded for " + department + ".");
        }
    }

    private void bookAppointment() {
        String patient = patientField.getText().trim();
        String department = (String) departmentBox.getSelectedItem();
        String doctor = (String) doctorBox.getSelectedItem();

        if (patient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the patient name.", "Validation", JOptionPane.WARNING_MESSAGE);
            patientField.requestFocus();
            return;
        }

        if (department == null || doctor == null) {
            JOptionPane.showMessageDialog(this, "Select a department and doctor.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        statusLabel.setText("Appointment booked for " + patient + " with " + doctor + ".");
        JOptionPane.showMessageDialog(this,
                "Appointment confirmed!\nPatient: " + patient + "\nDepartment: " + department + "\nDoctor: " + doctor,
                "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalAppointmentDesk().setVisible(true));
    }
}