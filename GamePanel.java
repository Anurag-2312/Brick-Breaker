package brickBreaker;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private boolean isPlaying = false;
    private int currentScore = 0;
    private int bricksRemaining = 6 * 10;
    private Timer gameTimer;
    private int speed = 8;
    private int paddleX = 400;
    private int ballX = 220;
    private int ballY = 450;
    private int ballXSpeed = -1;
    private int ballYSpeed = -2;
    private BrickManager brickLayout;

    public GamePanel() {
        brickLayout = new BrickManager(6, 10);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setPreferredSize(new Dimension(900, 700));
        gameTimer = new Timer(speed, this);
        gameTimer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 900, 700);

        // Draw bricks
        brickLayout.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 700);
        g.fillRect(0, 0, 900, 3);
        g.fillRect(899, 0, 3, 700);

        // Score Display
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: ", 600, 30);
        g.drawString("" + currentScore, 700, 30);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(paddleX, 600, 120, 10);

        // Ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, 20, 20);

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
            outerLoop:
            for (int i = 0; i < brickLayout.bricks.length; i++) {
                for (int j = 0; j < brickLayout.bricks[0].length; j++) {
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
                        }
                    }
                }
            }
            ballX += ballXSpeed;
            ballY += ballYSpeed;
            if (ballX < 0 || ballX > 870) {
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
            if (paddleX >= 800) {
                paddleX = 800;
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
                ballX = 220;
                ballY = 450;
                ballXSpeed = -1;
                ballYSpeed = -2;
                paddleX = 400;
                currentScore = 0;
                bricksRemaining = 6 * 10;
                brickLayout = new BrickManager(3, 7);
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
