import java.util.ArrayList;

PImage backgroundImage;
ArrayList<Point> lines = new ArrayList<Point>();

int BOARD_WIDTH = 500;
int BOARD_HEIGHT = 700;
int BALL_SIZE = 20;
int BAR_WIDTH = 80;
int BAR_HEIGHT = 10;
int BALL_SPEED = 5;
int LINE_HEIGHT = 30;
int NUM_LINES = 5;

int ballX;
int ballY;
int ballSpeedX = BALL_SPEED;
int ballSpeedY = BALL_SPEED;

int barX;
int barY;

int hits = 0;
int missed = 0;
int score = 0;

boolean gameRunning = true;

void setup() {
  size(500, 700);
  surface.setTitle("ProgrammingProject");

  // Allow the frame to be resizable
  surface.setResizable(true);

  backgroundImage = loadImage("images/background.jpg");

  initializeLines();

  ballX = width / 2;
  ballY = 0;

  barX = (width - BAR_WIDTH) / 2;
  barY = height - BAR_HEIGHT - 117;
}

void draw() {
  background(255);

  drawBackground();
  drawBall();
  drawBar();
  drawStaticTexts();

  if (gameRunning) {
    moveBall();
  }
}

void moveBall() {
  ballX += ballSpeedX;
  ballY += ballSpeedY;

  if (ballX <= 0 || ballX >= width - BALL_SIZE) {
    ballSpeedX = -ballSpeedX;
  }

  if (ballY <= 0) {
    ballSpeedY = -ballSpeedY;
  }

  if (ballY + BALL_SIZE >= barY && ballX >= barX && ballX <= barX + BAR_WIDTH) {
    // Check if the ball was below the bar in the previous move
    if (ballY - ballSpeedY < barY) {
      ballSpeedY = -ballSpeedY;
      hits++;
      score = hits - missed;
    }
  }

  if (ballY >= height) {
    ballX = width / 2;
    ballY = 0;
    ballSpeedY = BALL_SPEED;
    missed++;
    score = hits - missed;
  }
}

void drawBackground() {
  image(backgroundImage, 0, 0, width, height);
}

void drawBall() {
  fill(0);
  ellipse(ballX, ballY, BALL_SIZE, BALL_SIZE);
}

void drawBar() {
  fill(0);
  rect(barX, barY, BAR_WIDTH, BAR_HEIGHT);
}

void drawStaticTexts() {
  fill(0, 0, 255);
  textSize(23);

  text("Missed", 48, height - 2 * LINE_HEIGHT + 11);
  text(Integer.toString(missed), 70, height - LINE_HEIGHT + 11);

  fill(255, 0, 0);
  text("Hits", width / 2 - 40, height - 2 * LINE_HEIGHT + 11);
  text(Integer.toString(hits), width / 2 - 25, height - LINE_HEIGHT + 11);

  fill(0);
  text("Score", width - 120, height - 2 * LINE_HEIGHT + 11);
  text(Integer.toString(score), width - 100, height - LINE_HEIGHT + 11);

  int linePositionY = height - 3 * LINE_HEIGHT - 10;
  fill(0);
  rect(0, linePositionY, width, 1);
}

void initializeLines() {
  for (int i = 0; i < NUM_LINES; i++) {
    int x = (int) random(width - 2);
    int y = (int) random(height - LINE_HEIGHT);
    lines.add(new Point(x, y));
  }
}

void keyPressed() {
  if (keyCode == LEFT && barX > 0) {
    barX -= 20;
  } else if (keyCode == RIGHT && barX < width - BAR_WIDTH) {
    barX += 20;
  } else if (key == 's' || key == 'S') {
    gameRunning = !gameRunning;
  } else if (key == 'r' || key == 'R') {
    resumeGame();
  }
}

void resumeGame() {
  if (!gameRunning) {
    gameRunning = true;
  }
}

class Point {
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
