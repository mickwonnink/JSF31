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

/**
 *
 * @author martijn
 */
public class GenerateSide implements Callable,Observer{
    
    Side side;
    KochFractal koch;
    ArrayList<Edge> edges;
    
    public GenerateSide(int level, Side s){
        koch = new KochFractal();
        koch.setLevel(level);
        koch.addObserver(this);
        side = s;
        edges = new ArrayList<Edge>();
    }
    /*
    public void run(){
        
    }
    */
    @Override
    public void update(Observable o, Object arg){
        edges.add((Edge)arg);
    }

    @Override
    public Object call() throws Exception {
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
        return edges;
    }
}
