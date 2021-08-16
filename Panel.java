import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.time.chrono.ThaiBuddhistDate;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Panel extends JPanel implements MouseListener, MouseMotionListener, ActionListener{

    Panel panel = this;
    static final int SCREEN_WIDTH = 1425;
    static final int SCREEN_HEIGHT = 775; //change to 775 or 750
    static final int HEADER_HEIGHT = 50; //change to 175 or 150
    static final int UNIT_SIZE = 25;      //change to 25  or 75 
    public Grid grid;

    public  Node pressedNode;
    public int prevNodeType = grid.EMPTY;
    private Node prevNode;
    A_Search aSearch;
    private boolean debug = false;

    ColorLibrary colorLibrary = new ColorLibrary();

    ActionListener gameTimer = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent theEvent){
            if (aSearch != null && !aSearch.pathIsFound){
                timer.stop();
                timer.start();
                aSearch.exploreNodes();
                aSearch.evaulateNodes();  
            }
            else if (aSearch != null && aSearch.pathIsFound){
                timer.stop();
                timer.start();
                aSearch.fillPathList2();
            }
            repaint();
        }
    };

    Timer timer = new Timer(10, gameTimer);

    JLabel noPathLabel = new JLabel("");

    JComboBox mazesComBox;

    Panel(){
        timer.start();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true); //?

        addMouseListener(this);
        addMouseMotionListener(this);

        grid = new Grid(this);
        //for moving the start and end node
        int startNodePos[] = grid.getGridPos(grid.getStartNode());
        prevNode = new Node(startNodePos[0], startNodePos[1], 0);

        JButton startButton = new JButton("Start");
        startButton.setBounds(25, 25, 95, 30);  //fix the cordinates 
        this.add(startButton);
        startButton.addActionListener(this);
    
        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(115, 25, 95, 30);
        this.add(restartButton);
        restartButton.addActionListener(this);

        JButton clearWallsButton = new JButton("Clear Walls");
        this.add(clearWallsButton);
        clearWallsButton.addActionListener(this);

        JButton clearPathButton = new JButton("Clear Path");
        this.add(clearPathButton);
        clearPathButton.addActionListener(this);

        String[] mazes = {"Mazes", "Random Maze", "Deap Search Maze"};
        mazesComBox = new JComboBox(mazes);
        mazesComBox.addActionListener(this);
        this.add(mazesComBox);

        this.add(noPathLabel);
        noPathLabel.setFont(new Font("Serif", Font.BOLD, 25));
        noPathLabel.setForeground(colorLibrary.endColor);
    }

    //is called every 15 milli seconds 
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g); 
   
        if (aSearch != null && aSearch.noPath){
            //showNoPath();
            noPathLabel.setText("no path");
        }


        if (aSearch != null){
            drawExploredNodes(g);
            drawEvaluatedNodes(g);
        }

        draw(g); 


        if (aSearch != null && aSearch.pathIsFound){
            drawPath(g);
        }
    }


    public void draw(Graphics g)
    {
        drawGrid(g);
        Graphics2D g2D = (Graphics2D) g;

        //draws nodes based on its type
        for (int x = 0; x < Grid.xNodes; x++){
            for (int y = 0; y < Grid.yNodes; y++){
                Node node = grid.getNode(x, y);
                if (node == null){
                    System.out.println("x: " + x + " " + y);
                }
                int type = node.getType();

                if (type == Grid.BORDER){
                    g.setColor(colorLibrary.borderColor);
                    g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
                }
                else if (type == Grid.START){
                    g.setColor(colorLibrary.startColor);
                    g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
                }
                else if (type == Grid.END){
                    g.setColor(colorLibrary.endColor);
                    g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
                }  

                if (debug){
                    showCosts(node);
                }
            }
        }
    }

    public void showCosts(Node node){
        if (node.getFCost() != Integer.MAX_VALUE && node.getType() != Grid.START ){
            JLabel label1 = new JLabel("" + node.getGCost());
            label1.setBounds(node.getX() + 10, node.getY() + 20, 60, 10);
            this.add(label1);

            JLabel label2 = new JLabel("" + node.getHCost());
            label2.setBounds(node.getX() + 55, node.getY() + 20, 60, 10);
            this.add(label2);

            JLabel label3 = new JLabel("" + node.getFCost());
            label3.setBounds(node.getX() + 30, node.getY() + 50, 60, 10);
            this.add(label3);
        }
    }

    public void drawGrid(Graphics g){
        g.setColor(colorLibrary.lineColor);
        //draws vertical lines
        for(int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++){

            g.drawLine(i * UNIT_SIZE, HEADER_HEIGHT, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        //draws horizontal lines
        for (int i = 0; i <= (SCREEN_HEIGHT - HEADER_HEIGHT) / UNIT_SIZE; i++){
            g.drawLine(0, i * UNIT_SIZE + HEADER_HEIGHT, SCREEN_WIDTH, i * UNIT_SIZE + HEADER_HEIGHT);
        }
    }

    public void drawExploredNodes(Graphics g){
        if (aSearch != null){
            for (Node node : aSearch.getOpenList()){
                g.setColor(colorLibrary.expColor);
                g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void drawEvaluatedNodes(Graphics g){
        if (aSearch != null){
            for (Node node: aSearch.getClosedList()){
                if (node != grid.getStartNode()){
                    g.setColor(colorLibrary.evColor);
                    g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
    }

    public void drawPath(Graphics g){
        if (aSearch != null){
            for (Node node : aSearch.pathList2){
                g.setColor(colorLibrary.pathColor);
                g.fillRect(node.getX(), node.getY(), UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void showNoPath(){
        JLabel noPathLabel = new JLabel("noPath");
        noPathLabel.setText("No Path is found ");
        this.add(noPathLabel);
    }

    @Override 
    public void actionPerformed(ActionEvent e){        
        if (e.getActionCommand().equals("Start")){
            noPathLabel.setText("");
            timer.start();
            grid.clearPath();
            aSearch = new A_Search(grid, panel);
        }
        else if (e.getActionCommand().equals("Restart")){
            timer.start();
            grid.clearGrid();
            grid = new Grid(this);
            aSearch = null;
            noPathLabel.setText("");

        }
        else if (e.getActionCommand().equals("Clear Walls")){
            grid.clearWalls();
        }
        else if(e.getActionCommand().equals("Clear Path")){
            aSearch = null;
        }
        else if (e.getSource() == mazesComBox){
            if (mazesComBox.getSelectedItem().equals("Random Maze")){
                grid.randomGrid();
                noPathLabel.setText("");
                timer.start();
                mazesComBox.setSelectedItem("Mazes");
            }
            else if (mazesComBox.getSelectedItem().equals("Deap Search Maze")){
                grid.clearGrid();
                aSearch = null;
                grid.fillGridWithBorders();
                grid.depMaze();
                noPathLabel.setText("");
                mazesComBox.setSelectedItem("Mazes");
            }
            else if (mazesComBox.getSelectedItem().equals("Recursive Maze")){
                grid.clearGrid();
                grid.addOuterBorders();
                grid.addInnerBorders(false, 0, 56, 0, 23,0);
                aSearch = null;
                noPathLabel.setText("");
                mazesComBox.setSelectedItem("Mazes");
            }
        }
        repaint();
    }

    //updates the node that was first pressed before dragging the mouse
    @Override
    public void mousePressed(MouseEvent e){
        pressedNode = getNode(e);

        if (pressedNode != null){
            int pressedNodeType = pressedNode.getType();
            //draws a border when we click on an empty node
            //erases a border when we click on a border
            if (pressedNodeType != Grid.START && pressedNodeType != Grid.END ){

                if(pressedNodeType == Grid.EMPTY){
                    pressedNode.setType(Grid.BORDER);

                }
                else{
                    pressedNode.setType(Grid.EMPTY);
                }

                repaint();
            }
        }
    } 

    //resets the first pressed node to null when the user stops dragging the mouse 
    @Override
    public void mouseReleased(MouseEvent e){ 
        pressedNode = null;
    }

    @Override
    public void mouseEntered(MouseEvent e){
        //Not used
     }
 
     @Override
     public void mouseExited(MouseEvent e){
         //Not used
     }
     
     @Override
     public void mouseClicked(MouseEvent e){
         //Not used 
    }

    @Override
    public void mouseDragged(MouseEvent e){

        Node node = getNode(e);
        //the first node pressed before dragging 
        int pressedNodeType = pressedNode.getType();
        int nodeType = node.getType();

        //first we check if the clickled node exists and if we are still on the same node 
        //if we are, then there's no point to draw anything 
        if (node != null && node != pressedNode){
            //moves the start node and the end node when the user clicks on them and drugs them somewhere 
            if (pressedNodeType == Grid.START || pressedNodeType == Grid.END){

                node.setType(pressedNodeType);

                if (pressedNodeType == Grid.START){
                    grid.setStartNode(node);
                }
                else{
                    grid.setEndNode(node);
                }

                pressedNode.setType(prevNodeType);
                pressedNode = node;
                prevNodeType = nodeType;

                pressedNode.setGCost(0);
            }
            //prevents the user to erase the start node and end node 
            else if (nodeType != Grid.START && nodeType != Grid.END)
            {   
                //erasing borders 

                if (pressedNodeType == Grid.BORDER){
                    if (nodeType == Grid.EMPTY){
                        node.setType(Grid.BORDER);
                    }
                    else{
                        node.setType(Grid.EMPTY);                        
                    }
                }
                //if the node's type is empty
                else{
                    if (nodeType == Grid.BORDER){
                        node.setType(Grid.EMPTY);
                    }
                    else{
                        node.setType(Grid.BORDER);
                    }
                }

                pressedNode = node;
            }
            
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
        //Not used 
    }
   
    //compares the mouse's and node's positions
    //If the positions match, then the node is returned; null otherwise
    public Node getNode(MouseEvent e)
    {
        int mouseX = e.getX();
        int mouseY = e.getY();

        //compares the mouse's position and the position of every single node. If the mouse and a cell have the same position within the unit size 
        //the the cell is returned 
        for (int x = 0; x < Grid.xNodes; x++){
            for (int y = 0; y < Grid.yNodes; y++){
                Node node = grid.getNode(x, y);
                
                if (mouseX >= node.getX() && mouseX < node.getX() + UNIT_SIZE && 
                    mouseY >= node.getY() && mouseY < node.getY() + UNIT_SIZE)
                    {
                        return node;
                    }
            }
        }
        return null;
    }
}


