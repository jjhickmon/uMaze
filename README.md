# uMaze

## Description
uMaze is a simple program to generate and solve mazes.

## Instructions
To run the program, simply compile and execute uMaze.java
I just used AWT and Swing, so you should not need to download anything.
Upon execution you will be presented with a screen that looks like Image 1 shown below.
Simply press the button that you want to use, and the program will do the rest!


**Caveats of the program:**
You must generate a maze before pressing solve because the default grid is unsolvable.
You must also reset the grid after solving a maze

## Screenshots
Image 1
![Screenshot 1](https://github.com/cs-olympic/finalcs2-jjhickmon/blob/main/screenshots/Screenshot1.png?raw=true)
Image 2
![Screenshot 2](https://github.com/cs-olympic/finalcs2-jjhickmon/blob/main/screenshots/Screenshot2.png?raw=true)
Image 3
![Screenshot 3](https://github.com/cs-olympic/finalcs2-jjhickmon/blob/main/screenshots/Screenshot3.png?raw=true)
Image 4
![Screenshot 4](https://github.com/cs-olympic/finalcs2-jjhickmon/blob/main/screenshots/Screenshot4.png?raw=true)

## Software Design
The program uses a randomized depth-first search algorthm in order to generate the maze.
A description of the algorithm is given in Citation 2. Depth-first search traverses a tree by going through
each branch until it reaches a dead end, then backtracking. Randomized depth-first search has a similar process.
It traverses a tree by going through a random branch, then continues to choose random branches until it reaches a dead end.
It then backtracks but never goes to a branch it has already visited. In my situation, I was using a grid but it has the
same principle. Since I was creating a random maze, I needed to use the randomized depth-first search algorithm so the
generator would completely random. After that, I just created walls based on where the algorithm went and I had a maze.
This algorithm only creates "perfect mazes" or mazes with one solution.


I used dijkstra's algorithm to solve the generated maze. It took me a little while to understand how to implement the algorithm,
but I eventually found how to do so.
To implement dijkstra's algorithm I had to create nodes, but I first had to understand what a node was.
Citation 8 best described how I ended up creating nodes. I created to rules that I check a cell against in order to
create a node at that location. 
- Rule 1: if two or more adjacent walls are active, then create a node
- Rule 2: if less than two walls are active then create a node


These two rules describe all dead ends, split pathways, and corners. Once I created nodes, I set neighbors and weights.
A cell's neighbor is on the same row or column, and is not obstructed by a wall or another node.
A edge's weight is the distance from one node to its neighbor.
After creating nodes, then I implemented dijkstra's algorithm. 
Citation 9 proved to be most useful when learning how the algorithm worked, because they explained
every single step in detail. This is the section of my project that I am most proud of. The code
is very clean yet clever, and it works flawlessly.
After I implemented dijkstra's algorithm, I was very suprised that it was so fast. It took only 0.005 seconds to
solve the maze on my laptop.


I decided to not use any JUnit testing, because most of my methods changed the state of the program.
If I were to use JUnit testing, then I would have to use it to drive the program, which is unneccessary.


I am very proud of this project over all. Even though I could have cleaned up certain parts by using recursion or
made the program faster by using hashing, I discovered many new topics and learned how to implement them.
My code is also fairly cohesive and minimizes coupling. I hope to possibly expand on this in the future
once I learn more about CS.

## Challenges and Issues
My first major challenge was learning how to draw to the screen. I could have used the provided demo files, but I did not want
the user to have to download anything. I also just wanted to learn how do draw to the screen, so I ended up finding some videos on YouTube
covering the topic.


My second major challenge was implementing the randomized depth-first search algorithm. This problem was difficult until
I realized that each cell needed a reference to its neighbors. After that, the implementation part was very smooth.


My last and most difficult challenge was implementing dijkstra's algorithm. The hardest part was just understanding how it
worked and what data structures I needed. The rest actually went by very fast.

## Citations
1. https://docs.oracle.com/javase/8/docs/api/index.html
2. https://en.wikipedia.org/wiki/Maze_generation_algorithm
3. https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
4. https://www.youtube.com/watch?v=5o3fMLPY7qY
5. https://www.youtube.com/watch?v=Stv1EmzDKnk&t=321s
6. https://www.youtube.com/watch?v=IkDeUW1yFLk
7. https://www.youtube.com/watch?v=D8UgRyRnvXU
8. https://www.youtube.com/watch?v=rop0W4QDOUI
9. https://www.youtube.com/watch?v=FSm1zybd0Tk&t=716s
