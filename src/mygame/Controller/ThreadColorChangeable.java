/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.tools.Color;
import java.util.Date;

/**
 *
 * @author Rafal
 */
public class ThreadColorChangeable implements Runnable {

        int localR;
        int localG;
        int localB;
        Element niftyElement;

    public ThreadColorChangeable(int localR, int localG, int localB, Element niftyElement) {
        
        this.localR = localR;
        this.localG = localG;
        this.localB = localB;
        this.niftyElement = niftyElement;
    }
        boolean isWorking = true;
    public void run() {
    try {
        while (isWorking) {
            localR+=5;            
            localG+=9;
            localB-=5;
            if(localR >= 158 && localG >=222 && localB <=0)
                isWorking = false;
            niftyElement.getRenderer(PanelRenderer.class).setBackgroundColor(new Color(localR,localG,localB,100));
            System.out.println(new Date());
            Thread.sleep(100); // every 0.1 sec
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}
