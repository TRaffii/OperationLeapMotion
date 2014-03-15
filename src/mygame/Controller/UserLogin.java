/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import mygame.Main;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.windows.LoginBox;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author Rafal
 */
public class UserLogin extends AbstractAppState {
    Main app;
    Screen screen;
    Element bgLayer, stage, uiLayer;
    LoginBox loginWindow;
 
    public UserLogin(Main app, Screen screen) {
        this.app = app;
        this.screen = screen;
        screen.parseLayout("Interface/Welcom.xml", this);
        
    }
 
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
 
        initLoginWindow();
    }
 
    public void initLoginWindow() {
        bgLayer = new Element(screen, UIDUtil.getUID(),
			Vector2f.ZERO,
			new Vector2f(screen.getWidth(),screen.getHeight()),
			Vector4f.ZERO,
			screen.getStyle("Common").getString("blankImg")
		);
        
		bgLayer.setEffectZOrder(false);
		bgLayer.setIgnoreMouse(true);
		bgLayer.setIsMovable(false);
		bgLayer.setIsResizable(false);
		bgLayer.setAsContainerOnly();
		screen.addElement(bgLayer);
                stage = new Element(screen, UIDUtil.getUID(),
			Vector2f.ZERO,
			new Vector2f(screen.getWidth(),screen.getHeight()),
			Vector4f.ZERO,
			screen.getStyle("Common").getString("blankImg")
		);
		stage.setEffectZOrder(false);
		stage.setIgnoreMouse(true);
		stage.setIsMovable(false);
		stage.setIsResizable(false);
		stage.setAsContainerOnly();
		screen.addElement(stage);
		
		uiLayer = new Element(screen, UIDUtil.getUID(),
			Vector2f.ZERO,
			new Vector2f(screen.getWidth(),screen.getHeight()),
			Vector4f.ZERO,
			screen.getStyle("Common").getString("blankImg")
		);
		uiLayer.setEffectZOrder(false);
		uiLayer.setIgnoreMouse(true);
		uiLayer.setIsMovable(false);
		uiLayer.setIsResizable(false);
		uiLayer.setAsContainerOnly();
		screen.addElement(uiLayer);
//                Panel panel = new Panel(screen, "panel", new Vector2f(0, 0),
//                    new Vector2f(screen.getWidth(), screen.getHeight())
//                );
//                panel.setIgnoreMouse(true);
//		panel.setIsMovable(false);
//		panel.setIsResizable(false);
        //screen.addElement(panel);
        /*
        loginWindow = new LoginBox(screen, 
                "loginWindow",
                new Vector2f(screen.getWidth()/2-175,screen.getHeight()/2-125)) {
            @Override
            public void onButtonLoginPressed(MouseButtonEvent evt, boolean toggled) {
                // Some call to the server to log the client in
                finalizeUserLogin();
            }

            @Override
            public void onButtonCancelPressed(MouseButtonEvent mbe, boolean bln) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        */
        //screen.addElement(loginWindow);
    }
 
    @Override
    public void cleanup() {
        
        super.cleanup();
 
        //screen.removeElement(loginWindow);
    }
 
    public void finalizeUserLogin() {
        // Some call to your app to unload this AppState and load the next AppState
       // app.someMethodToSwitchAppStates();
    }
}