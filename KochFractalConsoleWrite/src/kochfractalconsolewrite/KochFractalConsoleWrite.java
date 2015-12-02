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
    static int mode;
    static ArrayList<Edge> edges;
    static FileWriter f = new FileWriter();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter KochFractal level (only numbers, between 1 and 10): ");

        
        
        //1 bin zonder
        //2 bin met
        //3 text zonder
        //4 text met

        try {
            level = Integer.valueOf(reader.readLine());
            if (level < 1 || level > 10) throw new Exception("false input");
            System.out.print("Enter save mode 1=bin, 2=binBuffer, 3=text, 4=textBuffer: ");
            mode = Integer.valueOf(reader.readLine());
            if (mode > 0 && mode < 5){            
                f.saveEdges(level, mode);
            }
            else {
                System.out.print(mode + " is not a correct mode.");
            }
            
        }
        catch (NumberFormatException ex) {
            System.err.println("Wrong input, please only use numbers");
        }
        catch (Exception ex) {
            System.err.println("Wrong input, the number should be in between 1 and 10");
        }
    }
    
    
}
