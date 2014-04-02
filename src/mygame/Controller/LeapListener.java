/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Main;

/**
 *
 * @author Rafal
 */
public class LeapListener extends Listener {

    int screenWidth;
    int screenHeight;
    Robot robot;

    public LeapListener(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(LeapListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        /*
         System.out.println("Frame id: " + frame.id()
         + ", timestamp: " + frame.timestamp()
         + ", hands: " + frame.hands().count()
         + ", fingers: " + frame.fingers().count()
         + ", tools: " + frame.tools().count()
         + ", gestures " + frame.gestures().count());
         * */
        if (!frame.hands().isEmpty()) {
            // Get the first hand
            Hand hand = frame.hands().rightmost();

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();

            if (fingers.count() > 0) { //use finger as mouse pointer
                // Calculate the hand's average finger tip position
                Vector avgPos = Vector.zero();
                for (Finger finger : fingers) {
                    avgPos = avgPos.plus(finger.tipPosition());
                }
                avgPos = avgPos.divide(fingers.count());
                ScreenList screenList = controller.locatedScreens();
                Screen screen = screenList.get(0);
                com.leapmotion.leap.Vector intersection = screen.intersect(fingers.get(0), true);
                int screenw = screen.widthPixels();
                int screenh = screen.heightPixels();
                int posXi = (int) (intersection.get(0) * screenw);      // actual x position on your screen
                int posYi = (int) (screenh - intersection.get(1) * screenh);
                if (Main.getScreenFlag() != 2) {
                    robot.mouseMove(posXi, posYi);
                    avgPos = avgPos.divide(fingers.count());
                }

            }
            if (fingers.count() == 2)//use finger as getter 
            {
                int podziel = 30;
                int pomnoz = 3;
//                System.out.println("R:"+(fingers.get(0).direction().roll()));
//                System.out.println("P:"+(fingers.get(0).direction().pitch()));
//                System.out.println("Y:"+(fingers.get(0).direction().yaw()));
                //Main.setThumbRotateVector(new Vector3f(fingers.get(0).direction().roll()-FastMath.HALF_PI,FastMath.PI- fingers.get(0).direction().yaw()+FastMath.PI, ( fingers.get(0).direction().roll()-FastMath.HALF_PI)));
                //Main.setThumbRotateVector(new Vector3f(0,fingers.get(0).direction().roll(), 0));
                Main.setThumbRotateVector(new Vector3f(fingers.get(0).direction().pitch(), -fingers.get(0).direction().yaw(), 0));
                Main.setThumbVector(new Vector3f(-fingers.get(0).tipPosition().getX() / podziel, (fingers.get(0).tipPosition().getY() - 200) / podziel, -fingers.get(0).tipPosition().getZ() / podziel));

                Main.setForeFingerRotateVector(new Vector3f(fingers.get(1).direction().pitch(), -fingers.get(1).direction().yaw(), 0));
                Main.setForeFingerVector(new Vector3f((-fingers.get(1).tipPosition().getX()) / podziel, (fingers.get(1).tipPosition().getY() - 200) / podziel, -fingers.get(1).tipPosition().getZ() / podziel));

            }
            if (frame.hands().count() > 1) {

                Hand handCameraControl = frame.hands().leftmost();
                System.out.println(handCameraControl.sphereRadius());
                if (handCameraControl.fingers().count() > 2) {
                    int podziel = 30;
                    System.out.println("ROLL:" + (handCameraControl.direction().roll()));
                    System.out.println("PITCH:" + (handCameraControl.palmPosition().pitch()));
                    System.out.println("Z:" + (handCameraControl.palmPosition().getZ() / podziel));
                   // Main.setCameraRotationRPY(new Vector3f(FastMath.HALF_PI - handCameraControl.direction().roll(), (float) ((handCameraControl.palmPosition().getX()/50)%6.28), 0));
                    
                    
                        Main.setCameraRotationRPY(new Vector3f(FastMath.QUARTER_PI, (float) ((handCameraControl.palmPosition().getX()/50)%6.28), 0));
                   
                    
                    Vector3f newPositionVectorXYZ = new Vector3f(handCameraControl.palmPosition().getX() / podziel, handCameraControl.palmPosition().getY() / podziel, 
                            (handCameraControl.palmPosition().getZ() + 100) / podziel);
                   
                    Main.setCameraPositionXYZ(new Vector3f(handCameraControl.palmPosition().getX() / podziel, handCameraControl.palmPosition().getY() / podziel, 
                            (handCameraControl.palmPosition().getZ() + 100) / podziel));
                    
                }
            }
            /*
             // Get the hand's sphere radius and palm position
             System.out.println("Hand sphere radius: " + hand.sphereRadius()
             + " mm, palm position: " + hand.palmPosition());

             // Get the hand's normal vector and direction
             Vector normal = hand.palmNormal();
             Vector direction = hand.direction();

             // Calculate the hand's pitch, roll, and yaw angles
             System.out.println("Hand pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
             */
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 4) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    System.out.println("Circle id: " + circle.id()
                            + ", " + circle.state()
                            + ", progress: " + circle.progress()
                            + ", radius: " + circle.radius()
                            + ", angle: " + Math.toDegrees(sweptAngle)
                            + ", " + clockwiseness);
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    System.out.println("Swipe id: " + swipe.id()
                            + ", " + swipe.state()
                            + ", position: " + swipe.position()
                            + ", direction: " + swipe.direction()
                            + ", speed: " + swipe.speed());
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    System.out.println("Screen Tap id: " + screenTap.id()
                            + ", " + screenTap.state()
                            + ", position: " + screenTap.position()
                            + ", direction: " + screenTap.direction());
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    System.out.println("Key Tap id: " + keyTap.id()
                            + ", " + keyTap.state()
                            + ", position: " + keyTap.position()
                            + ", direction: " + keyTap.direction());
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }

        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            System.out.println();
        }
    }
}
