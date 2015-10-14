/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author martijn
 */
public class KochManager implements Observer {
    KochFractal k;
    private JSF31KochFractalFX application;
    CopyOnWriteArrayList<Edge> edges;
    TimeStamp timestampCalc;
    TimeStamp timestampDraw;
    GenerateSide sideLeft;
    GenerateSide sideBottom;
    GenerateSide sideRight;
    ExecutorService pool;
    
    public KochManager(JSF31KochFractalFX app){
        application = app;
        k = new KochFractal();
        k.addObserver(this);
        edges = new CopyOnWriteArrayList();
        pool = Executors.newFixedThreadPool(4);
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
        timestampCalc = new TimeStamp();
        timestampCalc.setBegin();
        
        sideLeft = new GenerateSide(nxt, Side.LEFT);
        sideBottom = new GenerateSide(nxt, Side.BOTTOM);
        sideRight = new GenerateSide(nxt, Side.RIGHT);
        
        Future<ArrayList<Edge>> futEdgesLeft = pool.submit(sideLeft);
        Future<ArrayList<Edge>> futEdgesBottom = pool.submit(sideBottom);
        Future<ArrayList<Edge>> futEdgesRight = pool.submit(sideRight);
        
        Thread th = new Thread(new Runnable(){
            @Override public void run(){
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
                application.requestDrawEdges();
        } catch (ExecutionException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        });
        pool.execute(th);
                timestampCalc.setEnd();
                application.setTextCalc(timestampCalc.toString());
        
        
    }
    public void drawEdges(){
        
        application.clearKochPanel();
        timestampDraw = new TimeStamp(); 
        timestampDraw.setBegin();
        
        for (Edge e : edges){

            application.drawEdge(e);
        }
        
        timestampDraw.setEnd();
        application.setTextDraw(timestampDraw.toString());
        application.setTextNrEdges(Integer.toString(k.getNrOfEdges()));
    }
    

}
