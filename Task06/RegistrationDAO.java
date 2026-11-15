import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {
    private final String url, user, password;

    public RegistrationDAO() {
        url = System.getenv().getOrDefault("DB_URL",
                "jdbc:mysql://localhost:3306/campus_events");
        user = System.getenv().getOrDefault("DB_USER", "root");
        password = System.getenv().getOrDefault("DB_PASSWORD", "");
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void insert(String name, String email, String event, String department)
            throws SQLException {
        String sql = "INSERT INTO registrations(student_name,student_email,event_name,department) VALUES(?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, event);
            ps.setString(4, department);
            ps.executeUpdate();
        }
    }

    public List<Registration> fetchPage(int limit, int offset, String filter)
            throws SQLException {
        String sql = "SELECT registration_id,student_name,student_email,event_name,department,registered_at " +
                "FROM registrations WHERE event_name LIKE ? OR student_name LIKE ? " +
                "ORDER BY registered_at DESC LIMIT ? OFFSET ?";
        List<Registration> rows = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            String pattern = "%" + filter + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Registration(rs.getLong("registration_id"),
                            rs.getString("student_name"), rs.getString("student_email"),
                            rs.getString("event_name"), rs.getString("department"),
                            rs.getTimestamp("registered_at").toString()));
                }
            }
        }
        return rows;
    }
}
