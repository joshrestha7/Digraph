package minbinheap;

public class MinBinHeap implements Heap_Interface {
	  private EntryPair[] array; //load this array
	  private int size;
	  private static final int arraySize = 10000; //Everything in the array will initially 
	                                              //be null. This is ok! Just build out 
	                                              //from array[1]
	  
	  public MinBinHeap() {
	    this.array = new EntryPair[arraySize];
	    array[0] = new EntryPair(null, -100000); //0th will be unused for simplicity 
	                                             //of child/parent computations...
	                                             //the book/animation page both do this.
	  }
	    
	@Override
	public void insert(EntryPair entry) {
		//Extend array size
		if (size == array.length - 1) {
            EntryPair[] cloneArr = new EntryPair[size];    
            cloneArr = array.clone();
            array = new EntryPair[(size * 2) + 1];
            array = cloneArr.clone();
		}
		
		int hole = size + 1;
		array[0] = entry;
		//While priority of insert < than priority of hole's parent
		while(entry.getPriority() < array[hole/2].getPriority()) { 
			array[hole] = array[hole/2]; //Bubble up
			hole = hole / 2; //Next parent
		}
		array[hole] = entry;
		size++;
	}

	@Override
	public void delMin() {
		if(size == 0) {
			return;
		}
		
		array[1] = array[size];
		bubbleDown(1);
		size--;
	}
	
	private void bubbleDown(int hole) {
		int child;
		EntryPair temp = array[hole];
		
		//While child is not null
		while(hole * 2 <= size) {
			child = hole * 2;
			
			//If right child has greater priority, switch to right child
			if(child != size && array[child + 1].getPriority() < array[child].getPriority())
				child++;
			
			//Bubble down
			if(array[child].getPriority() < temp.getPriority())
				array[hole] = array[child];
			else
				break;
			hole = child;
		}
		array[hole] = temp;
	}

	@Override
	public EntryPair getMin() {
		if(size == 0) {
			return null;
		}
		
		EntryPair min = array[1];
		
		if(size > 1)
		//Loops thru whole array
		for(int i = 2; i <= size(); i++) {
			if(array[i].getPriority() < min.getPriority())
				min = array[i];
		}
		
		return min;
	}

	@Override 
	public int size() {
		return size;
	}

	@Override
	public void build(EntryPair[] entries) {
		
		size = entries.length;
		array = new EntryPair[size + 10];
		
		int i = 1;
		for(EntryPair entry : entries)
			array[i++] = entry;
			
		for(int j = size() / 2; j > 0; j--)
			bubbleDown(j);
	}

	@Override
	public EntryPair[] getHeap() { 
		return this.array;
	}

}
