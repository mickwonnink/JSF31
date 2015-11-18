/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author martijn
 */
public class GenerateSideTask extends Task<Void> implements Observer{
    
    Side side;
    KochFractal koch;
    ArrayList<Edge> edges;
    int maxEdges;
    int edge = 0;
    
    public GenerateSideTask(int level, Side s){
        koch = new KochFractal();
        koch.setLevel(level);
        koch.addObserver(this);
        side = s;
        edges = new ArrayList<Edge>();
        maxEdges = (int)Math.pow(4, level-1);
    }
    
    public void update(Observable o, Object arg){
        edges.add((Edge)arg);
        updateProgress(edges.size(), maxEdges);
        updateMessage("Nr Edges: " + edges.size());
    }

    @Override
    protected Void call() throws Exception {
        switch(side)
        {
            case LEFT: 
            {
                koch.generateLeftEdge();
                break;
            }
            case BOTTOM:
            {
                koch.generateBottomEdge();
                break;
            }
            case RIGHT:
            {
                koch.generateRightEdge();
                break;
            }
        }
        return null;
    }
}
