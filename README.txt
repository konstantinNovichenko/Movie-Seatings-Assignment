Assumptions:

- Customer Satisfaction and Customer Safety is priority
- Safety buffer example (3 seats to the sides and at least 1 row to both sides): 
xxxxxxxxx
xxxsssxxx
xxxxxxxxx

sssxxxsss
xxxsssxxx
sssxxxsss

- Customers prefer to sit in the middle of the theater and to sit all in the same row (no splitting)
- If they have to split - each subgroup would like to seat at the best seats possible with everybody in their subgroup
in the same row (no splitting, could be far away from the other subgroup). Also I assume that when customer party splits,
they prefer to have as large subgroups as possible (divide by 2)
- Input Assumptions - Input is preprocessed: 
	- Always pairs ticket/num of people
	- Number of people is an integer greater than 0

Tradeoffs:
- Not maximized revenue
- If groups split, the subgroups could sit far away from each other, but at the best seats possible



Building the solution:
- Go to "\Movie-Theater-Seating\src\movieTheaterSeating"
- In terminal run "javac *.java" to build
- Go back to the previous directory by running "cd .."
- Run the program "java movieTheaterSeating.MovieTheaterSeatingRunner <PATH-TO-FILE/filename.txt>"
- See the output to locate the output file
- Run the testing unit by running "java movieTheaterSeating.MovieTheaterTestingUnit"

