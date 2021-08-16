import java.util.ArrayList;

public class A_Search {
    
    //the set of nodes to be evaluated 
    private ArrayList<Node> openList;
    //the set of nodes already evaluated
    private ArrayList<Node> closedList;
    
    private ArrayList<Node> pathList;

    private Grid grid; 

    Panel panel;

    boolean pathIsFound;

    boolean noPath = false;
    //it's a pathList that is used for drawing stuff
    ArrayList<Node> pathList2 = new ArrayList<Node>();

    Node pathNode;

    public A_Search(Grid grid, Panel panel){
        openList = new ArrayList<Node>();
        closedList = new ArrayList<Node>();
        pathList = new ArrayList<Node>();
        this.grid = grid;
        this.panel = panel;
        closedList.add(grid.getStartNode());
        pathIsFound = false;
    }

    public ArrayList<Node> getOpenList(){
        return openList;
    }

    public ArrayList<Node> getClosedList(){
        return closedList;
    } 
    
    //g-cost of the node is calculated by adding 1 to the closed node's g-cost
    public int calcGCost(Node clsdNode){

        return clsdNode.getGCost() + 1;
    }

    //h-cost is calculated by adding the differences in x and y from the end-node
    public int calcHCost(Node nbr){
        int pos[] = grid.getGridPos(nbr);
        int xNode = pos[0];
        int yNode = pos[1];

        int endPos[] = grid.getGridPos(grid.getEndNode());
        int xEndNode = endPos[0];
        int yEndNode = endPos[1];

        int xDif = Math.abs(xNode - xEndNode);
        int yDif = Math.abs(yNode - yEndNode);

        return xDif + yDif;
    }

    //sets/updates g,h,and f costs
    //returs true if the parent of the neihbor needs to be updated; returns false otherwise 
    public boolean updateCost(Node clsdNode, Node nbr, Node parent){
        int gCost = calcGCost(clsdNode);

        //if a node doesn't have a parent, it sets all costs 
        if (parent == null){
            int hCost = calcHCost(nbr);
            nbr.setGCost(gCost);
            nbr.setHCost(hCost);
            nbr.setFCost(gCost + hCost);
            return true;
        }
        //if new gCost is less than the old gCost, then g-cost is updated 
        else if (gCost < nbr.getGCost()){
            nbr.setGCost(gCost);
            return true;
        }
        else{
            return false;
        }
    }

    //loops through the closed list and adds its nodes' neigbors to the open-list if they are empty (not borders)
    //also update node's parent - node from where the cost was calculated 
    public void exploreNodes(){
        for (Node node : closedList){
            for (Node nbr : grid.getNodesNbrs(node)){
                if (nbr.getType() == Grid.EMPTY){
                    //checks if the node is alreay in the open-list
                    if (nbr.getParent() == null){
                        updateCost(node,nbr, null);
                        openList.add(nbr);
                        nbr.setParent(node);

                    }
                    else if(updateCost(node, nbr, nbr.getParent())){
                        nbr.setParent(node);
                    }
                }
                else if (nbr.getType() == Grid.END){
                    fillPathList(node);
                    pathNode = node;
                }
            }
        }
    }

    //loops through the open list and adds nodes with the lowest f-cost to the closed list 
    public void evaulateNodes(){
        int min = Integer.MAX_VALUE;
        //node with the lowest f-cost 
        Node closestNode = null;

        for (Node node : openList){
            if (node.getFCost() < min){
                min = node.getFCost();
                closestNode = node;
            }
        }

        if (closestNode != null){
            closedList.add(closestNode);
            openList.remove(closestNode);
        }
        else{
            noPath = true;
            panel.timer.stop();
        }
    }

    public void fillPathList(Node node){
        pathIsFound = true;

        Node copy = node;
        pathList.add(node);
        while (copy.getParent().getType() != grid.START){
            pathList.add(copy.getParent());
            copy = copy.getParent();
        }
    }

    //copies pathList in a new list but in a reverse order, so the displays from the starting point,
    //not the ending point
    public void fillPathList2(){
        if (pathList.size() != 0){
            pathList2.add(pathList.get(pathList.size() - 1));
            pathList.remove(pathList.size() - 1);
        }
    }

    public ArrayList<Node> getPathList(){
        return pathList;
    }
}
