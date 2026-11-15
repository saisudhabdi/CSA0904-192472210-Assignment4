# Task 05 – Currency Converter Event Handling

| Style | Implementation | Use |
|---|---|---|
| Frame implements ActionListener | actionPerformed in the JFrame | Central handler for related actions |
| Named inner class | ClearHandler | Reusable named handler |
| Anonymous inner class | Input field listener | One-off local handler |
| Lambda | Convert button listener | Concise functional-interface callback |

The example uses a fixed illustrative rate of 0.012 USD per INR; it is not a live exchange rate. Enter in the input field triggers conversion, and the Convert button is the root pane's default button. WindowAdapter confirms before closing.

## Test cases

| Input | Expected result |
|---|---|
| 100 | USD: 1.20 |
| 0 | USD: 0.00 |
| abc | Warning dialog |
| -10 | Warning dialog |
| Empty | Warning dialog |

Record genuine observed results and screenshots after execution.