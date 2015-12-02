/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochfractalconsolewrite;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import javafx.scene.paint.Color;

/**
 *
 * @author martijn
 */
public class FileWriter implements Observer {

    ArrayList<Edge> edges = new ArrayList<Edge>();
    
    @Override
    public void update(Observable o, Object o1) {
        edges.add((Edge) o1);
    }
    
    
    
    public boolean saveEdges(int level) {
        edges = new ArrayList<Edge>();
        KochFractal k = new KochFractal();
        k.addObserver(this);
        k.setLevel(level);
        k.generateLeftEdge();
        k.generateBottomEdge();
        k.generateRightEdge();
        TimeStamp t = new TimeStamp();
        t.setBegin();
        boolean b = saveEdgesBinaryNoBuffer(); // change this method 
        t.setEnd();
        System.out.println(t.toString());
        loadEdges();
        return b;
    }
    
    
    public boolean saveEdgesBinaryNoBuffer(){
        try {
            FileOutputStream fos  = new FileOutputStream("test.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Edge e : edges) {
                oos.writeObject(e.X1);
                oos.writeObject(e.X2);
                oos.writeObject(e.Y1);
                oos.writeObject(e.Y2);
                oos.writeObject(e.color.getBlue());
                oos.writeObject(e.color.getGreen());
                oos.writeObject(e.color.getRed());
                System.out.println(e.X2 + e.Y1);
            }
            
            oos.close();
            fos.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean loadEdges(){
        try {
        FileInputStream fis = new FileInputStream("test.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                    try {
                    double x1 = (double)ois.readObject();
                    double x2 = (double)ois.readObject();
                    double y1 = (double)ois.readObject();
                    double y2 = (double)ois.readObject();
                    double blue = (double)ois.readObject();
                    double green = (double)ois.readObject();
                    double red = (double)ois.readObject();
                    Edge e = new Edge(x1,x2,y1,y2,Color.color(red, green, blue));
                    System.out.println(e.X2 + e.Y1);
                    } catch (EOFException ex) {
                    break;
                    }
            }
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
}
