/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochfractalconsolewrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author martijn
 */
public class KochFractalConsoleWrite{

    static int level;
    static ArrayList<Edge> edges;
    static FileWriter f = new FileWriter();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter KochFractal level (only numbers, between 1 and 10): ");

        try {
            level = Integer.valueOf(reader.readLine());
            if (level < 1 || level > 10) throw new Exception("false input");
            System.out.println((f.saveEdges(level))? "The level is: " + level + ", and it was saved properly" : "An error occured while calculating or saving");
            
            }
        catch (NumberFormatException ex) {
            System.err.println("Wrong input, please only use numbers");
        }
        catch (Exception ex) {
            System.err.println("Wrong input, the number should be in between 1 and 10");
        }
    }
    
    
}
