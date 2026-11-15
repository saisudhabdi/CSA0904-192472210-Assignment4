import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class RegistrationService {
    private final RegistrationDAO dao;
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public RegistrationService(RegistrationDAO dao) { this.dao = dao; }

    public void register(String name, String email, String event, String department)
            throws SQLException {
        if (name == null || name.isBlank() || email == null || !EMAIL.matcher(email).matches()
                || event == null || event.isBlank()
                || department == null || department.isBlank()) {
            throw new IllegalArgumentException("Enter all fields and a valid email address.");
        }
        try {
            dao.insert(name.trim(), email.trim(), event.trim(), department.trim());
        } catch (SQLException ex) {
            if ("23000".equals(ex.getSQLState())) {
                throw new IllegalArgumentException(
                        "This student is already registered for this event.");
            }
            throw ex;
        }
    }

    public List<Registration> page(int pageSize, int offset, String filter)
            throws SQLException {
        return dao.fetchPage(pageSize, offset, filter == null ? "" : filter.trim());
    }
}
