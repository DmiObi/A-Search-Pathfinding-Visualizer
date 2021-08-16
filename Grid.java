import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Grid {
    
    private Node[][] gridArray;

    //types of a node
    static final int EMPTY = 0;
    static final int BORDER  = 1;
    static final int START = 2;
    static final int END  = 3;

    private Node startNode;
    private Node endNode;

    //number of nodes on the horizontal axis 
    static final int xNodes = Panel.SCREEN_WIDTH / Panel.UNIT_SIZE;
    //number of nodes on the vertical axis 
    static final int yNodes = (Panel.SCREEN_HEIGHT - Panel.HEADER_HEIGHT) / Panel.UNIT_SIZE;

    Panel p;

    Grid(Panel p ){
        
        gridArray = new Node[xNodes][yNodes];
        //startNode = new Node(0, Panel.HEADER_HEIGHT, Grid.START); //fix it
        startNode = new Node(Panel.UNIT_SIZE * 8, Panel.UNIT_SIZE * 14 + Panel.HEADER_HEIGHT, Grid.START); 

        startNode.setGCost(0);
        //endNode   = new Node(Panel.UNIT_SIZE, Panel.HEADER_HEIGHT, Grid.END);  //fix it 
        endNode = new Node(Panel.UNIT_SIZE * 47, Panel.UNIT_SIZE * 14 + Panel.HEADER_HEIGHT, Grid.END);

        createGrid();

        this.p = p;
    }

    //is called only once 
    public void createGrid()
    {
        gridArray[8][14] = startNode; //fix it 
        gridArray[47][14] = endNode;   //fix it 

        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y] != startNode && gridArray[x][y] != endNode){
                    gridArray[x][y] = new Node(x * Panel.UNIT_SIZE, y * Panel.UNIT_SIZE + Panel.HEADER_HEIGHT, EMPTY);
                }
            }
        }
    }

    public Node[][] getGridArray(){
        return gridArray;
    }

    //return node with given cordinates
    //if there's no such point, then null is returned 
    public Node getNode(int x, int y){

        if (x >= xNodes || x < 0 || y >= yNodes || y < 0){
            return null;
        }
        else{
            return gridArray[x][y];
        }
    }
    
    public Node getStartNode(){
        return startNode;
    }

    public void setStartNode(Node node){
        startNode = node;
    }

    public Node getEndNode(){
        return endNode;
    }

    public void setEndNode(Node node){
        endNode = node;
    }
    
    //returns an array with nodes' x and y position in the grid 
    //array[0] = x
    //array[1] = y
    public int[] getGridPos(Node node){
        int pos[] = new int[2];

        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y] == node){
                    pos[0] = x;
                    pos[1] = y;
                    return pos;
                }
            }
        }

        return pos;
    }

    //node 2 is start node 
    public void switchGridPos(Node node1, Node node2){
        int pos1[] = getGridPos(node1);
        int pos2[] = getGridPos(node2);       
      
        gridArray[pos1[0]][pos1[1]] = node2;
        gridArray[pos2[0]][pos2[1]] = node1;
    }

    public void setNodesPos(Node node, int x, int y){
        gridArray[x][y] = node;
    }

    //return node's neighbors 
    public ArrayList<Node> getNodesNbrs(Node node){
        ArrayList<Node> nbrs = new ArrayList<Node>();
        int pos[] = getGridPos(node);
        int x = pos[0];
        int y = pos[1];

        //looking around for neighbors 

        if (getNode(x + 1, y) != null){
            nbrs.add(getNode(x + 1, y));
        }

        if (getNode(x - 1, y) != null){
            nbrs.add(getNode(x - 1, y));
        }

        if (getNode(x, y - 1) != null){
            nbrs.add(getNode(x, y - 1));
        } 

        if (getNode(x, y + 1) != null){
            nbrs.add(getNode(x, y + 1));
        }

        return nbrs;
    }

    public boolean surroundedByBorders(Node node){
        ArrayList<Node> nbrs = getNodesNbrs(node);

        for (Node nbr : nbrs){
            if (nbr.getType() != BORDER){
                return false;
            }
        }

        return true; 
    }

    //prints out all information about every single node 
    public void gridInfo(){
        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                System.out.println("x: " + gridArray[x][y].getX() + 
                                  " y: " + gridArray[x][y].getY() + 
                                  " t: " + gridArray[x][y].getType()
                );
            }
        }
    }

    //orints all information about a node 
    public void getNodeInfo(Node node){
        int pos[] = getGridPos(node);
        System.out.println("x: " + pos[0] + " y: " + pos[1] + " type: " + node.getType() + " g-Cost: " + node.getGCost() + " h-Cost " + node.getHCost() + " f-Cost: " + node.getFCost());
    }   
    
    public void clearGrid(){
        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y] != startNode && gridArray[x][y] != endNode){
                    gridArray[x][y] = new Node(x * Panel.UNIT_SIZE, y * Panel.UNIT_SIZE + Panel.HEADER_HEIGHT, EMPTY);
                }
            }
        }
    }

    public void clearPath(){
        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y].getType() == EMPTY){
                    gridArray[x][y] = new Node(x * Panel.UNIT_SIZE, y * Panel.UNIT_SIZE + Panel.HEADER_HEIGHT, EMPTY);
                }
            }
        }
    }

    public void clearWalls(){
        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y].getType() == BORDER){
                    gridArray[x][y].setType(EMPTY);
                }
            }
        }
    }

    Random rand = new Random();

    public void randomGrid(){
        p.timer.start();

        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y] != startNode && gridArray[x][y] != endNode){
                    int randNum = rand.nextInt(3);

                    if (randNum == 0){
                        gridArray[x][y] = new Node(x * Panel.UNIT_SIZE, y * Panel.UNIT_SIZE + Panel.HEADER_HEIGHT, BORDER);    
                    }
                    else{
                        gridArray[x][y] = new Node(x * Panel.UNIT_SIZE, y * Panel.UNIT_SIZE + Panel.HEADER_HEIGHT, EMPTY);
                    }
                }
            }
        }
    }
 
    public void addOuterBorders(){
        //top && bottom
        for (int i = 0; i < xNodes; i++){
            gridArray[i][0].setType(BORDER);
            gridArray[i][yNodes - 1].setType(BORDER);
        }
        //left and right
        for (int i = 0; i < yNodes; i++){
            gridArray[0][i].setType(BORDER);
            gridArray[xNodes - 1][i].setType(BORDER);
        }
    }

    //indexes
    public void addInnerBorders(boolean hor, int minX, int maxX, int minY, int maxY, int n){
        if (hor){
            //creates a gab between borders 
            if (maxX - minX < 2){
                return;
            }
            //y is where we place the border; it's always even 
            int y = (int)(getRandNum(minY, maxY) / 2) * 2;
            addHorBorders(minX, maxX, y);
            addInnerBorders(!hor, minX, maxX, minY, y - 1, ++n);
            addInnerBorders(!hor, minX, maxX, y + 1, maxY, n);    
        }
        else{
            //creates a gab between borders 
            if (maxY - minY < 2) {
                return;
            }
            //y is where we place the border; it's always even 
            int x = (int)(getRandNum(minX, maxX) / 2) * 2;
            addVerBorders(minY, maxY, x);
            addInnerBorders(!hor, minX, x - 1, minY, maxY, ++n);
            addInnerBorders(!hor, x + 1, maxX, minY, maxY, ++n);
   
        }
    }

    //creates a horizontal border with a hole in it 
    public void addHorBorders(int minX, int maxX, int y){
        int xHole = (int)(getRandNum(minX, maxX) / 2) * 2 + 1;
        for (int i = minX; i <= maxX; i++){
            if (gridArray[i][y] != startNode && gridArray[i][y] != endNode){
                if (i == xHole){
                    gridArray[i][y].setType(EMPTY);
                }
                else{
                    gridArray[i][y].setType(BORDER);
                }
            }
        }
    }

    //creates a vertical border with a hole in it 
    public void addVerBorders(int minY, int maxY, int x){
        int yHole = (int)(getRandNum(minY, maxY) / 2) * 2 + 1;
        for (int i = minY; i <= maxY; i++){
            if (gridArray[x][i] != startNode && gridArray[x][i] != endNode && i != yHole){
                if (i == yHole){
                    gridArray[x][i].setType(EMPTY);
                }else{
                    gridArray[x][i].setType(BORDER);
                }
            }
        }
    }

    public void fillGridWithBorders(){
        for (int x = 0; x < xNodes; x++){
            for (int y = 0; y < yNodes; y++){
                if (gridArray[x][y] != startNode && gridArray[x][y] != endNode){
                    gridArray[x][y].setType(BORDER);
                }
            }
        }
    }

    Stack<Node> expNodesStack = new Stack<Node>();

    //creates a real looking maze 
    public void depMaze(){
        Node curNode = gridArray[getRandNum(0, 56)][getRandNum(0, 24)];

        if (curNode == startNode || curNode == endNode){
            curNode = gridArray[getRandNum(0, 56)][getRandNum(0, 24)];
        }

        expNodesStack.push(curNode);

        startNode.setType(BORDER);
        endNode.setType(BORDER);
        while (!expNodesStack.empty()){
            Node lastNode = expNodesStack.pop();
            lastNode.setType(EMPTY);
            explore(lastNode);
        }

        startNode.setType(START);
        endNode.setType(END);

        if (surroundedByBorders(startNode) || surroundedByBorders(endNode)){
            fillGridWithBorders();
            depMaze();
        }
    }

    //helper method of depMaze()
    public void explore(Node node){
        ArrayList<Node> expList = new ArrayList<Node>();
        int pos[] = getGridPos(node);
        int x = pos[0];
        int y = pos[1];
        boolean changed = false;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 4; i++){
            list.add(i);
        }


        while (!list.isEmpty()){
            Integer randNum = getRandNum(1, 4);

            if (randNum == 1 && list.contains(randNum)){
                
                if (x + 2 <= xNodes - 1 && gridArray[x + 2][y].getType() == BORDER && !expNodesStack.contains(gridArray[x + 2][y])){
                    expNodesStack.push(gridArray[x + 2][y]);
                    if (gridArray[x + 1][y] != startNode && gridArray[x + 1][y] != endNode){
                        gridArray[x + 1][y].setType(EMPTY);
                    }
                    changed = true;
                }
                
                list.remove(randNum);
            }
    
            if (randNum == 2 && list.contains(randNum)){

                if (x - 2 >= 0 && gridArray[x - 2][y].getType() == BORDER && !expNodesStack.contains(gridArray[x - 2][y])){
                    expNodesStack.push(gridArray[x - 2][y]);
                    if (gridArray[x - 1][y] != startNode && gridArray[x - 1][y] != endNode){
                        gridArray[x - 1][y].setType(EMPTY);
                    }
                    changed = true;
                }

                list.remove(randNum);
            }
    
            if (randNum == 3 && list.contains(randNum)){

                if (y + 2 <= yNodes - 1 && gridArray[x][y + 2].getType() == BORDER && !expNodesStack.contains(gridArray[x][y + 2])){
                    expNodesStack.push(gridArray[x][y + 2]);
                    if (gridArray[x][y + 1] != startNode && gridArray[x][y + 1] != endNode){
                        gridArray[x][y + 1].setType(EMPTY);
                    }
                    changed = true;
                }
        
                list.remove(randNum);
            }
    
            if (randNum == 4 && list.contains(randNum)){

                if (y - 2 >= 0 && gridArray[x][y - 2].getType() == BORDER && !expNodesStack.contains(gridArray[x][y - 2])){
                    expNodesStack.push(gridArray[x][y - 2]);
                    if (gridArray[x][y - 1] != startNode && gridArray[x][y - 1] != endNode){
                        gridArray[x][y - 1].setType(EMPTY);
                    }
                    changed = true;
                }
                
                list.remove(randNum);
            }
        }

        if (!changed)
        {
            node.setType(EMPTY);
        }
    }

    public int getRandNum(int min, int max){
        Random random = new Random();
        return (int)(Math.random() * (max - min + 1) + min);
    }
}
