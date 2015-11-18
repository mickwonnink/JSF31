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
    CopyOnWriteArrayList<Edge> edges;/// synchronized arraylist
    TimeStamp timestampCalc;
    TimeStamp timestampDraw;
    GenerateSideTask sideLeft;
    GenerateSideTask sideBottom;
    GenerateSideTask sideRight;
    ExecutorService pool; /// only one threadpool
    
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
        
        sideLeft = new GenerateSideTask(nxt, Side.LEFT);
        sideBottom = new GenerateSideTask(nxt, Side.BOTTOM);
        sideRight = new GenerateSideTask(nxt, Side.RIGHT);
        
        application.pbLeft.progressProperty().unbind();
        application.pbLeft.setProgress(0);
        application.pbLeft.progressProperty().bind(sideLeft.progressProperty());
        application.labelLeftEdges.textProperty().bind(sideLeft.messageProperty());
        application.pbBottom.progressProperty().unbind();
        application.pbBottom.setProgress(0);
        application.pbBottom.progressProperty().bind(sideBottom.progressProperty());
        application.labelBottomEdges.textProperty().bind(sideBottom.messageProperty());        
        application.pbRight.progressProperty().unbind();
        application.pbRight.setProgress(0);
        application.pbRight.progressProperty().bind(sideRight.progressProperty());
        application.labelRightEdges.textProperty().bind(sideRight.messageProperty());
        
        
        
        pool.execute(sideLeft);
        pool.execute(sideBottom);
        pool.execute(sideRight);
        
            
            Thread th = new Thread(new Runnable(){/// not letting UI wait for the calculations
                @Override public void run(){
                    try {
                        sleep(250);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    application.requestDrawEdges();
                }
            });
            pool.execute(th);
            
        
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
