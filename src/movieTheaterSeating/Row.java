package movieTheaterSeating;

import java.util.ArrayList;

/**
 * 
 * This class is used to record data about the row and all the partitions within the row
 * 
 * @author Konstantin
 *
 */
public class Row {
	
	private int currentMaxPartitionSize; // the size of the biggest partition
	private int totalAvailableSeats; // total number of available seats
	private ArrayList<RowPartition> partitions; // list of all partitions
	
	/**
	 * Parameterized Constructor
	 * 
	 * @param size
	 */
	Row(int size)
	{		
		this.currentMaxPartitionSize = size;
		this.totalAvailableSeats = size;
		this.partitions = new ArrayList<RowPartition>();
		RowPartition startingPartition = new RowPartition(0, size);
		this.partitions.add(startingPartition);
	}
	
	//*****************ACCESSOR METHODS******************
	public ArrayList<RowPartition> getPartitions()
	{
		return this.partitions;
	}
	
	public int getCurrentMaxPartitionSize()
	{
		return currentMaxPartitionSize;
	}	
	
	public int getTotalAvailableSeats()
	{
		return totalAvailableSeats;
	}
	
	
	//*****************MUTATOR METHODS******************
	
	public void setCurrentMaxPartitionSize(int size)
	{
		this.currentMaxPartitionSize = size;
	}
	
	public void setTotalAvailableSeats(int size)
	{
		this.totalAvailableSeats = size;
	}
}
