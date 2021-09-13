
/**
 * Generic singly linked node.
 * 
 * @param <T> Object to store
 * @version 1.0
 * @author Sunny Jiao
 */
public class SingleLinkedNode<T> {

    private T item;
    private SingleLinkedNode<T> next;

    /**
     * Initializes the node with an item and links the next node.
     * 
     * @param item Item to store in the node
     * @param next The next node that this node is linked to
     */
    public SingleLinkedNode(T item, SingleLinkedNode<T> next){
        this.item = item;
        this.next = next;   
    }

    /**
     * Gets the item stored in the node
     * 
     * @return The item
     */
    public T getItem(){
        return this.item;
    }

    /**
     * Gets the node that this node is linked to.
     * 
     * @return The next node
     */
    public SingleLinkedNode<T> getNext(){
        return this.next;
    }

    /**
     * Sets the node that this node is linked to.
     * 
     * @param next Node to set the next node to
     */
    public void setNext(SingleLinkedNode<T> next){
        this.next = next;
    }
}
