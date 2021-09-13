
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * A QuadTree data structure that divides a reigion into quadrants
 * and subquadrants.
 * 
 * @version 1.0
 * @author Sunny Jiao
 */
public class QuadTree {

    public static final int MAX_DEPTH = 5;
    public static final int THRESHOLD = 5;
    private QuadTree[] children; // 0 - top left, 1 - top right, 2 - bot left, 3 - bot right
    private SingleLinkedList<Ball> ballList;
    private Rectangle boundingBox;
    private int depth;

    /**
     * Constructs a QuadTree.
     * 
     * @param boundingBox Rectangle with the bounds of the tree
     * @param depth How many layers deep from the root tree
     */
    public QuadTree(Rectangle boundingBox, int depth) {
        this.boundingBox = boundingBox;
        this.children = new QuadTree[4];
        this.ballList = new SingleLinkedList<Ball>();
        this.depth = depth;
    }

    /**
     * Updates the state of the QuadTree. Checks for various tasks: Collisions,
     * subdivisions, and collpasing of children. Also calls update on the children.
     */
    public void update() {
        if(isLeaf()) {
            if(ballList.size() > 1){
                checkForCollisions(); // Check for collisions (only leaves)
            }
            // Check if enough balls are in to subdivide -> only on leaves
            if(ballList.size() >= THRESHOLD && depth <= MAX_DEPTH) {
                subdivide();
            }
        }
        else{
            // Collapse children
            if(ballList.size() < THRESHOLD){
                collapseChildren();
            }
            else{
                // Update children
                for(int i = 0; i < children.length; i++) {
                    children[i].update();
                }
            }
        }
    }

    /**
     * Subdivides the tree to have 4 subtrees. Creates 4 children QuadTrees and adds
     * them to the array, then checks all the balls in the current QuadTree and adds
     * them to the subtree if it is within the area.
     */
    private void subdivide() {
        children[0] = new QuadTree(
                new Rectangle(boundingBox.x, boundingBox.y, boundingBox.width / 2, boundingBox.height / 2), depth + 1);
        children[1] = new QuadTree(new Rectangle(boundingBox.x + boundingBox.width / 2, boundingBox.y,
                boundingBox.width / 2, boundingBox.height / 2), depth + 1);
        children[2] = new QuadTree(new Rectangle(boundingBox.x, boundingBox.y + boundingBox.height / 2,
                boundingBox.width / 2, boundingBox.height / 2), depth + 1);
        children[3] = new QuadTree(new Rectangle(boundingBox.x + boundingBox.width / 2,
                boundingBox.y + boundingBox.height / 2, boundingBox.width / 2, boundingBox.height / 2), depth + 1);

        // Add balls to appropriate quadrant
        for(Ball ball : ballList) {
            addBallToChildren(ball);
        }
    }

    /**
     * Collapses the child trees.
     */
    private void collapseChildren() {
        for(int i = 0; i < children.length; i++) {
            children[i] = null;
        }
    }
    
    /**
     * Adds a ball to the bounding area of this QuadTree. If this layer has
     * children, the ball will also be added to the appropriate child.
     * 
     * @param ball Ball to add to the tree/subtrees
     */
    public void addBall(Ball ball) {
        ballList.add(ball);
        if(!isLeaf()) {
            addBallToChildren(ball);
        }
    }

    /**
     * Find the appropriate children to add a ball to. Balls that occupy a space
     * in between two boundaries will be added to both, so that cross-boundary
     * collisions can be made.
     * 
     * @param ball Ball to add to the children
     */
    private void addBallToChildren(Ball ball) {
        for (int i = 0; i < children.length; i++) {
            // Create an enlarged box to account for balls that occupy multiple children.
            Rectangle childBox = children[i].getBoundingBox();
            Rectangle enlargedBox = new Rectangle(childBox);
            enlargedBox.grow(ball.radius, ball.radius);
            if (enlargedBox.contains(ball.x, ball.y)) {
                children[i].addBall(ball);
            }
        }
    }

    /**
     * Clears the tree's and it's childrens' ball lists.
     */
    public void clearBallList(){
        if(!isLeaf()){
            for(QuadTree child : children){
                child.clearBallList();
            }
        }
        ballList.clear();
    }

    /**
     * Compares all combinations of balls in the list and checks for a collision.
     * Uses iterators rather than  the .get() function from SingleLinkedList, as
     * that must interally iterate to reach the element (and thus is only efficient
     * for single acceses).
     */
    private void checkForCollisions() {
        SingleLinkedList.CustomIterator<Ball> ballListIterator1 = ballList.iterator();
        Iterator<Ball> ballListIterator2 = ballList.iterator();
        Ball ball1, ball2;

        while(ballListIterator1.hasNext()) {
            ball1 = ballListIterator1.next();
            ballListIterator2 = ballListIterator1.clone();
            while(ballListIterator2.hasNext()) {
                ball2 = ballListIterator2.next();
                // Collide two balls if they touch
                if(Point2D.distance(ball1.x, ball1.y, ball2.x, ball2.y) < ball1.radius + ball2.radius) {
                    collide(ball1, ball2);
                }
            }
            ballListIterator2 = ballList.iterator();
        }
    }

    /**
     * Sets new velocites and positions for the collsion of two balls.
     * 
     * @param ball1 First ball in collision
     * @param ball2 Second ball in collision
     */
    private void collide(Ball ball1, Ball ball2) {
        double m1 = ball1.mass;
        double m2 = ball2.mass;

        // Distance between x & y
        double dx = ball2.x - ball1.x;
        double dy = ball2.y - ball1.y;

        // Angle of axis of collision
        double angle = Math.atan2(dy, dx);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Rotated positions on collision axis
        double x1 = 0, y1 = 0;
        double x2 = dx * cos + dy * sin;
        double y2 = dy * cos - dx * sin;

        // Rotated velocities on collision axis
        double vx1 = ball1.vx * cos + ball1.vy * sin;
        double vy1 = ball1.vy * cos - ball1.vx * sin;
        double vx2 = ball2.vx * cos + ball2.vy * sin;
        double vy2 = ball2.vy * cos - ball2.vx * sin;

        // Final velocities on 1D axis of collision (elastic collsion formula)
        double vx1f = ((m1 - m2) * vx1 + 2 * m2 * vx2) / (m1 + m2);
        double vx2f = ((m2 - m1) * vx2 + 2 * m1 * vx1) / (m1 + m2);
        vx1 = vx1f;
        vx2 = vx2f;

        // Move balls apart so that they don't overlap
        double vTotal = Math.abs(vx1) + Math.abs(vx2);
        double overlap = (ball1.radius + ball2.radius) - Math.abs(x1 - x2);
        x1 += vx1 / vTotal * overlap;
        x2 += vx2 / vTotal * overlap;

        // Rotate positions back
        double x1f = x1 * cos - y1 * sin;
        double y1f = y1 * cos + x1 * sin;
        double x2f = x2 * cos - y2 * sin;
        double y2f = y2 * cos + x2 * sin;

        // Set final position
        ball2.x = ball1.x + x2f;
        ball2.y = ball1.y + y2f;
        ball1.x = ball1.x + x1f;
        ball1.y = ball1.y + y1f;

        // Set velocites at original angles
        ball1.vx = vx1 * cos - vy1 * sin;
        ball1.vy = vy1 * cos + vx1 * sin;
        ball2.vx = vx2 * cos - vy2 * sin;
        ball2.vy = vy2 * cos + vx2 * sin;
    }

    /**
     * Get the bounding box of the tree's area
     * 
     * @return Bounding box of the tree's area
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Return whether the tree is a leaf (no children).
     * 
     * @return True if the QuadTree is a leaf (No children)
     */
    public boolean isLeaf() {
        return children[0] == null;
    }

    /**
     * Draws the bounding box of the area, and those of the children.
     * 
     * @param g Graphics object to draw to
     */
    public void draw(Graphics g) {
        g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        for (QuadTree child : children) {
            if (child != null) {
                child.draw(g);
            }
        }
    }
}
