/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import mygame.model.DatabaseManager;
import mygame.model.Users;

/**
 *
 * @author Rafal
 */
public class OptionsScreenController extends AbstractAppState implements ScreenController{
    DatabaseManager handler = new DatabaseManager();
    public void bind(Nifty nifty, Screen screen) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        //handler.insertUser("test", 40 , sqlDate);
        
        
     }
    public String getPlayers(){
        String returnValue = null;
        Collections.sort(handler.selectUsers(), new Comparator<Users>(){
        public int compare(Users o1, Users o2){
           return o1.getResult()- o2.getResult();
        }
     });
        int i = 0;
        for(Users u:handler.selectUsers())
        {
            
            if(i == 0)
            {
                returnValue = u.toString();
            }
            else
            {
                 returnValue += u.toString();
            }
            i++;
            
        }
        return returnValue; 
   }
    public void onStartScreen() {
        
        //Date newDate = new Date();
        //handler.insertUser("Testowy", 30,new Date());
       }

    public void onEndScreen() {
        handler.closeConnection();
     }
    
}
