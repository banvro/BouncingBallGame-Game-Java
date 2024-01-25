import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BouncingBallGame extends JFrame {

    private static final int BOARD_WIDTH = 500;
    private static final int BOARD_HEIGHT = 700;
    private static final int BALL_SIZE = 20;
    private static final int BAR_WIDTH = 80;
    private static final int BAR_HEIGHT = 10;
    private static final int BALL_SPEED = 5;

    private int ballX;
    private int ballY;
    private int ballSpeedX = BALL_SPEED;
    private int ballSpeedY = BALL_SPEED;

    private int barX = (BOARD_WIDTH - BAR_WIDTH) / 2;
    private int barY = BOARD_HEIGHT - BAR_HEIGHT - 20;

    private int hits = 0;

    public BouncingBallGame() {
        setTitle("Bouncing Ball Game");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBall();
                repaint();
            }
        });
        timer.start();

        setFocusable(true);
        addKeyListener(new BarKeyListener());

        // Initialize the ball position
        ballX = BOARD_WIDTH / 2;
        ballY = 0;

        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
                drawBall(g);
                drawBar(g);
                drawHits(g);
            }
        });
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Bounce off the walls
        if (ballX <= 0 || ballX >= BOARD_WIDTH - BALL_SIZE) {
            ballSpeedX = -ballSpeedX;
        }

        // Bounce off the top
        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        // Check if the ball hits the bar
        if (ballY + BALL_SIZE >= barY && ballX >= barX && ballX <= barX + BAR_WIDTH) {
            ballSpeedY = -ballSpeedY;
            hits++; // Increase hits when the bar catches the ball
        }

        // Check if the ball falls off the screen
        if (ballY >= BOARD_HEIGHT) {
            ballX = BOARD_WIDTH / 2;
            ballY = 0;
            ballSpeedY = BALL_SPEED;
            hits = 0; // Reset hits when the ball falls off the screen
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.WHITE); // Set background color
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        // Draw the image in the background
        g.drawImage(getBackgroundImage(), 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
    }

    private Image getBackgroundImage() {
        ImageIcon imageIcon = new ImageIcon("https://i.pinimg.com/736x/df/6d/9b/df6d9b173e3e4b0521d5134548f3e4a5.jpg");
        return imageIcon.getImage();
    }

    private void drawBall(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    private void drawBar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(barX, barY, BAR_WIDTH, BAR_HEIGHT);
    }

    private void drawHits(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Hits: " + hits, 10, 20);
    }

    private class BarKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && barX > 0) {
                barX -= 20;
            } else if (key == KeyEvent.VK_RIGHT && barX < BOARD_WIDTH - BAR_WIDTH) {
                barX += 20;
            }

            // Ensure the JFrame has focus
            requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BouncingBallGame().setVisible(true);
            }
        });
    }
}
