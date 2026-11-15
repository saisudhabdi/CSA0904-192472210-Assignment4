# Task 01 – GUI Design Review and Redesign

## 1. Common defects in a poorly designed FeeWindow

1. **Business logic inside event handlers:** calculation rules become difficult to test and maintain.
2. **Repeated conditional dispatch:** a long if/else chain grows as fee types are added.
3. **No input validation:** blank, non-numeric or negative amounts can cause incorrect results or exceptions.
4. **Tight coupling:** UI components and fee rules depend directly on each other.
5. **Poor feedback:** users are not told why an input was rejected.
6. **No clear/reset operation:** users must manually remove old input and results.
7. **Unclear event ownership:** one generic handler may make individual button behaviour difficult to understand.

## 2. Redesign

- `FeeWindow` owns Swing components, layout and event handling.
- `FeeService` owns fee calculation rules.
- Each button has a dedicated listener.
- Invalid input is reported with a dialog.
- The Calculate button is the default button, so Enter can trigger calculation.

## 3. Event flow

1. User enters an amount and selects a fee type.
2. User clicks Calculate or presses Enter.
3. The ActionListener invokes the window's calculate method.
4. The UI validates and parses the amount.
5. FeeService calculates the value.
6. The result is displayed in the result label.

## 4. Dispatch comparison

| Approach | Time per event | Listener/dispatch space | Comment |
|---|---:|---:|---|
| One central handler with k-way if/else dispatch | O(k) worst case | O(1) dispatch structure, excluding controls | Handler may become lengthy |
| Separate listener registered for each button | O(1) event dispatch on average | O(k) listener registrations/objects | Clearer separation of actions |

The O(1) claim for separate listeners refers to selecting the registered callback; the callback's own work may take additional time.

## 5. Defence (under 100 words)

The redesigned interface separates presentation from fee calculation. FeeWindow manages user interaction, validation and output, while FeeService contains the calculation rules. This separation improves testability, reuse and maintainability. Dedicated listeners make each button's behaviour explicit and reduce dependence on a large conditional dispatcher. Input validation prevents invalid values from reaching the service. The design can be extended with new fee categories without placing all calculation logic inside the GUI class.
