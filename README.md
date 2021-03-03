# Movie Seat Assignmetn Algorithm
## Assumptions

- Customer Satisfaction and Customer Safety is priority
- Safety buffer example (3 seats to the sides and at least 1 row to both sides): 
<br />xxxxxxxxx
<br />xxxsssxxx
<br />xxxxxxxxx

- Customers prefer to sit in the middle of the theater and to sit all in the same row (no splitting)
- If they have to split - each subgroup would like to sit at the best seats possible with everybody in their subgroup
in the same row (no splitting, could be far away from the other subgroup). Also, I assume that when customer party splits,
they prefer to have as large subgroups as possible (divide by 2)
- Input Assumptions - Input is preprocessed: 
	- Always pairs ticket/num of people
	- Number of people is an integer greater than 0

## Tradeoffs
- All the assumptions listed above causing algorithm not maximize the revenue, since it's focused on prioritizing customer satisfaction and safety
- If groups split, the subgroups could sit far away from each other, but at the best seats possible (best seats are priority)
- If we were to prioritize the revenue, we could simplify the algorithm by making it "Greedy Algorithm" and packing parties starting from the sides vs. assigning the best seats in the middle by using compilcated scoring system.


## Building the solution
### I suggest using Eclipse IDE for the easier use. If you want to run the program and the testing unit from the terminal, please see the instructions below:
*Note: If you want to try the testing unit, please specify the location to "test.txt" file in "MovieTheaterTestingUnit.java" file line 21. Template testing file is located in "\Movie-Theater-Seating\src\movieTheaterSeating\test.txt"*
- Go to "\Movie-Theater-Seating\src\movieTheaterSeating"
- In terminal run "javac *.java" to build
- Go back to the previous directory by running "cd .."
- Run the program "java movieTheaterSeating.MovieTheaterSeatingRunner <PATH-TO-FILE/filename.txt>"
- See the output to locate the output file
- Run the testing unit by running "java movieTheaterSeating.MovieTheaterTestingUnit"

