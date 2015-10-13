/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author martijn
 */
public class KochManager implements Observer {
    KochFractal k;
    private JSF31KochFractalFX application;
    ArrayList<Edge> edges;
    TimeStamp timestamp;
    GenerateSide sideLeft;
    GenerateSide sideBottom;
    GenerateSide sideRight;
    
    public KochManager(JSF31KochFractalFX app){
        application = app;
        edges = new ArrayList();
    }
    
    @Override
    public synchronized void update(Observable o, Object arg){
        if (arg instanceof Edge){         
            edges.add((Edge)arg);
        }
        else{
            for (Edge e : (ArrayList<Edge>)arg)
            {
                edges.add(e);
            }
        }
    }
    
    public void changeLevel(int nxt){
        edges.clear();
        application.clearKochPanel();
        timestamp = new TimeStamp();
        timestamp.setBegin();
        
        sideLeft = new GenerateSide(nxt, Side.LEFT);
        sideLeft.addObserver(this);
        //sideLeft.run();
        sideBottom = new GenerateSide(nxt, Side.BOTTOM);
        sideBottom.addObserver(this);
        //sideBottom.run();
        sideRight = new GenerateSide(nxt, Side.RIGHT);
        sideRight.addObserver(this);
        //sideRight.run();
        
        Thread thLeft = new Thread(sideLeft);
        thLeft.start();
        Thread thBottom = new Thread(sideBottom);
        thBottom.start();
        Thread thRight = new Thread(sideRight);
        thRight.start();
        
        
        try {
            thLeft.join();
            thBottom.join();
            thRight.join();
        } catch (InterruptedException ex) {
           ex.fillInStackTrace();
        }
        
        timestamp.setEnd();
        application.setTextCalc(timestamp.toString());
        drawEdges();
    }
    public void drawEdges(){
        
        timestamp = new TimeStamp(); 
        timestamp.setBegin();
        
        for (Edge e : edges){

            application.drawEdge(e);
        }
        
        timestamp.setEnd();
        application.setTextDraw(timestamp.toString());
        application.setTextNrEdges(Integer.toString(k.getNrOfEdges()));
    }
    

}
