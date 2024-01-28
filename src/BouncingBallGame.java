import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BouncingBallGame extends JFrame {

    private static final int BOARD_WIDTH = 500;
    private static final int BOARD_HEIGHT = 700;
    private static final int BALL_SIZE = 20;
    private static final int BAR_WIDTH = 80;
    private static final int BAR_HEIGHT = 10;
    private static final int BALL_SPEED = 5;
    private static final int LINE_HEIGHT = 30;
    private static final int NUM_LINES = 5; // Number of lines to draw

    private int ballX;
    private int ballY;
    private int ballSpeedX = BALL_SPEED;
    private int ballSpeedY = BALL_SPEED;

    private int barX = (BOARD_WIDTH - BAR_WIDTH) / 2;
    private int barY = BOARD_HEIGHT - BAR_HEIGHT - 117; // Raised the bar position

    private int hits = 0;
    private int missed = 0;
    private int score = 0;

    // List to store line positions
    private List<Point> lines = new ArrayList<>();

    public BouncingBallGame() {
        setTitle("Bouncing Ball Game");

        // Set the preferred size of the content pane
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
                drawBall(g);
                drawBar(g);
                drawStaticTexts(g);
            }
        };
        contentPane.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setContentPane(contentPane);

        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize lines with random positions
        initializeLines();

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

        ballX = BOARD_WIDTH / 2;
        ballY = 0;
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0 || ballX >= BOARD_WIDTH - BALL_SIZE) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballY + BALL_SIZE >= barY && ballX >= barX && ballX <= barX + BAR_WIDTH) {
            ballSpeedY = -ballSpeedY;
            hits++;
            score++;
        }

        if (ballY >= BOARD_HEIGHT) {
            ballX = BOARD_WIDTH / 2;
            ballY = 0;
            ballSpeedY = BALL_SPEED;
            hits = 0;
            missed++;
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        Image backgroundImage = getBackgroundImage().getScaledInstance(BOARD_WIDTH, BOARD_HEIGHT, Image.SCALE_SMOOTH);
        g.drawImage(backgroundImage, 0, 0, this);
    }

    private Image getBackgroundImage() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("images.jpg"));
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void drawBall(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    private void drawBar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(barX, barY, BAR_WIDTH, BAR_HEIGHT);
    }

    private void drawStaticTexts(Graphics g) {
        g.setColor(Color.BLUE);
        Font font = new Font("Calibri", Font.TRUETYPE_FONT, 30);
        g.setFont(font);

        // Display each statistic on a new line
        g.drawString("Missed", 20, BOARD_HEIGHT - 2 * LINE_HEIGHT);
        g.drawString(Integer.toString(missed), 50, BOARD_HEIGHT - LINE_HEIGHT);

        g.setColor(Color.RED);
        g.drawString("Hits", BOARD_WIDTH / 2 - 40, BOARD_HEIGHT - 2 * LINE_HEIGHT);
        g.drawString(Integer.toString(hits), BOARD_WIDTH / 2 - 25, BOARD_HEIGHT - LINE_HEIGHT);

        g.setColor(Color.BLACK);
        g.drawString("Score", BOARD_WIDTH - 120, BOARD_HEIGHT - 2 * LINE_HEIGHT);
        g.drawString(Integer.toString(score), BOARD_WIDTH - 100, BOARD_HEIGHT - LINE_HEIGHT);

        // Draw a horizontal line at the bottom of the catching bar
//        g.fillRect(barX, barY + BAR_HEIGHT, BAR_WIDTH, 2);

        // Draw a horizontal line at the top of the scores
//        g.fillRect(0, BOARD_HEIGHT - 3 * LINE_HEIGHT, BOARD_WIDTH, 2);
        // Draw a horizontal line at the top of the scores
        int linePositionY = BOARD_HEIGHT - 3 * LINE_HEIGHT - 10; // Adjust the Y position as needed
        g.fillRect(0, linePositionY, BOARD_WIDTH, 1);
    }

    private void initializeLines() {
        Random random = new Random();
        for (int i = 0; i < NUM_LINES; i++) {
            int x = random.nextInt(BOARD_WIDTH - 2);
            int y = random.nextInt(BOARD_HEIGHT - LINE_HEIGHT);
            lines.add(new Point(x, y));
        }
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

            requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BouncingBallGame game = new BouncingBallGame();
                game.pack();  // Pack the frame to ensure the preferred size is respected
                game.setVisible(true);
            }
        });
    }
}
