package mygame;

import mygame.Controller.LeapListener;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.leapmotion.leap.Controller;
import de.lessvoid.nifty.Nifty;
import java.util.Calendar;
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

    public static boolean isLeftHandActive() {
        return leftHandActive;
    }

    public static void setLeftHandActive(boolean aLeftHandActive) {
        leftHandActive = aLeftHandActive;
    }
    // Create a sample listener and controller
    //physix
    private BulletAppState bulletAppState;
    //Material
    Material floor_mat;
    LeapListener listener;
    private static boolean MainGameWorking = false;

    public static boolean isMainGameWorking() {
        return MainGameWorking;
    }

    public static void setMainGameWorking(boolean MainGameWorking) {
        Main.MainGameWorking = MainGameWorking;
    }
    boolean isLeftPointerInCollision = false;
    boolean isRightPointerInCollision = false;
    long timeSpan = Calendar.getInstance().getTimeInMillis();
    private RigidBodyControl brick_phy;
    private RigidBodyControl toolLeft;
    private RigidBodyControl toolRight;
    private Quaternion floorQuatRotate;
    private boolean isRotationStart = false;
    Geometry pointer1;
    float global = 0;
    Geometry pointer2;
    BoundingBox test;
    static Geometry pickUpBox1;
    static Material materialPickUpActive;

    public static Material getMaterialPickUpActive() {
        return materialPickUpActive;
    }

    public static void setMaterialPickUpActive(Material materialPickUpActive) {
        Main.materialPickUpActive = materialPickUpActive;
    }

    public static Geometry getPickUpBox1() {
        return pickUpBox1;
    }

    public static void setPickUpBox1(Geometry pickUpBox1) {
        Main.pickUpBox1 = pickUpBox1;
    }
    public int winCount = 0;
    Nifty nifty;
    static AudioNode music;
    private Node pickables;
    private Node tools;
    private Node tableNode;
    private RigidBodyControl floor_phy;
    private RigidBodyControl releaseArea_phy;
    private Box floor;
    private Box releaseArea;
    private Quaternion oldRotate = Quaternion.ZERO;
//    private List<Vector3f> startPhysicElementPositions = new ArrayList<Vector3f>();
//    private List<RigidBodyControl> physicObjects = new ArrayList<RigidBodyControl>();
    private Vector3f startBox1Position;
    static Vector3f thumbVector = new Vector3f();
    static Vector3f thumbRotateVector = new Vector3f();
    static Vector3f foreFingerRotateVector = new Vector3f();
    static Vector3f cameraRotationRPY = new Vector3f(Vector3f.ZERO);
    static Vector3f cameraPositionXYZ = new Vector3f(Vector3f.ZERO);
    private static boolean leftHandActive = false;
    private static Spatial pickUpElement;

    public static Spatial getPickUpElement() {
        return pickUpElement;
    }

    public static void setPickUpElement(Spatial pickUpElement) {
        Main.pickUpElement = pickUpElement;
    }

    public static Vector3f getCameraPositionXYZ() {
        return cameraPositionXYZ;
    }

    public static void setCameraPositionXYZ(Vector3f cameraPositionXYZ) {
        Main.cameraPositionXYZ = cameraPositionXYZ;
    }

    public static Vector3f getCameraRotationRPY() {
        return cameraRotationRPY;
    }

    public static AudioNode getMusic() {
        return music;
    }

    public static void setMusic(AudioNode music) {
        Main.music = music;
    }

    public static void setCameraRotationRPY(Vector3f cameraRotationRPY) {
        Main.cameraRotationRPY = cameraRotationRPY;
    }
    static int screenFlag = 0;

    public static int getScreenFlag() {
        return screenFlag;
    }

    public static void setScreenFlag(int screenFlag) {
        Main.screenFlag = screenFlag;
    }

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

    public static Main getApp() {
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
        tableNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);

    }

    public void initReleaseArea() {
        Geometry releaseAreaGeo = new Geometry("Release area", releaseArea);
        releaseAreaGeo.setMaterial(floor_mat);
        releaseAreaGeo.setLocalTranslation(12f, -0.1f, 1f);
        tableNode.attachChild(releaseAreaGeo);
        /* Make the floor physical with mass 0.0f! */
        releaseArea_phy = new RigidBodyControl(0.0f);
        releaseAreaGeo.addControl(releaseArea_phy);
        bulletAppState.getPhysicsSpace().add(releaseArea_phy);

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
        tableNode = new Node("Table");
        rootNode.attachChild(pickables);
        rootNode.attachChild(tools);
        rootNode.attachChild(tableNode);
        setCameraPositionXYZ(new Vector3f(0, 3f, 12f));
        flyCam.setMoveSpeed(10);
        inputManager.addMapping("pick up", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("reset", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("escape", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addListener(analogListener, "pick up");
        inputManager.addListener(analogListener, "reset");
        inputManager.addListener(analogListener, "escape");
        floor = new Box(10f, 0.1f, 5f);
        releaseArea = new Box(5f, 0.1f, 5f);
        initMaterials();
        initFloor();
        initReleaseArea();
        Arrow arrow = new Arrow(new Vector3f(0, 2, 2));
        Arrow arrow2 = new Arrow(new Vector3f(0, 2, 2));
        //Box b = new Box(1,1,1);
        Box b1 = new Box(0.1f, 0.1f, 3);
        Box b2 = new Box(0.1f, 0.1f, 3);
        Box pickUp1 = new Box(0.5f, 0.5f, 0.5f);
//        assetManager.registerLocator("http://better-web.pl/test/HeartAfter.zip", 
//                             HttpZipLocator.class);
//        setPickUpElement(assetManager.loadModel("Human Heart.obj"));
//        getPickUpElement().scale(0.40f); // prevent that collision mesh is smaller than object representation(texture)
//        CollisionShape shapeBeforeScale = CollisionShapeFactory.createDynamicMeshShape(pickUpElement);
//        getPickUpElement().scale(0.40f);

        setMaterialPickUpActive(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));

        getMaterialPickUpActive().setTexture("LightMap", assetManager.loadTexture(
                "Models/HumanHeart-color.jpg"));
//        getPickUpElement().setMaterial(matHeart);
        pickUpBox1 = new Geometry("Brick 1", pickUp1);
        pointer1 = new Geometry("Pointer Left", b1);
        pointer2 = new Geometry("Pointer Right", b2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");


        mat.setColor("Color", ColorRGBA.Pink);
        mat2.setColor("Color", ColorRGBA.Blue);
        mat3.setColor("Color", ColorRGBA.Orange);


        pointer1.setMaterial(mat);
        pointer2.setMaterial(mat2);
        pickUpBox1.setMaterial(getMaterialPickUpActive());

        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(0.5f, 0.5f, 0.5f));

        brick_phy = new RigidBodyControl(boxShape, 10f);
        brick_phy.setKinematic(false);
        toolLeft = new RigidBodyControl(0.1f);
        toolRight = new RigidBodyControl(0.1f);
//        pickUpElement.setLocalTranslation(new Vector3f(0f, 1f, 0f));
        pickUpBox1.setLocalTranslation(new Vector3f(0f, 5f, 0f));
//        getPickUpElement().addControl(brick_phy);
        pickUpBox1.addControl(brick_phy);

        pointer1.addControl(toolLeft);
        pointer2.addControl(toolRight);


        bulletAppState.getPhysicsSpace().add(brick_phy);
        bulletAppState.getPhysicsSpace().add(toolLeft);
        bulletAppState.getPhysicsSpace().add(toolRight);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        brick_phy.setFriction(1.0f);
        brick_phy.setRestitution(0.0f);


        //brick_phy.setPhysicsLocation(new Vector3f(0f, 6f, 0f));
        toolLeft.setPhysicsLocation(new Vector3f(3f, 1f, 0f));
        toolRight.setPhysicsLocation(new Vector3f(4f, 1f, 0f));
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
        music = new AudioNode(assetManager, "Sounds/HappyBee.wav", true);
        if (settingFile.Get("music").equals("on")) {
            music.play();
        }
        initGUIScreen();


        rootNode.attachChild(SkyFactory.createSky(
                assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        initCrossHairs();
        startBox1Position = brick_phy.getPhysicsLocation().clone();

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
    }

    @Override
    public void stop() {
        controller.removeListener(listener);
        super.stop();

    }
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("escape")) {
                MainGameWorking = false;
            }
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
//           System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
                }
                //System.out.println(results.size());
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Geometry target = results.getClosestCollision().getGeometry();
                    // Here comes the action:
                    Vector3f pt = results.getCollision(0).getContactPoint();
                    pt.z = 0;
                    if (pt.y < 0.1f)//dont fall down
                    {
                        pt.y = 0.1f;
                    }
                    target.getControl(RigidBodyControl.class).setPhysicsLocation(pt);
                    target.getControl(RigidBodyControl.class).clearForces();
                    if (!target.getControl(RigidBodyControl.class).isActive()) {
                        target.getControl(RigidBodyControl.class).activate();
                    }
                }
            }
            if (name.equals("reset")) {
                brick_phy.clearForces();
                brick_phy.setPhysicsRotation(Matrix3f.ZERO);
                brick_phy.setPhysicsLocation(new Vector3f(1f, 0.5f, 0));
                if (!brick_phy.isActive()) {

                    brick_phy.activate();

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

    private boolean isQuaterionEquals(Quaternion qt1, Quaternion qt2) {
        if (Math.abs(qt1.getX() - qt2.getX()) > 0.001) {
            return false;
        }
        if (Math.abs(qt1.getY() - qt2.getY()) > 0.001) {
            return false;
        }
        if (Math.abs(qt1.getZ() - qt2.getZ()) > 0.001) {
            return false;
        }
        if (Math.abs(qt1.getW() - qt2.getW()) > 0.001) {
            return false;
        }
        return true;
    }

    @Override
    public void simpleUpdate(float tpf) {

        if (brick_phy.getLinearVelocity().getY() < -10f) {
            brick_phy.setLinearVelocity(Vector3f.ZERO);
        }
        if (!brick_phy.isActive()) {
            brick_phy.activate();
        }
        if (brick_phy.getPhysicsLocation().y < -5.0f) // dont fall to much
        {
            brick_phy.setPhysicsLocation(Vector3f.ZERO);
        }
//       brick_phy.setAngularDamping(0f);
//       brick_phy.setAngularFactor(0f);
//       brick_phy.setAngularSleepingThreshold(0f);
//       brick_phy.setAngularVelocity(Vector3f.ZERO);
//       brick_phy.setApplyPhysicsLocal(false);
//       brick_phy.setCcdMotionThreshold(0f);
//       brick_phy.setCcdSweptSphereRadius(0f);
//       brick_phy.setDamping(0f,0f);
//       brick_phy.setFriction(0f);
//       brick_phy.setGravity(Vector3f.ZERO);
//       brick_phy.setKinematic(false);
//       brick_phy.setKinematicSpatial(false);
//       brick_phy.setLinearDamping(0f);
//       brick_phy.setLinearSleepingThreshold(0f);
//       brick_phy.setLinearVelocity(Vector3f.ZERO);
//       brick_phy.setRestitution(0f);
//       brick_phy.setSleepingThresholds(0f, 0f);

        //Camera rotation by left hand RPY
//        test = (BoundingBox) pickUpBox1.getModelBound();
        Quaternion qatRotationCamera = new Quaternion().fromAngles(getCameraRotationRPY().x, 0, getCameraRotationRPY().z);
        Quaternion qatRotationCameraLocal = new Quaternion().fromAngles(0, getCameraRotationRPY().y, 0);
        cam.setRotation(qatRotationCamera);
        cam.setLocation(getCameraPositionXYZ());
//        brick_phy.setPhysicsRotation(qatRotationCameraLocal);
        //pickUpBox1.setLocalRotation(qatRotationCameraLocal);//!!!!
        floorQuatRotate = floor_phy.getPhysicsRotation().clone();
//        System.out.println("F"+floorQuatRotate);
//        System.out.println("C"+qatRotationCameraLocal);
        if (isLeftHandActive()) {
//            System.out.println("ROTACJA");
//            if(!isRotationStart)
//                {
//                    oldRotate = qatRotationCameraLocal;
//                    isRotationStart = true;
//                }
            Vector3f tempVector = startBox1Position.clone();
            System.out.println("QUAT:" + qatRotationCameraLocal.subtract(oldRotate).toString());
            //qatRotationCameraLocal.subtract(oldRotate).mult(tempVector, tempVector);
            qatRotationCameraLocal.mult(tempVector, tempVector);
            System.out.println("VECTOR:" + tempVector.toString());
            brick_phy.setPhysicsRotation(qatRotationCameraLocal);
            //TODO : zrobic tak aby po podniesieniu reki klocekreleaseArea_phy.setPhysicsLocation(tempVector); pozostawal
            brick_phy.setPhysicsLocation(tempVector);



        } else {
            oldRotate = qatRotationCameraLocal;
            isRotationStart = false;
//             System.out.println(brick_phy.getPhysicsLocation());
            startBox1Position = brick_phy.getPhysicsLocation().clone();
        }
        floor_phy.setPhysicsRotation(qatRotationCameraLocal);
        releaseArea_phy.setPhysicsRotation(releaseArea_phy.getPhysicsRotation().subtract(qatRotationCamera));

        //pickUpBox1.set(qatRotationCameraLocal);
        //floor_phy.setPhysicsLocation(new Vector3f(0, getCameraRotationRPY().y, 0));
        //pshycis rotation for all elements on the table
        for (Spatial spat : tableNode.getChildren()) {
            spat.getControl(RigidBodyControl.class).setPhysicsRotation(qatRotationCameraLocal);
        }
        //pickUpBox1.setLocalRotation(qatRotationCameraLocal);
        for (Spatial spat : pickables.getChildren()) {
//            spat.getControl(RigidBodyControl.class).setEnabled(false);
//            spat.getControl(RigidBodyControl.class).setPhysicsRotation(qatRotationCameraLocal);
//            spat.getControl(RigidBodyControl.class).setEnabled(true);
            //spat.getControl(RigidBodyControl.class).setPhysicsRotation(qatRotationCameraLocal);
//          
//            spat.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(2, 1, 0));
        }
        //floor_phy.setPhysicsRotation(qatRotationCameraLocal);
        // make the player rotate:
        Vector3f temp = getThumbRotateVector().mult(new Vector3f(0, 0, 1));
        // Quaternion qat =  new Quaternion(getThumbRotateVector().toArray(null));
        Quaternion quat1 = new Quaternion().fromAngles(getThumbRotateVector().x, getThumbRotateVector().y, 0);
        Quaternion quat2 = new Quaternion().fromAngles(getForeFingerRotateVector().x, getForeFingerRotateVector().y, 0);
        toolRight.setPhysicsLocation(cam.getRotation().mult(getThumbVector()));//during camera rotation recalculating hand position
        toolRight.setPhysicsRotation(quat1.mult(cam.getRotation()));

        toolLeft.setPhysicsLocation(cam.getRotation().mult(getForeFingerVector()));//during camera rotation recalculating hand position
        toolLeft.setPhysicsRotation(quat2.mult(cam.getRotation()));


//        pointer2.setLocalTranslation(cam.getRotation().mult(getThumbVector()));
//        pointer2.setLocalRotation(quat1.mult(cam.getRotation()));
//        pointer1.setLocalTranslation(cam.getRotation().mult(getForeFingerVector()));
//        pointer1.setLocalRotation(quat2.mult(cam.getRotation()));
        //geom.rotate( 0f , 0.002f , 0f );
        // 1. Reset results list.

        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.

        // 3. Collect intersections between Ray and Shootables in results list.
        BoundingVolume bv = pickUpBox1.getWorldBound();
        tools.collideWith(bv, results);
//        System.out.println("BoxPosition : "+brick_phy.getLinearVelocity());
        // 4. Print the results
        boolean foundLeftTool = false;
        boolean foundRightTool = false;
//        System.out.println("Result :"+results.size());
        float averageZPosition = 0;
        for (int i = 0; i < results.size(); i++) {
            String party = results.getCollision(i).getGeometry().getName();
            averageZPosition += results.getCollision(i).getGeometry().getLocalTranslation().getZ();

//            System.out.println("Z :"+results.getCollision(i).getGeometry().getLocalTranslation().getZ());
            if (party.equals("Pointer Left")) {
                foundLeftTool = true;
            } else if (party.equals("Pointer Right")) {
                foundRightTool = true;
            }

        }
        if (foundLeftTool && foundRightTool) {
//             System.out.println("CATCH");
            //Average of tools position
            Vector3f betweenTools = new Vector3f((toolLeft.getPhysicsLocation().x + toolRight.getPhysicsLocation().x) / 2,
                    (toolLeft.getPhysicsLocation().y + toolRight.getPhysicsLocation().y) / 2, averageZPosition / results.size());
            brick_phy.setPhysicsLocation(betweenTools);
//             pickUpBox1.setLocalTranslation(pointer1.getLocalTranslation());
//             System.out.println(pointer1.getLocalTranslation());
            Material matActive = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matActive.setColor("Color", ColorRGBA.Red);
            pickUpBox1.setMaterial(matActive);
        } else {
            Material matActive = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            pickUpBox1.setMaterial(getMaterialPickUpActive());

        }

    }
//  
}