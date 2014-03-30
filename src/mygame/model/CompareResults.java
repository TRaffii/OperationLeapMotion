/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.model;

import java.util.Comparator;

/**
 *
 * @author Rafal
 */
public class CompareResults implements Comparator<Users>{

    public int compare(Users o1, Users o2) {
        if(o1.getResult() > o2.getResult())
            return o1.getResult();
        else
            return o2.getResult();
    }
    
}
