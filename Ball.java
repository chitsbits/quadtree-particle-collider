
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

/**
 * A class representing a ball in the simulation.
 * 
 * @version 1.0
 * @author Sunny Jiao
 */
@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {

    private static final int MIN_RADIUS = 8;
    private static final int MAX_RADIUS = 15;
    public int radius;
    public int mass;
    public double vx, vy;
    public Color color;

    /**
     * Creates a ball and assigns it various random attributes.
     */
    public Ball(){
        super();
        this.vx = Math.random() * 1 - 0.5;
        this.vy = Math.random() * 1 - 0.5;
        this.radius = (int)(Math.random() * (MAX_RADIUS - MIN_RADIUS + 1) + MIN_RADIUS);
        this.x = (int)(Math.random() * ((1024 - this.radius) - (this.radius) + 1) + this.radius);
        this.y = (int)(Math.random() * ((1024 - this.radius) - (this.radius) + 1) + this.radius);
        this.width = radius * 2;
        this.height = radius * 2;
        this.mass = radius;
        this.color = new Color((int)(Math.random() * 255),
                                (int)(Math.random() * 255),
                                (int)(Math.random() * 255));
    }
    
    /**
     * Draws the ball to a graphics object.
     * 
     * @param g Graphics object to draw to
     */
    public void draw(Graphics g){
        g.setColor(color);
        g.fillOval((int)x - radius, (int)y - radius, radius * 2, radius * 2);
    }

    /**
     * Updates the attributes of the ball.
     */
    public void update(){
        this.x += vx;
        this.y += vy;
    }
}
