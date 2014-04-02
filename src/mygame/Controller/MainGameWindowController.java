/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import static mygame.Main.getCameraRotationRPY;

/**
 *
 * @author Rafal
 */
public class MainGameWindowController extends AbstractAppState implements ScreenController{

    public void bind(Nifty nifty, Screen screen) {
     }

    public void onStartScreen() {
         /** Add fog to a scene */
        Main.setScreenFlag(2);
     Main.getApp().getInputManager().setCursorVisible(false);
     Main.getApp().getFlyByCamera().setDragToRotate(false);
/* A colored lit cube. Needs light source! */ 
          
       }
@NiftyEventSubscriber(pattern="GTextfield.*")
    public void onChange(String id, TextFieldChangedEvent event) {
        System.out.println(id);
        Float val = Float.parseFloat(event.getText());
        Vector3f temp = Main.getCameraRotationRPY();
        if ("GTextfield2".equals(id )) {
            temp.x = val;
        }
        else if ("GTextfield4".equals(id )) {
            temp.y = val;
        }
        else if ("GTextfield5".equals(id )) {
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
