# Task 03 – Campus Library Search

The interface uses a title bar, a north search row (text field, category selector and Search button), a central scrollable results list, and a south row with Prev, Next, Reserve and a status label. Nested BorderLayout, FlowLayout and JScrollPane components support resizing.

## Resize test plan

| Window size | Expected observation |
|---|---|
| 420 × 280 | Search controls remain visible; result area scrolls |
| 800 × 600 | Comfortable spacing across all regions |
| 1280 × 800 | Results area expands while controls remain grouped |

Capture actual screenshots and record observations after running at these sizes.

## GridBagLayout vs absolute positioning

GridBagLayout adapts to preferred sizes and resizing using row/column constraints, but requires more constraint code. Absolute positioning is quick for fixed mock-ups but uses manually assigned coordinates and breaks more easily across window sizes, fonts and display scaling. The implementation uses nested standard layouts rather than fixed coordinates.