/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 * Entry.java
 * @author Jason Lib
 * This file implements the Entry field
 */
public class Entry extends Field {
    private Player belongsTo;
    
    public Entry(int index, Player owner)
    {
        super(index);
        
        this.belongsTo = owner;
    }
    
}
