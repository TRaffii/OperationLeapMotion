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
enum SwipeDirection
    {
        Up,
        Down,
        Left,
        Right
    }
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
        Main.setCameraRotationRPY(new Vector3f( FastMath.PI+0.4f, 0, FastMath.PI));
        Main.setCameraPositionXYZ(new Vector3f(0, 2f, 12f));
        controller.config().setFloat("Gesture.Swipe.MinLength", 10);
        controller.config().setFloat("Gesture.Swipe.MinVelocity", 100);
        controller.config().save();
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
//        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
//        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
//        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
       
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
                 if(handCameraControl.fingers().count()>3)
                 {
                     int podziel = 30;
                     Main.setCameraPositionXYZ(new Vector3f(0, handCameraControl.palmPosition().getY() / podziel, 
                            ( handCameraControl.palmPosition().getZ()+100)/podziel ));
                     GestureList gestures = frame.gestures();
                     for (Gesture gst: gestures)
                     {
                         SwipeGesture swipe = new SwipeGesture(gst);
                         if (Math.abs(swipe.direction().getX()) > Math.abs(swipe.direction().getY()))
                         {
                            if (swipe.direction().getX() > 0) // right swipe
                            {
                                System.out.println("Right swpie");
                                float xRotation = Main.getCameraRotationRPY().x;
                                float yRotation = Main.getCameraRotationRPY().y +0.01f;
                                Main.setCameraRotationRPY(new Vector3f( xRotation, yRotation, FastMath.PI));
                      //SwipeAction(fingers, SwipeDirection.Right);
                            }
                            else // left swipe
                            {
                                System.out.println("Left swpie");
                                float xRotation = Main.getCameraRotationRPY().x;
                                float yRotation = Main.getCameraRotationRPY().y -0.01f;
                                Main.setCameraRotationRPY(new Vector3f( xRotation, yRotation, FastMath.PI));
                  
                                //SwipeAction(fingers, SwipeDirection.Left);
                            }
                         }
                         else // Vertical swipe
                        {
                            if (swipe.direction().getY() > 0) // upward swipe
                            {
                                float xRotation = Main.getCameraRotationRPY().x -0.005f;
                                float yRotation = Main.getCameraRotationRPY().y;
                                Main.setCameraRotationRPY(new Vector3f( xRotation, yRotation, FastMath.PI));
                            }
                            else // downward swipe
                            {
                                float xRotation = Main.getCameraRotationRPY().x +0.005f;
                                float yRotation = Main.getCameraRotationRPY().y;
                                Main.setCameraRotationRPY(new Vector3f( xRotation, yRotation, FastMath.PI));
                            }
                        }
                     }
                }
                
                    
//                Hand handCameraControl = frame.hands().leftmost();
////                System.out.println("YAXIS"+(handCameraControl.palmPosition().getX()/50)%6.28);
//                if (handCameraControl.fingers().count() > 2) {
                    
//                    System.out.println("ROLL:" + (handCameraControl.direction().roll()));
//                    System.out.println("PITCH:" + (handCameraControl.palmPosition().pitch()));
//                    System.out.println("Z:" + (handCameraControl.palmPosition().getZ()));
                      
//                    
////                        Main.setCameraRotationRPY(new Vector3f((float) (-(handCameraControl.palmPosition().getY()/40)%6.28), (float) -((handCameraControl.palmPosition().getX()/40)%6.28), 0));
////                    if(Math.abs((float) Main.getCameraRotationRPY().x - ((handCameraControl.palmPosition().getX()/50)%6.28)) > 0.2f)
////                    {
//                        Main.setCameraRotationRPY(new Vector3f( (float) (FastMath.HALF_PI-(handCameraControl.palmPosition().getY()/40)%6.28), (float) ((float)  ((handCameraControl.palmPosition().getX()/50)%6.28)), FastMath.PI));
////                    }
//
//                   if(Math.abs(Main.getCameraPositionXYZ().z - handCameraControl.palmPosition().getZ())>10)
//                   {
//                       Main.setCameraPositionXYZ(new Vector3f(handCameraControl.palmPosition().getX() / podziel, handCameraControl.palmPosition().getY() / podziel, 
//                            ( handCameraControl.palmPosition().getZ()) ));
//                   }
//                    
//                   
//                    Main.setCameraPositionXYZ(new Vector3f(0, 0, 
//                            (handCameraControl.palmPosition().getZ() + 100) / podziel));
//                    
//                }
//            }
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
    }
}
}
