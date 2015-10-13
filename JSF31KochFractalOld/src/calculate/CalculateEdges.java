/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Mick
 */
public class CalculateEdges implements Observer {
    
    private KochFractal fractal;
     private KochFractal fractal2;
      private KochFractal fractal3;
    private KochManager sender;
    
    public CalculateEdges(KochFractal f, KochManager sender){
        fractal = f;
        fractal2 = f;
        fractal3 = f;
        fractal.addObserver(this);
        fractal2.addObserver(this);
        fractal3.addObserver(this);
        this.sender = sender;
        
        String s = "hoi";
        if (s == "hoi".toString()){}
    }

    
    public void run() {
        
         Thread thLeftEdge = new Thread(new Runnable() {
         @Override
            public void run() {
                fractal.generateLeftEdge();
            }
        });
        Thread thBottomEdge = new Thread(new Runnable() {
         @Override
            public void run() {
                fractal2.generateBottomEdge();
            }
        });
        Thread thRightEdge = new Thread(new Runnable() {
         @Override
            public void run() {
                fractal3.generateRightEdge();
            }
        });
        
        thLeftEdge.start();
        thBottomEdge.start();
        thRightEdge.start();
        
        
        try {
            thLeftEdge.join();
            thBottomEdge.join();
            thRightEdge.join();
        } catch (InterruptedException ex) {
           ex.fillInStackTrace();
        }
        
        sender.Request();
    }

    
    @Override
    public synchronized void update(Observable o, Object arg) {
        sender.AddEdgeToArray((Edge)arg);
    }
    
}
