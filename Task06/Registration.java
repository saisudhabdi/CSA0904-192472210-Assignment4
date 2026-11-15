public class Registration {
    private final long id;
    private final String studentName, email, eventName, department, registeredAt;

    public Registration(long id, String studentName, String email, String eventName,
                        String department, String registeredAt) {
        this.id = id;
        this.studentName = studentName;
        this.email = email;
        this.eventName = eventName;
        this.department = department;
        this.registeredAt = registeredAt;
    }
    public long getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getEventName() { return eventName; }
    public String getDepartment() { return department; }
    public String getRegisteredAt() { return registeredAt; }
}
