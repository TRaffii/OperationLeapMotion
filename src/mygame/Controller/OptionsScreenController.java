/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import mygame.model.DatabaseManager;
import mygame.model.SettingsIO;
import mygame.model.Users;

/**
 *
 * @author Rafal
 */
public class OptionsScreenController extends AbstractAppState implements ScreenController {

    SimpleApplication app;
    DatabaseManager handler = new DatabaseManager();
    boolean musicPlay = false;
    Nifty nifty;
    java.util.Date effectStart;
    AudioNode music;

    public void bind(Nifty nifty, Screen screen) {
        //TODO MAKE AUDIO LIVE
        this.nifty = nifty;
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        //handler.insertUser("test", 40 , sqlDate);


    }

    public OptionsScreenController() {
    }

    public OptionsScreenController(SimpleApplication app, AudioNode music) {
        this.app = app;
        this.music = music;
    }

    public String getPlayers() {
        String returnValue = null;
        Collections.sort(handler.selectUsers(), new Comparator<Users>() {
            public int compare(Users o1, Users o2) {
                return o1.getResult() - o2.getResult();
            }
        });
        int i = 0;
        for (Users u : handler.selectUsers()) {

            if (i == 0) {
                returnValue = u.toString();
            } else {
                returnValue += u.toString();
            }
            i++;

        }
        return returnValue;
    }

    public void MusicOn() {
        if (activateButton()) {
            if (!musicPlay) {
                SettingsIO settingFile = new SettingsIO("assets/Settings/ProgramSettings.xml");
                try {
                    settingFile.Save("music", "on");
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(OptionsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                music = new AudioNode(app.getAssetManager(), "Sounds/HappyBee.wav", true);
                music.play();
                musicPlay = true;

            }
        }

    }

    public void BackToMainMenu() {
        if (activateButton()) {
            nifty.fromXml("Interface/screen.xml", "start");
        }
    }

    public void MusicOff() {
        if (activateButton()) {
            SettingsIO settingFile = new SettingsIO("assets/Settings/ProgramSettings.xml");
            try {
                settingFile.Save("music", "off");
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(OptionsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
//                AudioNode music = new AudioNode(app.getAssetManager(), "Sounds/HappyBee.wav", true);
//                app.getAudioRenderer().stopSource(music);
//                
//                music.stop();
            music.stop();
            musicPlay = false;
        }

    }

    public void handMovedOut() {
        effectStart = new java.util.Date();
    }

    private boolean activateButton() {
        long diff = new java.util.Date().getTime() - effectStart.getTime();
        if (diff > 2400) {
            return true;
        } else {
            return false;
        }
    }

    public void onStartScreen() {
        //Date newDate = new Date();
        //handler.insertUser("Testowy", 30,new Date());
    }

    public void onEndScreen() {
        handler.closeConnection();
    }
}
