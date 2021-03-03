package movieTheaterSeating;

/**
 * This class is used to record data about the placement score evaluation of the seats for the current party (or sub-party)
 * 
 * @author Konstantin
 *
 */
public class SeatEvaluation {
	
	private int rowIndex;
	private int seatStartingIndex;
	private double rowScore;
	private double seatScore;	
	
	/**
	 * Parameterized Constructor
	 * 
	 * @param rowI
	 * @param seatI
	 */
	SeatEvaluation(int rowI, int seatI)
	{
		this.rowIndex = rowI;
		this.seatStartingIndex = seatI;
		this.rowScore = 2.0;
		this.seatScore = 2.0;		
	}
	
	
	//*****************ACCESSOR METHODS******************
	public int getRow()
	{
		return this.rowIndex;
	}
	
	public int getSeatIndex()
	{
		return this.seatStartingIndex;
	}
	
	public double getSeatScore()
	{
		return this.seatScore;		
	}
	
	public double getRowScore()
	{
		return this.rowScore;	
	}
	
	public double getOverallScore()
	{
		return (this.seatScore + this.rowScore) / 2.0;
	}
	
	//*****************MUTATOR METHODS******************
	public void setSeatScore(double seatS)
	{
		this.seatScore = seatS;
	}
	
	public void setRowScore(double rowS)
	{
		this.rowScore = rowS;
	}
	
	
}
