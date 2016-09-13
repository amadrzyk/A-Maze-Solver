/**
 * LinkedPriorityQueue represents a linked implementation of a priority queue.
 * The highest priority comes first, and then subsequent priorities are lower and lower.
 * This class implements the PriorityQueueADT interface.
 *
 * @author alexmadrzyk
 * @version 1.0.0
 */

public class LinkedPriorityQueue<T> implements PriorityQueueADT<T>
{
    private int count;
    private PriorityNode<T> front, rear; 
    
    /** 
     * Creates an empty queue.
     */
    public LinkedPriorityQueue()
    {
       count = 0;
       front = rear = null;
    }
    
    /**
     * Adds the specified element to the rear of this queue.
     * @param element the element to be added to the rear of this queue
     */
    public void enqueue (T element)
    {
       PriorityNode<T> node = new PriorityNode<T>(element); 

       if (isEmpty())
          front = node;
       else
          rear.setNext(node);

       rear = node;
       count++;
    }
    
    /**
     * Adds the specified element to the priority queue in the proper location based on priority.
     * @param element  the element to be added to this queue
     * @param p  the priority of the element to be added to this queue
     */
    public void enqueue (T element, double p){
    	PriorityNode<T> newNode = new PriorityNode<T>(element, p);
    	
    	// IF QUEUE IS EMPTY
    	if (isEmpty()){
    		front = newNode;
        	rear = newNode;
    	} 
    	
    	// IF QUEUE IS ONLY ONE ITEM LONG
    	else if (count == 1) {
    		// Link to start if priority of newNode is greater
    		if (newNode.getPriority() > front.getPriority()){
    			newNode.setNext(front);
    			front = newNode;
    		}
    		// Link to end if priority of newNode is smaller
    		else {
    			front.setNext(newNode);
    			rear = newNode;
    		}
    		
    	// IF QUEUE IS MORE THAN ONE ITEM LONG
    	} else {
    		// Flag variables
    		boolean endOfQueue = false;
    		boolean foundSpotToInsert = false;
    		
    		// Starting at front of list using the front reference
    		PriorityNode<T> currentNode = front;
    		
    		// Traversing the queue if newNode's priority is smaller or equal than currentNode, and until it's larger than currentNode.getNext()
    		while((!endOfQueue) && (!foundSpotToInsert) && (newNode.getPriority() <= currentNode.getPriority())){
    			if((currentNode.getNext() != null) && (newNode.getPriority() <= currentNode.getNext().getPriority())){
    				currentNode = currentNode.getNext();
    			} 
    			// We can't keep going if we're at the end of the queue, so...
    			else if (currentNode.getNext() == null) {
    				endOfQueue = true;
    			}
    			else {
    				foundSpotToInsert = true;
    			}
    		}
    		
    		// INSERTION INTO LINKED QUEUE
    		if (newNode.getPriority() > front.getPriority()){
    			newNode.setNext(front);
    			front = newNode;
    		} else if (endOfQueue){
    			currentNode.setNext(newNode);
    			rear = newNode;
    		} else {
    			newNode.setNext(currentNode.getNext());
    			currentNode.setNext(newNode);
    		}
    	}
    	count++;
    }
    
    /**
     * Removes the element at the front of this queue and returns a
     * reference to it. Throws an EmptyCollectionException if the
     * queue is empty.
     * @return the element at the front of this queue
     * @throws EmptyCollectionException  if an empty collection exception occurs
     */
    public T dequeue() throws EmptyCollectionException
    {
       if (isEmpty())
          throw new EmptyCollectionException ("queue");

       T result = front.getElement();
       front = front.getNext();
       count--;

       if (isEmpty())
          rear = null;

       return result;
    }
    
    /**
     * Returns a reference to the element at the front of this queue.
     * The element is not removed from the queue.  Throws an
     * EmptyCollectionException if the queue is empty.  
     * @return a reference to the first element in this queue
     * @throws EmptyCollectionsException  if an empty collection exception occurs
     */
    public T first() throws EmptyCollectionException
    {
       if(count == 0){
     	  throw new EmptyCollectionException("Queue");
       }
      
       return (T) front;
    }    
    
    /**
     * Returns true if this queue is empty and false otherwise. 
     * @return  true if this queue is empty and false if otherwise
     */
    public boolean isEmpty()
    { 
 	   return count == 0;
    }
    
    /**
     * Returns the number of elements currently in this queue.
     * @return  the integer representation of the size of this queue
     */
    public int size()
    { 
 	   return count;
    }
    
    /**
     * Returns a string representation of this queue. 
     * @return  the string representation of this queue
     */
    public String toString()
    {
        String outString = "";
        PriorityNode<String> currentNode = (PriorityNode<String>) front;
       
        for(int i = 0; i < count; i++){
     	   outString += (String) currentNode.getElement() + "\n";
     	   currentNode.getNext();
        }
       
        return outString;
    }
}
