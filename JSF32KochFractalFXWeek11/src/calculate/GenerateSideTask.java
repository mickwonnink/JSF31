/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;

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
    private KochManager manager;
    
    public GenerateSideTask(int level, Side s, KochManager man){
        koch = new KochFractal();
        koch.setLevel(level);
        koch.addObserver(this);
        side = s;
        edges = new ArrayList<Edge>();
        maxEdges = (int)Math.pow(4, level-1);
        manager = man;
    }
    
    public void update(Observable o, Object arg){
        Edge e = (Edge)arg;
        manager.edges.add(e);
        edges.add(e);
        updateProgress(edges.size(), maxEdges);
        updateMessage("Nr Edges: " + edges.size());
        
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                manager.drawWhiteEdge(e);
            }
        });
        
        try{
            switch(side){
                case LEFT : sleep(1);
                case BOTTOM: sleep(2);
                case RIGHT : sleep(4);
            }
        }
        catch(Exception ex){
            Thread.currentThread().interrupt();
        }
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
        
        manager.doneCalculating();
        return null;
    }
    
    @Override
    protected void cancelled() {
        super.cancelled();
        koch.cancel();
    }
}
