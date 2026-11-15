import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPad extends JFrame {
    private final DrawingPanel canvas = new DrawingPanel();
    private final JLabel status = new JLabel("Click inside the canvas to draw.");

    public DrawPad() {
        super("DrawPad – Persistent Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.addPoint(e.getPoint());
                status.setText("Points drawn: " + canvas.count());
            }
        });
        setSize(640, 420);
        setLocationRelativeTo(null);
    }

    private static class DrawingPanel extends JPanel {
        private final List<Point> points = new ArrayList<>();
        DrawingPanel() { setBackground(Color.WHITE); }
        void addPoint(Point p) { points.add(new Point(p)); repaint(); }
        int count() { return points.size(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            for (Point p : points) g.fillOval(p.x - 3, p.y - 3, 7, 7);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DrawPad().setVisible(true));
    }
}
