package p2.DataStructures.SortedList;
/**
 * Implementation of a Sorted List using a Singly Linked List structure
 * 
 * @author Fernando J. Bermudez - bermed28
 * @author ADD YOUR NAME HERE - 802212115
 * @version 3.0
 * @since 03/28/2023
 * @param <E> 
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}
	/**
	 * Adds the specified element to this list in the corresponding position.
	 * 
	 * @param e element to be added to this list
	 */
	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the new value is the smallest */
		Node<E> newNode = new Node<>(e);
		Node<E> curNode;
		if (e == null) { throw new NullPointerException(); }

		if (this.isEmpty()) { 
			this.head = newNode; 
			currentSize++;
		}

		else if (head.getValue().compareTo(e) > 0) { 
			newNode.setNext(head); 
			head = newNode; 
			currentSize++;
		}

		else {
			curNode = head;
			while (curNode.getNext() != null && curNode.getNext().getValue().compareTo(e) < 0) { 
				curNode = curNode.getNext(); 
			}
			newNode.setNext(curNode.getNext());
			curNode.setNext(newNode);
			currentSize++;
		}
	}
	/**
	 * Removes the first occurrence of the specified element from this list, if it is present.
	 * If the list does not contain the element, it is unchanged.
	 * 
	 * @param e element to be removed from this list, if present
	 * @return boolean denoting whether the element was removed or not
	 */
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		if(e == null) { throw new NullPointerException(); }

		if(this.isEmpty()) { return false; }

		Node<E> rmNode = new Node<>(e);
		Node<E> curNode = this.head;
		Node<E> nextNode = curNode.getNext();

		if (head.getValue().compareTo(rmNode.getValue()) == 0) { 
			head = head.getNext(); 
			rmNode.clear();
			currentSize--;
			return true; 
		}

		while (nextNode != null && nextNode.getValue().compareTo(rmNode.getValue()) != 0) { 
			curNode = nextNode;
			nextNode = nextNode.getNext();
		}
		
		if(nextNode != null) { 
			curNode.setNext(nextNode.getNext());
			rmNode.clear();
			currentSize--;
			return true;
		}

		return false; 
		
	}
	/**
	 * Removes the element at the specified position in the list.
	 * 
	 * @param index the index of the element to be removed
	 * @return the element that was removed from the list
	 */
	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		if (index < 0 || index >= this.size())
			throw new IndexOutOfBoundsException();

		// E output = this.get(index);
		// this.remove(output);
		// return output;
		
		if(index == 0) { 
			Node<E> rmNode = this.head;
			this.head = this.head.getNext();
			E outputCase1 = rmNode.getValue();
			rmNode.clear();
			currentSize--;
			return outputCase1;
		}
		
		Node<E> rmNode = this.head.getNext();
		Node<E> prevNode = this.head;
		int iter = 1;
		while (iter != index){
			rmNode = rmNode.getNext();
			prevNode = prevNode.getNext();
			iter++;
		}
		E outputCase2 = rmNode.getValue();
		prevNode.setNext(rmNode.getNext());
		rmNode.clear();
		currentSize--;

		return outputCase2;
	}
	/**
	 * Returns the index of the first occurrence of the specified element in this list if it is found, otherwise
	 * returns -1 if it does not contain the element.
	 * 
	 * @param e element to search for
	 * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element
	 */
	@Override
	public int firstIndex(E e) {
		Node<E> currentNode = this.head;
		Node<E> target = new Node<>(e);
		int currIndex = 0;

		while(currentNode != null && currentNode.getValue().compareTo(target.getValue()) != 0) {
			currentNode = currentNode.getNext();
			currIndex++;
		}
		if (currentNode == null) { return -1; } 

		else { return currIndex; }
	}
	/**
	 * Returns the element (value inside the node) at the specified position in the list.
	 * 
	 * @param e element to search for
	 * @return the element at the specified position in the list
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		Node<E> currentNode = this.head;

		for (int i = 0; i != index; i++) {
			currentNode = currentNode.getNext();
		}

		return currentNode.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<E>[] toArray() {
		int index = 0;
		Comparable[] theArray = new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}