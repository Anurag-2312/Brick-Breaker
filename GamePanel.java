// GamePanel.java
package brickBreaker;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private static final int win_width = 900;
    private static final int win_height = 700;
    private static final int brick_rows = 6;
    private static final int brick_cols = 10;
    private static final int ball_size = 20;
    private static final int paddle_width = 120;
    private static final int paddle_height = 10;

    private boolean isPlaying = false;
    private int currentScore = 0;
    private int bricksRemaining = brick_rows * brick_cols;
    private Timer gameTimer;
    private int speed = 8;
    private int paddleX = win_width / 2 - paddle_width / 2;
    private int ballX = win_width / 2;
    private int ballY = win_height - 150;
    private int ballXSpeed = -1;
    private int ballYSpeed = -2;
    private BrickManager brickLayout;

    public GamePanel() {
        /*
         * Instructions... ---thisizaro
         * Change this annoying constants to variables... Make global variables for the
         * number of rows and columns of the bricks. AND MOST IMPORTANTLY THE WINDOW
         * SIZE!!
         * IT TURNS INTO MESSED UP CODE IF YOU USE CONSTANTS LIKE THIS... YOU NEED TO
         * CHANGE THE VALUE
         * EVERYWHERE LATER...
         * So please just make a variable and use it everywhere...
         * 
         * for the window.
         * the window size is 900x700 for now but it is creating an issue in the
         * actionPerformed function since it has some other value... Fix the issue...
         */
        brickLayout = new BrickManager(brick_rows, brick_cols);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setPreferredSize(new Dimension(win_width, win_height));
        gameTimer = new Timer(speed, this);
        gameTimer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, win_width, win_height);

        // Draw bricks
        brickLayout.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, win_height);
        g.fillRect(0, 0, win_width, 3);
        g.fillRect(win_width - 1, 0, 3, win_height);

        // Score Display
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: ", 600, 30);
        g.drawString("" + currentScore, 700, 30);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(paddleX, win_height - 100, paddle_width, paddle_height);

        // Ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ball_size, ball_size);

        // Game won scenario
        if (bricksRemaining <= 0) {
            isPlaying = false;
            ballXSpeed = 0;
            ballYSpeed = 0;
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Win! ", 350, 350);
            g.drawString("Score: " + currentScore, 350, 400);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 330, 450);
        }

        // Game over scenario
        if (ballY > 670) {
            isPlaying = false;
            ballXSpeed = 0;
            ballYSpeed = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! ", 320, 350);
            g.drawString("Score: " + currentScore, 350, 400);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 330, 450);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameTimer.start();
        if (isPlaying) {
            if (new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(paddleX, 600, 120, 10))) {
                ballYSpeed = -ballYSpeed;
            }
            for (int i = 0; i < brickLayout.bricks.length; i++) {
                for (int j = 0; j < brickLayout.bricks[0].length; j++) {
                    // I added the part to check or(is it -1) to check if it is an obstacle ---
                    // thisizaro
                    if (brickLayout.bricks[i][j] > 0) {
                        int brickX = j * brickLayout.brickWidth + 80;
                        int brickY = i * brickLayout.brickHeight + 50;
                        int brickW = brickLayout.brickWidth;
                        int brickH = brickLayout.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickW, brickH);
                        Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            if (brickLayout.bricks[i][j] != -1) { // Not an obstacle
                                brickLayout.hitBrick(i, j);
                                if (brickLayout.bricks[i][j] == 0) {
                                    bricksRemaining--;
                                    currentScore += 5;
                                }
                            }

                            if (ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width) {
                                ballXSpeed = -ballXSpeed;
                            } else {
                                ballYSpeed = -ballYSpeed;
                            }
                            ballX += ballXSpeed;
                            ballY += ballYSpeed;
                        }
                    }
                }
            }
            ballX += ballXSpeed;
            ballY += ballYSpeed;
            if (ballX < 0 || ballX > win_width - ball_size - 10) {
                ballXSpeed = -ballXSpeed;
            }
            if (ballY < 0) {
                ballYSpeed = -ballYSpeed;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddleX >= win_width - paddle_width) {
                paddleX = win_width - paddle_width;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddleX < 10) {
                paddleX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isPlaying) {
                isPlaying = true;
                ballX = win_width / 2;
                ballY = win_height - 150;
                ballXSpeed = -1;
                ballYSpeed = -2;
                paddleX = win_width / 2 - paddle_width / 2;
                currentScore = 0;
                bricksRemaining = brick_rows * brick_cols;
                brickLayout = new BrickManager(brick_rows, brick_cols);
            }
        }
    }

    public void moveRight() {
        isPlaying = true;
        paddleX += 20;
    }

    public void moveLeft() {
        isPlaying = true;
        paddleX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
