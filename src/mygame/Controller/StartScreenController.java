/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.util.Date;

/**
 *
 * @author Rafal
 */
public class StartScreenController  extends AbstractAppState implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    private boolean  succesfullInteration = false;
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
       
    }
    /** Custom methods */
    public void startGame(String nextScreen)
    {
        nifty.gotoScreen(nextScreen);
    }

    public StartScreenController() {
    }
   
    public void QuittingOn() throws InterruptedException
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("panel_bottom_right");
        niftyElement.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#9ede00"));
        // swap old with new text
        Runnable colorChangeable = new ThreadColorChangeable(73,66,61,niftyElement);
        Thread colorThread = new Thread(colorChangeable);
        colorThread.start();
        colorThread.join();
        colorThread.interrupt();
       // app.stop();
    }
    public StartScreenController(String data)
    {
        
    }
    

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
    
    @Override
    public void initialize(AppStateManager stateManger , Application app)
    {
        super.initialize(stateManger, app);
        this.app = (SimpleApplication)app;
    }
    @Override
    public void update(float tpf)
    {
    
    }
     
}
