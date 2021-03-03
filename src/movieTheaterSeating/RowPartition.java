package movieTheaterSeating;

/**
 * This class is used to record data about the partition of available seats in the row
 * 
 * 
 * @author Konstantin
 *
 */
public class RowPartition {

	int startIndex;
	int size;
	
	/**
	 * 
	 * @param startIndex : int that represents the starting index of the current partition
	 * @param size : int that represents the size of the current partition
	 */
	RowPartition(int startIndex, int size){		
		this.startIndex = startIndex;
		this.size = size;
	}
	
	/**
	 * 
	 * @return starting index of the current partition
	 */
	public int getStartIndex()
	{
		return this.startIndex;
	}
	
	/**
	 * 
	 * @return size of the current partition
	 */
	public int getSize()
	{
		return this.size;
	}
	
}
