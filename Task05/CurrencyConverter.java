import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CurrencyConverter extends JFrame implements ActionListener {
    private final JTextField inr = new JTextField(12);
    private final JLabel output = new JLabel("USD: —");
    private static final double INR_TO_USD = 0.012;

    public CurrencyConverter() {
        super("INR to USD Converter");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton convert = new JButton("Convert (lambda)");
        JButton convertViaFrame = new JButton("Convert (frame listener)");
        JButton clear = new JButton("Clear");
        add(new JLabel("Amount in INR:"));
        add(inr);
        add(convert);
        add(convertViaFrame);
        add(clear);
        add(output);

        convertViaFrame.addActionListener(this);

        clear.addActionListener(new ClearHandler());

        inr.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { convertAmount(); }
        });

        convert.addActionListener(e -> convertAmount());

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(CurrencyConverter.this,
                        "Close the converter?", "Confirm exit", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) dispose();
            }
        });
        getRootPane().setDefaultButton(convert);
        pack();
        setLocationRelativeTo(null);
    }

    @Override public void actionPerformed(ActionEvent e) { convertAmount(); }

    private class ClearHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            inr.setText("");
            output.setText("USD: —");
            inr.requestFocusInWindow();
        }
    }

    private void convertAmount() {
        try {
            double amount = Double.parseDouble(inr.getText().trim());
            if (!Double.isFinite(amount) || amount < 0) throw new NumberFormatException();
            output.setText(String.format("USD: %.2f", amount * INR_TO_USD));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid non-negative INR amount.",
                    "Invalid amount", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverter().setVisible(true));
    }
}
