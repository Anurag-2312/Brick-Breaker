// BrickManager.java
package brickBreaker;

import java.awt.*;

public class BrickManager {

    public int bricks[][];
    public int brickWidth;
    public int brickHeight;

    public BrickManager(int rows, int cols) {
        bricks = new int[rows][cols];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                double rand = Math.random();
                // for testing puroposes, I will set the value of the bricks to 999
                // bricks[i][j] = 2;
                // I will comment the code below to test the obstacles (debugging purposes)
                // --- thisizaro

                if (rand < 0.2) {
                    bricks[i][j] = 999; // 20% chance of being an obstacle
                } else if (rand < 0.5) {
                    bricks[i][j] = 2; // 30% chance of being a strong brick
                } else {
                    bricks[i][j] = 1; // Normal brick
                }
            }
        }
        brickWidth = 700 / cols;
        brickHeight = 200 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                // The value of the brick helps with last block of this loop for the printing
                // the brick strength (3 lines) --- thisizaro

                int brickValue = bricks[i][j];
                int x = j * brickWidth + 80; // Calculate X position
                int y = i * brickHeight + 50; // Calculate Y position

                if (bricks[i][j] > 0 && bricks[i][j] <= 2) {
                    if (bricks[i][j] == 2) {
                        g.setColor(Color.RED); // Strong bricks are red
                    } else {
                        g.setColor(Color.WHITE); // Normal bricks are white
                    }
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                } else if (bricks[i][j] > 2) {
                    // THIS IS THE PART FOR THE OBSTACLE... NOT DECIDED IF I SHOULD KEEP THE VALUE
                    // -1 OR CHNAGE IT TO 9999 --- thisizaro

                    g.setColor(Color.GRAY); // Obstacles are gray
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }

                // Draw brick strength (except obstacles) debugging purposes
                // (IfBlock)---thisizaro
                if (brickValue > 0) {
                    g.setColor(Color.BLACK); // Text color
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    // Center the text
                    String text = String.valueOf(brickValue);
                    FontMetrics fm = g.getFontMetrics();
                    int textX = x + (brickWidth - fm.stringWidth(text)) / 2;
                    int textY = y + (brickHeight + fm.getAscent()) / 2 - 2;
                    g.drawString(text, textX, textY);
                }
            }
        }
    }

    public void hitBrick(int row, int col) {
        if (row >= 0 && row < bricks.length && col >= 0 && col < bricks[0].length) {
            if (bricks[row][col] > 1) {
                bricks[row][col]--; // Reduce strength if it’s a strong brick
            } else if (bricks[row][col] == 1) {
                bricks[row][col] = 0; // Destroy the normal brick
            } // obstacles are indestructible
        }
    }
}
