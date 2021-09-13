
import java.util.Iterator;

/**
 * Singly linked list of generic nodes.
 * 
 * @param <T> Object to score
 * @version 1.0
 * @author Sunny Jiao
 */
public class SingleLinkedList<T> implements Iterable<T> {

    private SingleLinkedNode<T> head;
    private int size;

    /**
     * Constructs an empty linked list.
     */
    public SingleLinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Adds an item to the end of the linked list.
     * 
     * @param item Item to add
     */
    public void add(T item) {
        if (head == null) {
            head = new SingleLinkedNode<T>(item, null);
        } else {
            SingleLinkedNode<T> tempNode = head;
            while (tempNode.getNext() != null) {
                tempNode = tempNode.getNext();
            }
            tempNode.setNext(new SingleLinkedNode<T>(item, null));
        }
        size++;
    }

    /**
     * Retrieves an item from the linked list.
     * 
     * @param item Item to retrieve
     * @return item, or null if not found
     */
    public T get(T item) {
        SingleLinkedNode<T> tempNode = head;
        if(head == null){
            return null;
        }
        while (!tempNode.getItem().equals(item)) {
            tempNode = tempNode.getNext();
            if(tempNode == null){
                return null;
            }
        }
        return tempNode.getItem();
    }

    /**
     * Retrives an item from the linked list by index.
     * 
     * @param index Index to retrieve from
     * @return The item at index, or null if not found
     */
    public T get(int index) {
        if (index == 0) {
            return head.getItem();
        }
        SingleLinkedNode<T> tempNode = head;
        for (int i = 1; i <= index; i++) {
            tempNode = tempNode.getNext();
        }
        return tempNode.getItem();
    }

    /**
     * Gets the index of a specified item.
     * 
     * @param item Item to find index of
     * @return The index of the item, or -1 if not found
     */
    public int indexOf(T item) {
        int index = 0;
        if(head == null){
            return -1;
        }
        SingleLinkedNode<T> tempNode = head;
        while (!tempNode.getItem().equals(item)) {
            tempNode = tempNode.getNext();
            index++;
            if (tempNode == null) {
                return -1;
            }
        }
        return index;
    }

    /**
     * Removes an item from the linked list.
     * 
     * @param item Item to remove
     * @return True if item was found and removed, False otherwise
     */
    public boolean remove(T item) {
        SingleLinkedNode<T> checkNode = head;
        SingleLinkedNode<T> tempNode = head;

        if (head == null) {
            return false;
        }
        if(head.getItem().equals(item)){
            head = head.getNext();
            return true;
        }
        while (!checkNode.getItem().equals(item)) {
            tempNode = checkNode;
            checkNode = checkNode.getNext();
            if(checkNode == null) {
                return false;
            }
        }
        tempNode.setNext(checkNode.getNext());
        size--;
        return true;
    }

    /**
     * Clears and resets the list to be empty.
     */
    public void clear() {
        size = 0;
        head = null;
    }

    /**
     * Gets the size of the list.
     * 
     * @return Number of elements in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Gets the head node of the list.
     * 
     * @return Head node of the linked list.
     */
    private SingleLinkedNode<T> getHead(){
        return head;
    }

    /**
     * Creates and returns an iterator of the list.
     */
    @Override
    public CustomIterator<T> iterator() {
        return new CustomIterator<T>(this);
    }

    /**
     * Custom Iterator class for SingleLinkedList
     * 
     * @version 1.0
     * @author Sunny Jiao
     */
    public static class CustomIterator<E> implements Iterator<E>, Cloneable {
        SingleLinkedNode<E> currentNode;

        /**
         * Initializes the iterator, with the head as the current element.
         * @param list List to iterate
         */
        public CustomIterator(SingleLinkedList<E> list){
            currentNode = list.getHead();
        }

        /**
         * Returns whether the list has an element in the next node.
         * @return True if there exists more nodes, false otherwise.
         */
        public boolean hasNext(){
            return currentNode != null;
        }

        /**
         * Gets the item at the next node.
         * @return Next item in the list
         */
        public E next(){
            E item = currentNode.getItem();
            currentNode = currentNode.getNext();
            return item;
        }
        
        /**
         * Clone method that overrides Object.clone(), allowing for
         * a shallow copy of the iterator.
         * 
         * @return Clone of the list iterator.
         */
        @SuppressWarnings("unchecked")
        @Override
        public CustomIterator<E> clone(){
            try{
                return (CustomIterator<E>)super.clone();
            } catch (CloneNotSupportedException e){
                // Won't run
                return null;
            }
        }
    }
}
