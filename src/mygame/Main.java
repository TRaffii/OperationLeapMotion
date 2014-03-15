package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.leapmotion.leap.Controller;
import mygame.Controller.UserLogin;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
/** Sample 7 - how to load an OgreXML model and play an animation,
 * using channels, a controller, and an AnimEventListener. */
public class Main extends SimpleApplication  {
  // Create a sample listener and controller
        LeapListener listener;
        public int winCount = 0;
        Controller controller = new Controller();
        private Screen screen;
  public static void main(String[] args) {
    Main app = new Main();
      AppSettings settings = new AppSettings(true);
      //my custom Settings of app
      settings.setTitle("Operation Leap Motion");
      app.setSettings(settings);
    app.start();
  }
 
  public void simpleInitApp() {
      Box b = new Box(1,1,1);
      Geometry geom = new Geometry("Box",b);
      Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
      mat.setColor("Color", ColorRGBA.Pink);
      geom.setMaterial(mat);
      rootNode.attachChild(geom);
      listener = new LeapListener(this.settings.getWidth(),this.settings.getHeight());
      
      
      //Leap motion section
      //controller.addListener(listener);
//      DirectionalLight sun = new DirectionalLight();
//      sun.setDirection((new Vector3f(-0.1f,-0.7f,-1.0f)));
//      rootNode.addLight(sun);
     
      initGUIScreen();
     
      flyCam.setDragToRotate(true); //disable to fly CAM
       
      
  }
  private void initGUIScreen()
  {
        Screen screen = new Screen(this, "tonegod/gui/style/def/style_map.xml");
        guiNode.addControl(screen);
        screen.setUseCustomCursors(true);
        UserLogin us = new UserLogin(this, screen);
        stateManager.attach(us);
        // Add it to out initial window
        

        // Add window to the screen
     

  }
  public final void createNewWindow(String someWindowTitle) {
    Window nWin = new Window(
        screen,
        "Window" + winCount,
        new Vector2f( (screen.getWidth()/2)-175, (screen.getHeight()/2)-100 )
    );
    nWin.setWindowTitle(someWindowTitle);
    screen.addElement(nWin);
    winCount++;
}
  @Override
  public void stop()
  {
      controller.removeListener(listener);
      System.out.println("test");
      super.stop();
     
  }
}