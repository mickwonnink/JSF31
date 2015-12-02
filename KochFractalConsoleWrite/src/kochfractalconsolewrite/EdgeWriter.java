/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochfractalconsolewrite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Timer;
import javafx.scene.paint.Color;

/**
 *
 * @author martijn
 */
public class EdgeWriter implements Observer {

    ArrayList<Edge> edges = new ArrayList<Edge>();
    
    @Override
    public void update(Observable o, Object o1) {
        edges.add((Edge) o1);
    }
    
    
    
    public boolean saveEdges(int level, int method) {
        edges = new ArrayList<Edge>();
        KochFractal k = new KochFractal();
        k.addObserver(this);
        k.setLevel(level);
        k.generateLeftEdge();
        k.generateBottomEdge();
        k.generateRightEdge();
        TimeStamp t = new TimeStamp();
        t.setBegin();
        boolean b = false;
        switch (method) {
            case 1: b = saveEdgesBinaryNoBuffer();
            case 2: b = saveEdgesBinaryWithBuffer();
            case 3: b = saveEdgesTextNoBuffer();
            case 4: b = saveEdgesTextWithBuffer();
            default: b = false;
        }
        t.setEnd();
        System.out.println(t.toString());
        loadEdgesBinaryWithBuffer();
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
            }
            
            oos.close();
            fos.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean saveEdgesBinaryWithBuffer(){
        try {
            FileOutputStream fos  = new FileOutputStream("test.bin");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            for (Edge e : edges) {
                oos.writeObject(e.X1);
                oos.writeObject(e.X2);
                oos.writeObject(e.Y1);
                oos.writeObject(e.Y2);
                oos.writeObject(e.color.getBlue());
                oos.writeObject(e.color.getGreen());
                oos.writeObject(e.color.getRed());
            }
            
            oos.close();
            bos.close();
            fos.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean saveEdgesTextNoBuffer(){
        try {
            PrintWriter pw = new PrintWriter("test.txt");
            for (Edge e : edges) {
                pw.print(e.X1 + "|");
                pw.print(e.X2 + "|");
                pw.print(e.Y1 + "|");
                pw.print(e.Y2 + "|");
                pw.print(e.color.getBlue() + "|");
                pw.print(e.color.getGreen() + "|");
                pw.println(e.color.getRed());
            }
            pw.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean saveEdgesTextWithBuffer(){
        try {
            FileWriter fw = new FileWriter("test.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            for (Edge e : edges) {
                pw.print(e.X1 + "|");
                pw.print(e.X2 + "|");
                pw.print(e.Y1 + "|");
                pw.print(e.Y2 + "|");
                pw.print(e.color.getBlue() + "|");
                pw.print(e.color.getGreen() + "|");
                pw.println(e.color.getRed());
            }
            pw.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    
    public boolean loadEdgesBinaryNoBuffer(){
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
                    } catch (EOFException ex) {
                    break;
                    }
            }
            fis.close();
            ois.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean loadEdgesBinaryWithBuffer(){
        try {
        FileInputStream fis = new FileInputStream("test.bin");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
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
                    } catch (EOFException ex) {
                    break;
                    }
            }
            fis.close();
            bis.close();
            ois.close();
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    public boolean loadEdgesTextNoBuffer() {
        try {
            FileReader fr = new FileReader("test.txt");
            Scanner sc = new Scanner(fr);
            while(true) {
                try {
                    String line = sc.nextLine();
                    String[] edge = line.split("|");
                    double x1 = Double.parseDouble(edge[0]);
                    double x2 = Double.parseDouble(edge[1]);
                    double y1 = Double.parseDouble(edge[2]);
                    double y2 = Double.parseDouble(edge[3]);
                    double blue = Double.parseDouble(edge[4]);
                    double green = Double.parseDouble(edge[5]);
                    double red = Double.parseDouble(edge[6]);
                    Color c = new Color(red, green, blue, 1);
                    Edge e = new Edge(x1, y1, x2, y2, c);
                }
                catch (NoSuchElementException ex) {
                    break;
                }
            }
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public boolean loadEdgesTextWithBuffer(){
        try {
            FileReader fr = new FileReader("test.txt");
            BufferedReader br = new BufferedReader(fr);
            Scanner sc = new Scanner(br);
            while(true) {
                try {
                    String line = sc.nextLine();
                    String[] edge = line.split("|");
                    double x1 = Double.parseDouble(edge[0]);
                    double x2 = Double.parseDouble(edge[1]);
                    double y1 = Double.parseDouble(edge[2]);
                    double y2 = Double.parseDouble(edge[3]);
                    double blue = Double.parseDouble(edge[4]);
                    double green = Double.parseDouble(edge[5]);
                    double red = Double.parseDouble(edge[6]);
                    Color c = new Color(red, green, blue, 1);
                    Edge e = new Edge(x1, y1, x2, y2, c);
                }
                catch (NoSuchElementException ex) {
                    break;
                }
            }
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
