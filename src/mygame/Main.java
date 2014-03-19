package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.leapmotion.leap.Controller;
import de.lessvoid.nifty.Nifty;
import mygame.Controller.StartScreenController;
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
        Nifty nifty;
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
      controller.addListener(listener);
//      DirectionalLight sun = new DirectionalLight();
//      sun.setDirection((new Vector3f(-0.1f,-0.7f,-1.0f)));
//      rootNode.addLight(sun);
     
      initGUIScreen();
     
      flyCam.setDragToRotate(true); //disable to fly CAM
       
      
  }
  private void initGUIScreen()
  {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            assetManager, inputManager, audioRenderer, guiViewPort);
        /** Create a new NiftyGUI object */
        nifty = niftyDisplay.getNifty();
       System.out.println( nifty.getVersion());
        StartScreenController startScreenState = new StartScreenController();
        startScreenState.initialize(stateManager, this);
        stateManager.attach(startScreenState);
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml("Interface/screen.xml", "start",startScreenState);
        // nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        // disable the fly cam
        flyCam.setDragToRotate(true);
        // Add it to out initial window
        

        // Add window to the screen
     

  }

  @Override
  public void stop()
  {
      controller.removeListener(listener);
      super.stop();
     
  }
  @Override
  public void simpleUpdate(float tpf) {
        // make the player rotate:
     
    }
}