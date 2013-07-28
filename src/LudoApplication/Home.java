/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 * Home.java
 * @author Jason Lib
 * Home class which extends Field
 */
public class Home extends Field{
    public Home(int id)
    {
        super(id);
    }
    
    @Override
    public Field GetNextForPlayer(Player p, int distance)
    {
        return next;
    }
}
