package mygame;

import mygame.Controller.LeapListener;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.leapmotion.leap.Controller;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import mygame.Controller.StartScreenController;
import mygame.model.SettingsIO;
import tonegod.gui.core.Screen;

/**
 * test
 *
 * @author normenhansen
 */
/**
 * Sample 7 - how to load an OgreXML model and play an animation, using
 * channels, a controller, and an AnimEventListener.
 */
public class Main extends SimpleApplication {
    // Create a sample listener and controller

    //physix
    private BulletAppState bulletAppState;
    //Material
    Material floor_mat;
    LeapListener listener;
    private RigidBodyControl brick_phy;
    Geometry pointer1;
    float global = 0;
    Geometry pointer2;
    BoundingBox test;
    Geometry pickUpBox1;
    public int winCount = 0;
    Nifty nifty;
    AudioNode music;
    private Node pickables;
    private Node tools;
    private RigidBodyControl floor_phy;
    private Box floor;
    static Vector3f thumbVector = new Vector3f();
    static Vector3f thumbRotateVector = new Vector3f();
    static Vector3f foreFingerRotateVector = new Vector3f();
    
    public static Vector3f getThumbRotateVector() {
        return thumbRotateVector;
    }

    public static void setThumbRotateVector(Vector3f thumbRotateVector) {
        Main.thumbRotateVector = thumbRotateVector;
    }

    public static Vector3f getForeFingerRotateVector() {
        return foreFingerRotateVector;
    }

    public static void setForeFingerRotateVector(Vector3f foreFingerRotateVector) {
        Main.foreFingerRotateVector = foreFingerRotateVector;
    }

    public static Vector3f getThumbVector() {
        return thumbVector;
    }

    public static void setThumbVector(Vector3f thumbVector) {
        Main.thumbVector = thumbVector;
    }

    public static Vector3f getForeFingerVector() {
        return foreFingerVector;
    }

    public static void setForeFingerVector(Vector3f foreFingerVector) {
        Main.foreFingerVector = foreFingerVector;
    }
    static Vector3f foreFingerVector = new Vector3f();
    Controller controller = new Controller();
    private Screen screen;
    static Main app;
    public static Main getApp()
    {
        return app;
    }

    public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
        //my custom Settings of app
        settings.setTitle("Operation Leap Motion");
        app.setSettings(settings);
        app.start();
    }

    public void initFloor() {
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, -0.1f, 0);
        this.rootNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);
    }

    public void initMaterials() {
        floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
        key3.setGenerateMips(true);
        Texture tex3 = assetManager.loadTexture(key3);
        tex3.setWrap(Texture.WrapMode.Repeat);
        floor_mat.setTexture("ColorMap", tex3);
    }

    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        pickables = new Node("Pickables");
        tools = new Node("Tools");
        rootNode.attachChild(pickables);
        rootNode.attachChild(tools);
        cam.setLocation(new Vector3f(0, 3f, 12f));
        inputManager.addMapping("pick up",   new  MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(analogListener, "pick up");
        floor = new Box(10f, 0.1f, 5f);
        //floor.scaleTextureCoordinates(new Vector2f(1, 2));
        initMaterials();
        initFloor();
        Arrow arrow = new Arrow(new Vector3f(0, 2, 2));
        Arrow arrow2 = new Arrow(new Vector3f(0, 2, 2));
        //Box b = new Box(1,1,1);
        Box b1 = new Box(0.1f, 0.1f, 3);
        Box b2 = new Box(0.1f, 0.1f, 3);
        Box pickUp1 = new Box(0.3f, 0.3f, 0.3f);
        pickUpBox1 = new Geometry("PickUp1", pickUp1);
        pointer1 = new Geometry("Box", b1);
        pointer2 = new Geometry("Box", b2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Pink);
        mat2.setColor("Color", ColorRGBA.Blue);
        mat3.setColor("Color", ColorRGBA.Orange);
        pointer1.setMaterial(mat);
        pointer2.setMaterial(mat2);
        pickUpBox1.setMaterial(mat3);
        brick_phy = new RigidBodyControl(2f);
        pickUpBox1.addControl(brick_phy);
        bulletAppState.getPhysicsSpace().add(brick_phy);
        pickUpBox1.setLocalTranslation(0, 0, -1);
        tools.attachChild(pointer1);
        tools.attachChild(pointer2);
        pickables.attachChild(pickUpBox1);
        test = (BoundingBox) pickUpBox1.getModelBound();
        listener = new LeapListener(this.settings.getWidth(), this.settings.getHeight());
        SettingsIO settingFile = new SettingsIO("assets/Settings/ProgramSettings.xml");
        /* A colored lit cube. Needs light source! */

        //Leap motion section
        controller.addListener(listener);

        //      DirectionalLight sun = new DirectionalLight();
        //      sun.setDirection((new Vector3f(-0.1f,-0.7f,-1.0f)));
        //      rootNode.addLight(sun);
        initGUIScreen();
        music = new AudioNode(assetManager, "Sounds/HappyBee.wav", true);
        if (settingFile.Get("music").equals("on")) {
            music.play();
        }

        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        initCrossHairs();

    }

    private void initGUIScreen() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        /**
         * Create a new NiftyGUI object
         */
        nifty = niftyDisplay.getNifty();
        System.out.println(nifty.getVersion());
        StartScreenController startScreenState = new StartScreenController();
        startScreenState.initialize(stateManager, this);
        stateManager.attach(startScreenState);
        /**
         * Read your XML and initialize your custom ScreenController
         */
        nifty.fromXml("Interface/screen.xml", "start", startScreenState);
        // nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        // disable the fly cam
        flyCam.setDragToRotate(true);
        // Add it to out initial window


        // Add window to the screen


    }

    @Override
    public void stop() {
        controller.removeListener(listener);
        super.stop();

    }
private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
 
      if (name.equals("pick up")) {         // test?
 //        brick_phy.setPhysicsLocation(new Vector3f(0, 4f, 0));
//        if(!brick_phy.isActive())
//            brick_phy.activate();
         CollisionResults results = new CollisionResults();
         // Aim the ray from camera location in camera direction
         // (assuming crosshairs in center of screen).
         Ray ray = new Ray(cam.getLocation(), cam.getDirection());
         //System.out.println("Ray dir = "+ray.getDirection());
         // Collect intersections between ray and all nodes in results list.
         pickables.collideWith(ray, results);
         for (int i = 0; i < results.size(); i++) {
           // For each “hit”, we know distance, impact point, geometry.
           float dist = results.getCollision(i).getDistance();
           Vector3f pt = results.getCollision(i).getContactPoint();
           String target = results.getCollision(i).getGeometry().getName();
           System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
         }
         //System.out.println(results.size());
         if (results.size() > 0) {
           // The closest result is the target that the player picked:
           Geometry target = results.getClosestCollision().getGeometry();
           // Here comes the action:
           Vector3f pt = results.getCollision(0).getContactPoint();
           target.getControl(RigidBodyControl.class).setPhysicsLocation(pt);
           if(!target.getControl(RigidBodyControl.class).isActive())
           {
               target.getControl(RigidBodyControl.class).activate();
           }
         }
      } 
 
    
 
    }
  };
  protected void initCrossHairs() {
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+");        // fake crosshairs :)
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
      settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
    guiNode.attachChild(ch);
  }
    @Override
    public void simpleUpdate(float tpf) {
        
        // make the player rotate:
        Vector3f temp = getThumbRotateVector().mult(new Vector3f(0, 0, 1));
        //Quaternion qat =  new Quaternion(getThumbRotateVector().toArray(null));
        Quaternion qat = new Quaternion().fromAngles(getThumbRotateVector().x, getThumbRotateVector().y, getThumbRotateVector().z);
        //Quaternion qat =  new Quaternion().fromAngleAxis(90*FastMath.DEG_TO_RAD, getThumbRotateVector());
        //Quaternion qat =  new Quaternion().fromAngleAxis(getThumbRotateVector().x, new Vector3f(1,-1,0));
        //System.out.println(qat);
        // Quaternion qat =  new Quaternion().fromAngleAxis(getThumbRotateVector().x, new Vector3f(1,0,0));
        //Quaternion qat2 =  new Quaternion().fromAngleAxis(getThumbRotateVector().z, new Vector3f(0,0,1));
        //pointer1.setLocalRotation(qat);
        //pointer1.setLocalRotation(qat2);
        //pointer1.setLocalRotation(new Quaternion().fromAngleAxis(getThumbRotateVector().y, new Vector3f(0,0,1)));
        pointer1.setLocalTranslation(getThumbVector());

        //pointer2.setLocalRotation( new Quaternion().fromAngleAxis(getForeFingerRotateVector().x, new Vector3f(1,0,0)));
        //pointer2.setLocalRotation( new Quaternion().fromAngleAxis(getForeFingerRotateVector().z, new Vector3f(0,0,1)));
        pointer2.setLocalTranslation(getForeFingerVector());
        //geom.rotate( 0f , 0.002f , 0f );
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.

        // 3. Collect intersections between Ray and Shootables in results list.
        tools.collideWith(test, results);

        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
        if (results.size() > 0) {
            pickUpBox1.setLocalTranslation(0, 0, getForeFingerVector().z + 0.3f);
        }
    }
}