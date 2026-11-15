import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarySearchApp extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category =
            new JComboBox<>(new String[]{"All", "Books", "Journals", "Theses"});
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> results = new JList<>(model);
    private final JLabel status = new JLabel("Enter a keyword to search.");
    private final List<String> catalogue = List.of(
            "Java: The Complete Reference — Book",
            "Database System Concepts — Book",
            "Artificial Intelligence Review — Journal",
            "Machine Learning for Campus Services — Thesis",
            "Data Structures and Algorithms — Book");

    public LibrarySearchApp() {
        super("Campus Library Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchButton = new JButton("Search");
        search.add(new JLabel("Search:"));
        search.add(query);
        search.add(category);
        search.add(searchButton);
        add(search, BorderLayout.NORTH);

        results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(results), BorderLayout.CENTER);

        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(prev);
        controls.add(next);
        controls.add(reserve);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(controls, BorderLayout.NORTH);
        bottom.add(status, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> search());
        query.addActionListener(e -> search());
        prev.addActionListener(e -> move(-1));
        next.addActionListener(e -> move(1));
        reserve.addActionListener(e -> reserve());
        pack();
        setMinimumSize(new Dimension(420, 280));
        setLocationRelativeTo(null);
        display(catalogue);
    }

    private void search() {
        String q = query.getText().trim().toLowerCase();
        String type = (String) category.getSelectedItem();
        List<String> found = new ArrayList<>();
        for (String item : catalogue) {
            boolean typeMatch = "All".equals(type)
                    || item.toLowerCase().endsWith(type.substring(0, type.length() - 1).toLowerCase());
            if ((q.isEmpty() || item.toLowerCase().contains(q)) && typeMatch) found.add(item);
        }
        display(found);
    }

    private void display(List<String> values) {
        model.clear();
        for (String value : values) model.addElement(value);
        status.setText(values.size() + " result(s) found.");
        if (!values.isEmpty()) results.setSelectedIndex(0);
    }

    private void move(int delta) {
        if (model.isEmpty()) return;
        int index = Math.max(0, Math.min(model.size() - 1, results.getSelectedIndex() + delta));
        results.setSelectedIndex(index);
        results.ensureIndexIsVisible(index);
    }

    private void reserve() {
        String item = results.getSelectedValue();
        if (item == null) status.setText("Select a result first.");
        else JOptionPane.showMessageDialog(this, "Reservation request: " + item);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchApp().setVisible(true));
    }
}
