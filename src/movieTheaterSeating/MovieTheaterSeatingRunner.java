package movieTheaterSeating;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

/**
 * Main Program runner
 * 
 * 
 * @author Konstantin
 *
 */
public class MovieTheaterSeatingRunner {

	public static void main(String[] args) {			
		
		if(args.length != 0)
		{			
			String filePath = args[0];		
			//String filePath = "F:/Java_Projects/Movie-Theater-Seating/src/movieTheaterSeating/test.txt";
		
			// Display input path
			System.out.println("Input File: " + filePath);
			
			// Initialize reader
			BufferedReader reader;
			MovieTheater cinema = new MovieTheater(10, 20); //Create default movie theater 10 rows by 20 seats
			
			try {
				// Open file
				reader = new BufferedReader(new FileReader(filePath));
				
				// Read the first line
				String line = reader.readLine();				
				
				// Generate out file path
				String outPath = new String();
				
				for (int i = filePath.length()-1; i >= 0; i--)
				{
					if(filePath.charAt(i) == '/')
					{
						outPath = filePath.substring(0, i+1);
						break;
					}
				}
				
				
				StringBuilder str = new StringBuilder();
				str.append(outPath);
				str.append("ReservedTickets.txt");
				
				outPath = str.toString();
				
				// Create outfile and open it to start writing
				File outFile = new File(outPath);
				outFile.createNewFile();
				FileWriter fileWriter = new FileWriter(outPath);
				
				System.out.print("Processing the input.");
				
				// Read file line by line
				while(line != null)
				{				
					// Split into reservation number and number of people
					String[] inputArray = line.split(" ");			
					
					// Request tickets
					String reservedTickets = cinema.ticketRequest(Integer.parseInt(inputArray[1]), inputArray[0]);			
					
					fileWriter.write(reservedTickets);
					fileWriter.write("\n");
					// Read next line
					line = reader.readLine();
					System.out.print(".");
				}
				System.out.println("");
				
				// Done. Print the output file destination
				System.out.println("Output file saved to: " + outPath);
				
				// Close reader and writer
				fileWriter.close();
				reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Please specify the file path as an argument.");
		}
		
		
		System.out.println("Exited");
		
		
		
		
	}

}
