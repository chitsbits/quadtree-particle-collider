
//Graphics &GUI imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.Rectangle;

/**
 * Main display and game loop class
 * @version 1.0
 * @author Sunny Jiao
 */
@SuppressWarnings("serial")
class QuadTreeDisplay extends JFrame {

    static GameAreaPanel gamePanel;    
    SingleLinkedList<Ball> masterBallList;
    QuadTree root;
    FrameRate framerate;
    
    /**
     * Main method. Runs the simulation program.
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        new QuadTreeDisplay();
    }

    /**
     * Creates the panel for the QuadTree collision simulation.
     */
    public QuadTreeDisplay() {
        super("haha balls go boing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1042, 1065);
        gamePanel = new GameAreaPanel();
        this.add(new GameAreaPanel());
        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);
        this.requestFocusInWindow();
        this.setVisible(true);

        // Initialize simulation objects
        root = new QuadTree(new Rectangle(1024, 1024), 0);
        masterBallList = new SingleLinkedList<>();
        framerate = new FrameRate();

        // Start the game loop in a separate thread (yikes)
        Thread t = new Thread(new Runnable(){ public void run(){ animate();}}); 
        t.start();
    }

    /**
     * The main gameloop method.
     */
    public void animate() {
        while (true) {
            for(Ball ball : masterBallList){
                // Detect collision with walls
                if(ball.x - ball.radius <= 0){
                    ball.x = 0 + ball.radius;
                    ball.vx = -ball.vx;
                }  
                else if (ball.x + ball.radius >= 1024){
                    ball.x = 1024 - ball.radius;
                    ball.vx = -ball.vx;
                }
                else if(ball.y - ball.radius <= 0){
                    ball.y = 0 + ball.radius;
                    ball.vy = -ball.vy;
                } 
                else if(ball.y + ball.radius >= 1024){
                    ball.y = 1024 - ball.radius;
                    ball.vy = -ball.vy;
                }
                // Update balls
                ball.update();
            }

            // Clear and reinsert balls to "move" balls between boundaries
            root.clearBallList();
            for(Ball ball : masterBallList){
                root.addBall(ball);
            }

            // Update the QuadTree
            root.update();
           
            // Update the frame information
            framerate.update();

            try {Thread.sleep(1);} catch (Exception exc) {
                exc.printStackTrace();
            }
            this.repaint();
        }
    }

    /** --------- INNER CLASSES ------------- **/

    /**
     * Main panel of the program
     * 
     * @version 1.0
     * @author Sunny Jiao
     */
    private class GameAreaPanel extends JPanel {

        /**
         * Draw method. Draws all items to the panel.
         * 
         * @param g Graphics object to draw to
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setDoubleBuffered(true);
            
            /* Draw all balls
            * Line 129 (sometimes, it's inconsistent) causes a NullPointerException on start (that doesn't stop the program).
            * My Iterator implementation should be fine, so I'm really not sure what's causing this. (some thread issue?)
            */ 
            for(Ball b : masterBallList){
                b.draw(g);
            }

            // Draw QuadTree and framerate
            g.setColor(Color.BLUE);
            root.draw(g);
            framerate.draw(g, 10, 10);
        }
    }

    /**
     * Keylistener class used to get input to insert balls.
     * 
     * @version 1.0
     * @author Sunny Jiao
     */
    private class MyKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        
        public void keyPressed(KeyEvent e) {
            // "A" is used to add balls
            if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
                Ball add = new Ball();
                masterBallList.add(add);
            }
            // "ESC" to quit
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("Y I K E S  ESCAPE KEY!");
                System.exit(0);
            }
        }
    }
}
