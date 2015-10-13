/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        k = new KochFractal();
        k.addObserver(this);
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
        sideBottom = new GenerateSide(nxt, Side.BOTTOM);
        sideRight = new GenerateSide(nxt, Side.RIGHT);
        
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Future<ArrayList<Edge>> futEdgesLeft = pool.submit(sideLeft);
        Future<ArrayList<Edge>> futEdgesBottom = pool.submit(sideBottom);
        Future<ArrayList<Edge>> futEdgesRight = pool.submit(sideRight);
        pool.shutdown();
        
        try {
            for (Edge e : (ArrayList<Edge>)futEdgesLeft.get())
            {
                edges.add(e);
            }
            for (Edge e : (ArrayList<Edge>)futEdgesBottom.get())
            {
                edges.add(e);
            }
            for (Edge e : (ArrayList<Edge>)futEdgesRight.get())
            {
                edges.add(e);
            }
        } catch (ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
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
