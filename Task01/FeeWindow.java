import javax.swing.*;
import java.awt.*;

public class FeeWindow extends JFrame {
    private final FeeService feeService = new FeeService();
    private final JTextField amountField = new JTextField(12);
    private final JComboBox<String> feeType =
            new JComboBox<>(new String[]{"Tuition", "Laboratory", "Library", "Sports"});
    private final JLabel resultLabel = new JLabel("Calculated fee: —");

    public FeeWindow() {
        super("Student Fee Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(new JLabel("Fee type:"));
        form.add(feeType);
        form.add(new JLabel("Base amount (INR):"));
        form.add(amountField);

        JButton calculateButton = new JButton("Calculate");
        JButton clearButton = new JButton("Clear");
        form.add(calculateButton);
        form.add(clearButton);

        resultLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        add(form, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        calculateButton.addActionListener(e -> calculate());
        clearButton.addActionListener(e -> {
            amountField.setText("");
            resultLabel.setText("Calculated fee: —");
            amountField.requestFocusInWindow();
        });

        getRootPane().setDefaultButton(calculateButton);
        pack();
        setLocationRelativeTo(null);
    }

    private void calculate() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            String selectedType = (String) feeType.getSelectedItem();
            double fee = feeService.calculateFee(selectedType, amount);
            resultLabel.setText(String.format("Calculated fee: INR %.2f", fee));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.",
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
