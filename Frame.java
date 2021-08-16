import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Frame extends JFrame implements ActionListener{
    
    JMenuBar menuBar;
    JMenu mazesMenu;
    JMenu startMenu;
    JMenuItem rndmMazeItem;
    Panel panel;

    Frame(){
        panel = new Panel();

        this.add(panel);
        this.setTitle("Pathfinding Visualizer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        //this.setJMenuBar(menuBar);

        mazesMenu = new JMenu("Mazes");
        startMenu = new JMenu("Start");

        menuBar.add(startMenu);
        menuBar.add(mazesMenu);
        

        rndmMazeItem = new JMenuItem("Random maze");

        mazesMenu.add(rndmMazeItem);

        this.setVisible(true);

        rndmMazeItem.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == rndmMazeItem){
            panel.grid.randomGrid();
        }
        else if (e.getSource() == startMenu){
            panel.timer.start();
            panel.grid.clearPath();
            panel.aSearch = new A_Search(panel.grid, panel);
        }
    }
}
