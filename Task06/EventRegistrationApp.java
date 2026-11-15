import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class EventRegistrationApp extends JFrame {
    private final RegistrationService service =
            new RegistrationService(new RegistrationDAO());
    private final JTextField name = new JTextField(16);
    private final JTextField email = new JTextField(16);
    private final JTextField event = new JTextField(16);
    private final JComboBox<String> department = new JComboBox<>(
            new String[]{"CSE", "IT", "ECE", "EEE", "Mechanical", "Civil"});
    private final JTextField filter = new JTextField(12);
    private final JLabel status = new JLabel("Ready");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Student", "Email", "Event", "Department", "Registered"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private int page = 0;
    private static final int PAGE_SIZE = 25;

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("New Registration"));
        form.add(new JLabel("Student name:")); form.add(name);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Event:")); form.add(event);
        form.add(new JLabel("Department:")); form.add(department);
        JButton register = new JButton("Register");
        JButton clear = new JButton("Clear");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(register); actions.add(clear);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton search = new JButton("Filter");
        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton export = new JButton("Export CSV");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Filter name/event:")); bottom.add(filter);
        bottom.add(search); bottom.add(previous); bottom.add(next); bottom.add(export);
        bottom.add(status);
        add(bottom, BorderLayout.SOUTH);

        register.addActionListener(e -> register());
        clear.addActionListener(e -> clearForm());
        search.addActionListener(e -> { page = 0; loadPage(); });
        filter.addActionListener(e -> { page = 0; loadPage(); });
        previous.addActionListener(e -> { if (page > 0) { page--; loadPage(); } });
        next.addActionListener(e -> { page++; loadPage(); });
        export.addActionListener(e -> exportCsv());

        setJMenuBar(createMenu());
        setSize(950, 580);
        setLocationRelativeTo(null);
        loadPage();
    }

    private JMenuBar createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exit = new JMenuItem("Exit");
        refresh.addActionListener(e -> loadPage());
        exit.addActionListener(e -> dispose());
        file.add(refresh); file.addSeparator(); file.add(exit);
        bar.add(file);
        return bar;
    }

    private void register() {
        String n = name.getText(), em = email.getText(), ev = event.getText();
        String dep = (String) department.getSelectedItem();
        status.setText("Saving...");
        setFormEnabled(false);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                service.register(n, em, ev, dep);
                return null;
            }
            @Override protected void done() {
                setFormEnabled(true);
                try {
                    get();
                    status.setText("Registration saved.");
                    clearForm();
                    page = 0;
                    loadPage();
                } catch (Exception ex) {
                    status.setText("Registration failed.");
                    JOptionPane.showMessageDialog(EventRegistrationApp.this,
                            rootMessage(ex), "Registration error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void loadPage() {
        status.setText("Loading...");
        new SwingWorker<List<Registration>, Void>() {
            @Override protected List<Registration> doInBackground() throws Exception {
                return service.page(PAGE_SIZE, page * PAGE_SIZE, filter.getText());
            }
            @Override protected void done() {
                try {
                    List<Registration> rows = get();
                    model.setRowCount(0);
                    for (Registration r : rows) model.addRow(new Object[]{
                            r.getId(), r.getStudentName(), r.getEmail(), r.getEventName(),
                            r.getDepartment(), r.getRegisteredAt()});
                    status.setText("Page " + (page + 1) + " — " + rows.size() + " row(s)");
                } catch (Exception ex) {
                    status.setText("Database unavailable or query failed.");
                    JOptionPane.showMessageDialog(EventRegistrationApp.this,
                            rootMessage(ex), "Database error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private String rootMessage(Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) cause = cause.getCause();
        return cause.getMessage() == null ? cause.toString() : cause.getMessage();
    }

    private void setFormEnabled(boolean enabled) {
        name.setEnabled(enabled); email.setEnabled(enabled);
        event.setEnabled(enabled); department.setEnabled(enabled);
    }

    private void clearForm() {
        name.setText(""); email.setText(""); event.setText("");
        department.setSelectedIndex(0);
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("registrations.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (PrintWriter out = new PrintWriter(chooser.getSelectedFile())) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) out.print(",");
                out.print(quote(model.getColumnName(c)));
            }
            out.println();
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) out.print(",");
                    out.print(quote(String.valueOf(model.getValueAt(r, c))));
                }
                out.println();
            }
            status.setText("CSV exported.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Export error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true));
    }
}
