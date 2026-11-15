# Task 02 – Hospital Appointment Desk

## Event-source table

| Source | Event | Listener | Action |
|---|---|---|---|
| Department JComboBox | ItemEvent | ItemListener | Refresh doctor list |
| Doctor JComboBox | ItemEvent | ItemListener | Update selected doctor |
| Book button | ActionEvent | ActionListener | Validate and book |
| Clear button | ActionEvent | ActionListener | Clear form |
| Window | WindowEvent | WindowAdapter | Confirm close and dispose |

## Containment tree

```text
JFrame: Hospital Appointment Desk
└── BorderLayout
    ├── NORTH: Header
    ├── CENTER: GridBagLayout form
    │   ├── Department label + JComboBox
    │   ├── Doctor label + JComboBox
    │   ├── Patient name field
    │   ├── Date field
    │   └── Time field
    └── SOUTH: FlowLayout buttons + status
```

## Pseudocode: refresh doctors

```text
ON department selection change:
    department = selected department
    clear doctor combo
    FOR each doctor in doctorMap[department]: add doctor
    disable booking if list is empty; otherwise enable it
```

## Pseudocode: book appointment

```text
ON Book:
    read patient, department, doctor, date and time
    IF required value missing OR date/time invalid:
        show validation message
    ELSE:
        save appointment
        show confirmation
        clear patient entry fields
```

## Lifecycle

Launch the desktop application, create components on the Swing Event Dispatch Thread (EDT), load initial data, show the frame, and process events through listeners. On close, confirm if necessary, dispose the frame and release database/background resources. An applet instead uses init() for initialisation, start() to begin/resume, paint() to render, stop() to pause and destroy() to release resources. JFrame applications do not use this applet lifecycle.

## Preloaded Map vs database lookup

| Approach | Time on department change | Memory | Trade-off |
|---|---|---|---|
| Preloaded Map | Usually low local lookup latency | Holds loaded doctor data | Fast for small stable data; may become stale |
| Query database each time | Includes query/network latency | Lower local data footprint | Better for large/changing data; needs error/loading handling |

Keep database work off the EDT using SwingWorker or an executor.