/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import mygame.Main;
import mygame.model.DatabaseManager;

/**
 *
 * @author Rafal
 */
public class MainGameWindowController extends AbstractAppState implements ScreenController {

    private Screen screen;
    SimpleDateFormat dateFormatBegin;
    Element timeElement;
    DateTime dateBegin;
    DatabaseManager handler = new DatabaseManager();
    Application app;
    Nifty nifty;
    AppStateManager stateManager;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = app;
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }

    public void bind(Nifty nifty, Screen screen) {
        this.screen = screen;
        this.nifty = nifty;
    }

    public String getTime() {
        return "aa";
    }
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        String tempSeconds = (Seconds.secondsBetween(dateBegin, new DateTime()).getSeconds()%60)+"";
        String tempMinutes = (Minutes.minutesBetween(dateBegin, new DateTime()).getMinutes()%60)+"";
        timeElement.getRenderer(TextRenderer.class).setText("Time "+tempMinutes+":"+tempSeconds);
        if(!Main.isMainGameWorking())
        {
            java.util.Date date = new java.util.Date();
            
            Integer result = Integer.parseInt(tempMinutes)*60+Integer.parseInt(tempSeconds);
            handler.insertUser("test", result, new java.sql.Date(date.getTime()));
            StartScreenController startScreen = new StartScreenController();
            startScreen.initialize(stateManager, app);
            stateManager.detach(this);
            Main.getApp().getInputManager().setCursorVisible(true);
            Main.getApp().getFlyByCamera().setDragToRotate(true);
            stateManager.attach(startScreen);
            nifty.fromXml("Interface/screen.xml", "start",startScreen);
            nifty.gotoScreen("start");
        }
    }

    public void onStartScreen() {
        /**
         * Add fog to a scene
         */
        Main.setScreenFlag(2);
        Main.setMainGameWorking(true);
        timeElement = screen.findElementByName("timeText");
        dateFormatBegin = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        dateBegin = new DateTime();
        System.out.println("E:"+isEnabled());      
        setEnabled(true);
        System.out.println("IN"+isInitialized());

//        String temp = (Seconds.secondsBetween(dateBegin, new DateTime()).getSeconds()+ " seconds");
//        timeElement.getRenderer(TextRenderer.class).setText(temp);
        Main.getApp().getInputManager().setCursorVisible(false);
        Main.getApp().getFlyByCamera().setDragToRotate(false);
        /* A colored lit cube. Needs light source! */

    }

    @NiftyEventSubscriber(pattern = "GTextfield.*")
    public void onChange(String id, TextFieldChangedEvent event) {
        System.out.println(id);
        Float val = Float.parseFloat(event.getText());
        Vector3f temp = Main.getCameraRotationRPY();
        if ("GTextfield2".equals(id)) {
            temp.x = val;
        } else if ("GTextfield4".equals(id)) {
            temp.y = val;
        } else if ("GTextfield5".equals(id)) {
            temp.z = val;
        }
        Main.setCameraRotationRPY(temp);
        /*
         Float val = Float.parseFloat(event.getText());
         Vector3f temp = Main.getForeFingerVector();
         temp.x = val;
         Main.setForeFingerRotateVector(temp);
         */
    }

    public void onEndScreen() {
    }
}
