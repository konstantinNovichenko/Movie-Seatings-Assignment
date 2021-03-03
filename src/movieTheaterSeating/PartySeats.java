package movieTheaterSeating;

/**
 * This public class is used for keeping track of currently reserved seats 
 * @author Konstantin
 *
 */
public class PartySeats {
	public int row;
	public int startIndex;
	public int size;
	
	/**
	 * Parameterized Constructor
	 * 
	 * @param r
	 * @param sI
	 * @param s
	 */
	PartySeats(int r, int sI, int s){
		this.row = r;
		this.startIndex = sI;
		this.size = s;
	}
	
}
