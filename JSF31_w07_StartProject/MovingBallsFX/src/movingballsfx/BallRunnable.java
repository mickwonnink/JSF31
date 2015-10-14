/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private Ball ball;
    BallMonitor monitor;

    public BallRunnable(Ball ball, BallMonitor mon) {
        this.ball = ball;
        monitor = mon;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ball.move();
                if (ball.getColor() == Color.BLUE){
                    if (ball.isEnteringCs()) monitor.enterWriter();
                    else if (ball.isLeavingCs()) monitor.exitWriter();
                }
                else if (ball.getColor() == Color.RED){
                    if (ball.isEnteringCs()) monitor.enterReader();
                    else if (ball.isLeavingCs()) monitor.exitReader();
                }
                   
                Thread.sleep(ball.getSpeed());
                
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
