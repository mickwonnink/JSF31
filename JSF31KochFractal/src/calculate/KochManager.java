/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Mick
 */
public class KochManager implements Observer {
    
    private JSF31KochFractalFX application;
    KochFractal k;
    private ArrayList<Edge> edges;
    TimeStamp timestamp;
    int count = 0;
    
    public KochManager(JSF31KochFractalFX app){
        application = app;
        
        k = new KochFractal();
        timestamp = new TimeStamp();
        edges = new ArrayList();
        k.addObserver(this);
        
    }
    
    @Override
    public synchronized void update (Observable o, Object arg) {        
        //edges.add((Edge)arg);
    }
    
    public synchronized void AddEdgeToArray(Edge e){
        edges.add(e);
        count++;
    }
    
    
    public void Request(){
        application.requestDrawEdges();
    }
    
    public void changeLevel(int nxt) {
        timestamp = new TimeStamp();
        timestamp.setBegin();
        k.setLevel(nxt);
       
        edges.clear();       
        
        CalculateEdges calcEdges = new CalculateEdges(k, this);
        calcEdges.run();
        
        
        
                    //k.generateLeftEdge();
                    //k.generateBottomEdge();
                    //k.generateRightEdge();
        timestamp.setEnd();
        //application.setTextCalc(timestamp.toString());
        //drawEdges();
        
         application.setTextCalc(timestamp.toString());
         application.setTextNrEdges(Integer.toString(edges.size()));
}
    
    public void drawEdges() {
        application.clearKochPanel();        
        timestamp = new TimeStamp();
        timestamp.setBegin();
        
        for (Edge e : edges){

            application.drawEdge(e);
        }
        
        timestamp.setEnd();
        application.setTextDraw(timestamp.toString());
}



    
}
