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
import javafx.scene.paint.Color;
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
    int counter;
    
    public KochManager(JSF31KochFractalFX app){
        application = app;
        k = new KochFractal();
        k.addObserver(this);
        edges = new CopyOnWriteArrayList();
        pool = Executors.newFixedThreadPool(3);
        counter = 0;
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
        if (sideLeft != null && sideRight != null && sideBottom != null)
        {
            sideLeft.cancel();
            sideRight.cancel();
            sideBottom.cancel();
        }
        edges.clear();
        application.clearKochPanel();
        timestampCalc = new TimeStamp();
        timestampCalc.setBegin();
        
        sideLeft = new GenerateSideTask(nxt, Side.LEFT, this);
        sideBottom = new GenerateSideTask(nxt, Side.BOTTOM, this);
        sideRight = new GenerateSideTask(nxt, Side.RIGHT, this);
        
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
        
        
    }
    
    public void drawWhiteEdge(Edge e){
        Color c = e.color;
        e.color = Color.WHITE;
        application.drawEdge(e);
        e.color = c;
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
        application.setTextNrEdges(Integer.toString(edges.size()));
    }
    
    public synchronized void doneCalculating(){
        if (++counter >= 3) {
            application.requestDrawEdges();
            counter = 0;
            timestampCalc.setEnd();
            Platform.runLater(new Runnable(){

                @Override
                public void run() {
                    application.setTextCalc(timestampCalc.toString());
                }
            });
        }
    }
    

}
