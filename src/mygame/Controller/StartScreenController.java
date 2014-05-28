/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.nifty.loaderv2.types.PanelType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.util.Date;
import mygame.Main;

/**
 *
 * @author Rafal
 */
public class StartScreenController extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private Element control;
    private SimpleApplication app;
    Date effectStart;
    private boolean succesfullInteration = false;

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        nifty.registerMouseCursor("hand", "Interface/mouse-cursor-hand.png", 5, 4);
    }

    /**
     * Custom methods
     */
    public void StartGameEnd(String nextScreen) {
        if (activateButton()) {

            nifty.fromXml("Interface/" + nextScreen + ".xml", nextScreen);
            nifty.gotoScreen(nextScreen);
        }
    }

    public StartScreenController() {
    }


    public void OptionsOn() {
        effectStart = new Date();
    }

    public void OptionsOff() {
        if (activateButton()) {

            nifty.fromXml("Interface/options.xml", "options", new OptionsScreenController(app));
            //nifty.gotoScreen("options");

        }
    }

    public void QuittingOff() {
        if (activateButton()) {
            System.out.println("KONIEC");
            app.stop();
        }
    }

    public StartScreenController(String data) {
    }

    private boolean activateButton() {
        long diff = new Date().getTime() - effectStart.getTime();
        if (diff > 2400) {
            return true;
        } else {
            return false;
        }
    }

    public void onStartScreen() {
        System.out.println("Startuje screen startowy");
    }

    public void onEndScreen() {
    }

    @Override
    public void initialize(AppStateManager stateManger, Application app) {
        super.initialize(stateManger, app);
        this.app = (SimpleApplication) app;
    }

    @Override
    public void update(float tpf) {
    }
}
