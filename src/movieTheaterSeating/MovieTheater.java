package movieTheaterSeating;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is created to store data about the seat availability of a movie theater of custom size
 * - Allows to assign the best seats for various group of guests based on the group size and seat score evaluation
 * 	 (seats closer to the middle of the theater considered to be better)
 * - Can print the map of the seat availability
 * 
 * 
 * @author Konstantin
 *
 */
public class MovieTheater {
	
	private int numRows;
	private int numSeats;
	private boolean[][] seats; // Matrix of available seats
	private ArrayList<Row> rows; // List of rows with partitions
	private SeatEvaluation currentBestSeat; // Used to keep track of the current best seats for the current party (or sub-party)
	private ArrayList<String> seatsStringList; // Used to keep track of all tickets sold for the entire party
	private ArrayList<PartySeats> currentPartyAssignment; // Used to keep track of the reserved seats for the party (each entry is sub-party)
	
	private int ticketCounter = 0; // keep track of all tickets sold
	
	
	
	//***************************CONTRUCTOR*********************************
	/**
	 * Parameterized Constructor
	 * 
	 * @param numRows : int that represent the number of rows in the movie theater
	 * @param rowSize : int that represent the number of seats in each row
	 */
	MovieTheater(int numRows, int rowSize)
	{		
		this.numRows = numRows;
		this.numSeats = rowSize;
		this.rows = new ArrayList<Row>();
		this.seatsStringList = new ArrayList<String>();
		this.seats = new boolean[numRows][];
		this.currentBestSeat = new SeatEvaluation(-1,-1);
		this.currentPartyAssignment = new ArrayList<PartySeats>();
		
		// Populate the theater with available seats and partitions
		for(int i = 0; i < numRows; i++)
		{
			// Set initial Row partition
			Row newRow = new Row(rowSize);
			this.rows.add(newRow);
			
			this.seats[i] = new boolean[rowSize];
			// Set the availability for the current row 
			for(int j = 0; j < rowSize; j++)
			{
				this.seats[i][j] = true; // set the seat as available
			}
		}
	}
	
	
	//***************************PUBLIC METHODS*********************************
	
	/**
	 * Print the seating map (1 - available, 0 - unavailable)
	 */
	public void printMap()
	{
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numSeats; j++)
			{
				System.out.print((this.seats[i][j] ? 1 : 0) + " ");
			}
			System.out.println();
		}
	}	
	
	public void printTotalTickets()
	{
		System.out.println("Total Sold: " + ticketCounter);
	}
	
	/**
	 * Check the if tickets could be sold to the party
	 * 
	 * @param partySize
	 * @return
	 */
	public String ticketRequest(int partySize, String reservation)
	{
						
		if(!checkAvailability(partySize)) // no more space
		{
			// reset the reserved best seats for the part of the group
			this.seatsStringList.clear();
			this.currentBestSeat.setRowScore(2.0);
			this.currentBestSeat.setSeatScore(2.0);
			
			// Cancel the reserved seats
			for(int i = 0; i < this.currentPartyAssignment.size(); i++)
			{
				int row = this.currentPartyAssignment.get(i).row;
				int startIndex = this.currentPartyAssignment.get(i).startIndex;
				int size = this.currentPartyAssignment.get(i).size;
				cancelReservedSeats(row, startIndex, size);				
			}			
			this.currentPartyAssignment.clear();
			
			// Print the message
			//System.out.println("NOT ENOUGH TICKETS FOR THIS PARTY");
			
			return "";
		}
		
		StringBuilder str = new StringBuilder(); 
		str.append(reservation);
		str.append(" ");		
		
		// Send the assigned tickets to the output
		for(int i = 0; i < this.seatsStringList.size(); i++)
		{			
			str.append(this.seatsStringList.get(i));	
			if(i != this.seatsStringList.size() - 1)
			{
				str.append(",");
			}
			this.ticketCounter++;
		}
		
		//System.out.println("------Total Sold: " + ticketCounter);
		
		// Finalize the reservation
		seatGuests();
		
		//TODO ADD TO THE OUTPUT FILE
		
		// Print the theater map
		//printMap();
		
		// Clear the best seats buffer
		this.seatsStringList.clear();
		this.currentBestSeat.setRowScore(2.0);
		this.currentBestSeat.setSeatScore(2.0);
		this.currentPartyAssignment.clear();
		
		return str.toString();
	}
	
	/**
	 * Check if the movie theater can accommodate the entire party,
	 * If not, split the party in 2 and check again recursively
	 * Returns false is there is no way to seat all people in the party even by splitting one by one
	 * 
	 * @param partySize
	 * @return
	 */
	public boolean checkAvailability(int partySize)
	{		
		// Start looking for a ideal row starting from the middle
		// NOTE: Assuming that the middle rows and the middle seats are the best
		
		// check if the movie theater has even amount of rows
		int even = this.numRows % 2;
		
		if(even != 0) //if odd, check the middle row first
		{
			findTheBestSeatsInRow((this.numRows / 2), partySize);
		}
		
		// Check 2 rows at a time by starting from the middle (go from center to the sides)
		for(int i = 0; i < this.numRows / 2; i++)
		{
			if(this.rows.get((this.numRows / 2) - 1 - i).getCurrentMaxPartitionSize() >= partySize)
			{
				findTheBestSeatsInRow((this.numRows / 2) - 1 - i, partySize);
			}
			
			if(this.rows.get((this.numRows / 2) + even + i).getCurrentMaxPartitionSize() >= partySize)
			{
				findTheBestSeatsInRow((this.numRows / 2) + even + i, partySize);
			}
		}		
		
		// Entire party can be seated in the same row all together without splitting the with the best view
		if(this.currentBestSeat.getOverallScore() != 2.0)
		{
			// Finalize the reservation			
			addSeatsToStringList(partySize);
			reserveSeats(this.currentBestSeat.getRow(), this.currentBestSeat.getSeatIndex(), partySize);
			
			// Reset the best seats
			this.currentBestSeat.setRowScore(2.0);
			this.currentBestSeat.setSeatScore(2.0);
		}
		else // Can't place the entire party all together without splitting
		{
			if(partySize == 1) // Can't place one person = no more place in the movie theater
			{				
				return false;
			}
			else // Split party in 2 and run this function recursively
			{
				int rightPartySize = partySize / 2;
				int leftPartySize = partySize - (partySize / 2);
				
				// Check the right side
				if(!checkAvailability(rightPartySize))
				{		
					// Reset the best seats
					this.currentBestSeat.setRowScore(2.0);
					this.currentBestSeat.setSeatScore(2.0);
					return false;
				}
				
				// Check the left side
				if(!checkAvailability(leftPartySize))
				{		
					// Reset the best seats
					this.currentBestSeat.setRowScore(2.0);
					this.currentBestSeat.setSeatScore(2.0);
					return false;
				}
			}
		}
		
		// Everybody was seated to the best seats possible
		return true;
		
	}	
	
	
	/**
	 * Find the starting index of the best seating position for the current party
	 * @param row
	 * @param partySize
	 * @return
	 */
	public void findTheBestSeatsInRow(int row, int partySize)
	{
		// Initialize temp values
		int startingIndex = 0;
		double bestScore = 1.0;		
		
		// Iterate through each partition
		for(int i = 0; i < this.rows.get(row).getPartitions().size(); i++)
		{
			int currentPartitionSize = this.rows.get(row).getPartitions().get(i).getSize();
			
			// Check if partition fits the entire party
			if( currentPartitionSize >= partySize)
			{	
				// Set the current start index to the beginning of the partition
				int currentStartIndex = this.rows.get(row).getPartitions().get(i).getStartIndex();
				
				// Look for the best position in the partition based on the score evaluation
				while(currentStartIndex + partySize <= 
						this.rows.get(row).getPartitions().get(i).getStartIndex() + currentPartitionSize)
				{
					// get the evaluation for the current position
					double currentPosScore = evaluateSeatPosition(currentStartIndex, partySize);
					
					if(currentPosScore < bestScore) // new best score found
					{
						bestScore = currentPosScore;
						startingIndex = currentStartIndex;
					}
					
					// go to the next position in partition
					currentStartIndex++;				
				}
			}
		}
		
		// All partitions checked. Create new seat evaluation object
		SeatEvaluation seatScore = new SeatEvaluation(row, startingIndex);
		seatScore.setSeatScore(bestScore);
		seatScore.setRowScore(evaluateRowPosition(row));	
		
		// Save the new best score if overall score (average of row and seat scores) is better than the previous best score
		if(this.currentBestSeat.getOverallScore() > seatScore.getOverallScore())
		{
			this.currentBestSeat = seatScore;
		}			
		
	}
	
	
	/**
	 * 
	 * @param row
	 * 
	 * @return score for the current row position for the party
	 */
	public double evaluateRowPosition(int row)
	{
		double evaluationScore = 0.0;
		double idealPosition = (double)this.numRows / 2.0;
		
		// Closer to 0 -> better
		evaluationScore = Math.abs(idealPosition - row) / idealPosition;
		
		return evaluationScore;
	}
	
	/**
	 * 
	 * @param startingPosition
	 * @param partySize
	 * 
	 * @return score for the current seat position for the party
	 */
	public double evaluateSeatPosition(int startingPosition, int partySize)
	{
		double evaluationScore = 0.0;
		double idealPosition = (double)this.numSeats / 2.0;
		
		// The closest party seats (middle point of the party seats) to the middle of the row - better
		//Formula:
		// T - best position in the middle of the row
		// S - size of the party divided by 2 (position in the middle of the party)
		// X - starting index of the party
		//
		// |T-(X+S)|/X
		// Closer to 0 -> better
		evaluationScore = Math.abs(idealPosition - (startingPosition + ((double)partySize / 2.0))) / idealPosition;
		
		
		return evaluationScore;
	}
	
	/**
	 * Reserve seats for the party of guest
	 */
	public void reserveSeats(int row, int startingIndex, int partySize)
	{			
		PartySeats party = new PartySeats(row, startingIndex, partySize);		
		this.currentPartyAssignment.add(party);
		for(int i = startingIndex; i < startingIndex + partySize; i++)
		{
			this.seats[row][i] = false;
		}
		
		updateRowPartition(row);	
	}
	
	/**
	 * Cancel reservation for the party of guest
	 */
	public void cancelReservedSeats(int row, int startingIndex, int partySize)
	{			
		for(int i = startingIndex; i < startingIndex + partySize; i++)
		{
			this.seats[row][i] = true;
		}
		
		updateRowPartition(row);	
	}	
	
	/**
	 * Finalize the reservation and create safety buffer
	 */
	public void seatGuests()
	{
		for(int i = 0; i < this.currentPartyAssignment.size(); i++)
		{
			int row = this.currentPartyAssignment.get(i).row;
			int startingIndex = this.currentPartyAssignment.get(i).startIndex;
			int partySize = this.currentPartyAssignment.get(i).size;
			
			// Update the row above if not out of bounds
			if(row > 0)
			{
				updateRow(row - 1, startingIndex, partySize);
			}		
			
			// Update the current row
			updateRow(row, startingIndex, partySize);
			
			// Update the row below if not out of bounds
			if(row != this.numRows - 1) 
			{
				updateRow(row + 1, startingIndex, partySize);
			}
		}
		
		
	}
	
	/**
	 * Set all the seats within a range as unavailable and update the partition
	 * 
	 * @param row
	 * @param partyStartIndex
	 * @param partySize
	 */
	public void updateRow(int row, int partyStartIndex, int partySize)
	{
		// Set the bounds
		int rightBound = partyStartIndex - 3;
		int leftBound = partyStartIndex + partySize + 3;
		
		// Prevent right out of bound 
		if(rightBound < 0)
		{
			rightBound = 0;
		}
		
		// Prevent left out of bound 
		if(leftBound > this.numSeats)
		{
			leftBound = this.numSeats;
		}
		
		// Set the seats within the range as unavailable
		for(int i = rightBound; i < leftBound; i++)
		{
			this.seats[row][i] = false;
		}		
		
		// Update the partition
		updateRowPartition(row);			
	}
	
	/**
	 * Update the partition of the row
	 * 
	 * @param row
	 */
	public void updateRowPartition(int row)
	{
		// Reset partitions, max partition size, and total available seats
		rows.get(row).getPartitions().clear();
		rows.get(row).setCurrentMaxPartitionSize(0);
		rows.get(row).setTotalAvailableSeats(0);
		
		// Initialize temp values
		int partitionStartIndex = -1;
		int partitionSize = 0;
		int maxPartitionSize = 0;
		int totalAvailableSeats = 0;
		
		// Reset partitions
		for(int i = 0; i < this.numSeats; i++)
		{
			
			if(this.seats[row][i] == true && partitionSize == 0) // Partition started
			{
				partitionStartIndex = i;
				partitionSize++;
			}			
			else if((this.seats[row][i] == false && partitionSize != 0) ||
					(this.seats[row][i] == true && partitionSize != 0 && i + 1 == this.numSeats)) // Partition ends
			{
				// Create new partition and push it to the list of partitions in the row
				RowPartition newPartition = new RowPartition(partitionStartIndex, partitionSize);
				rows.get(row).getPartitions().add(newPartition);
				totalAvailableSeats += partitionSize;
				
				// Set the max partition size if the current partition is the biggest in the row
				if(partitionSize > maxPartitionSize)
				{
					maxPartitionSize = partitionSize;
					rows.get(row).setCurrentMaxPartitionSize(maxPartitionSize);
				}
				
				// reset the partition size
				partitionSize = 0;
			}		
			else if(this.seats[row][i] == true && partitionSize != 0) // Partition continues
			{
				partitionSize++;
			}
			
		}
		
		// Update the total number of available seats
		rows.get(row).setTotalAvailableSeats(totalAvailableSeats);				
	}
	
	/**
	 * Convert the best party seats in string format seat by seat and save in the List of string seats
	 * @param partySize
	 */
	public void addSeatsToStringList(int partySize)
	{
		int rowCharValue = 'A' + this.currentBestSeat.getRow();
		char  rowChar = (char) rowCharValue;
		for(int i = this.currentBestSeat.getSeatIndex() + 1; i <= this.currentBestSeat.getSeatIndex() + partySize; i++)
		{
			StringBuilder str = new StringBuilder();
			str.append(rowChar);
			str.append(i);
			this.seatsStringList.add(str.toString());			
		}
		
	}
	

}
