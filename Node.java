public class Node {
    
    //position of the node on the grid 
    private int x;
    private int y;

    private int type;

    //the node from which the cell was explored
    private Node parent;

    //distance from starting node
    private int gCost;
    //distance from goal node
    private int hCost;
    //sum of g-cost and h-cost
    private int fCost;

    public Node(int x, int y, int type){

        this.x    = x;
        this.y    = y;
        this.type = type;
        gCost = Integer.MAX_VALUE;
        hCost = Integer.MAX_VALUE;
        fCost = Integer.MAX_VALUE;
        this.parent = null;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    } 

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type; 
    }

    public int getGCost(){
        return gCost;
    }

    public void setGCost(int cost){
        this.gCost = cost;
    }

    public int getHCost(){
        return hCost;
    }
 
    public void setHCost(int cost){
        this.hCost = cost;
    }

    public int getFCost(){
        return fCost;
    }
 
    public void setFCost(int cost){
        this.fCost = cost;
    }

    public Node getParent(){
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }
}
 