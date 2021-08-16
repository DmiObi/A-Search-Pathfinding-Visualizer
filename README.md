# Pathfinding Visualizer

Welcome to Pathfinding Visualizer! I finished this project when I was in my Senior year of High School to improve my programming skills and 
learn more about the Java language. I hope you like it! 


# About the project 

The overall purpose of the program is to find the shortest possible path between two points. The user may choose the positions for the starting point and ending point on the grid as well as build walls or choose a maze that was generated randomly or by an algorythm. In return, the program will display the shortest possible path on the screen. 
The “A-Search” algorithm is used in this program. The algorithm starts calculating the cost of traveling from the nodes that surround the starting node. 
Then, the algorithm chooses the node that has the lowest traveling cost, and again calculates the traveling costs for the nodes that surround that node. 
Every node has a reference to the node from where it was explored. The algorithm stops exploring nodes, once one of the nodes finds the ending node. 
After that, the algorithm traverses through the node’s references, displaying the path on the screen. 

# Project demonstration
https://user-images.githubusercontent.com/89030598/129605637-89c4a722-c03a-4954-a79a-8d749fa7d3f2.mov


