# Task 04 – DrawPad Defect Correction

| Defect | Correction |
|---|---|
| mouseclicked has incorrect case | Use mouseClicked exactly |
| Drawing directly with getGraphics is transient | Store coordinates and redraw in paintComponent |
| Status label is stale | Update it after each click |
| Missing/incorrect override check | Add @Override to catch signature errors |

## Drawing pseudocode

```text
ON mouseClicked:
    append a copy of click coordinate to point list
    request repaint
    update status label

WHEN paintComponent runs:
    call super.paintComponent
    draw every stored point
```

To demonstrate the compiler issue, temporarily rename mouseClicked to mouseclicked while retaining @Override, compile, and record the “does not override” diagnostic. Restore the correct method before submission.

| Strategy | Repaint work | Memory |
|---|---:|---:|
| Store N points and redraw | O(N) | O(N) points |
| Retain BufferedImage and blit | O(1) image draw per repaint, excluding update work | O(width × height) pixels |

A point list is simple and resolution-independent. A BufferedImage is useful for complex drawings because repaint can copy one raster image.