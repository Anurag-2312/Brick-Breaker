// MainClass.java

package brickBreaker;

import javax.swing.JFrame;

public class MainClass {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Brick Breaker Game");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
    }
}
