/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Player {
    private String name = null;
    
    private Strategy strategy = null;
    
    public Player(String name)
    {
        this.name = name;
    }
    
    public void SetName(String name)
    {
        this.name = name;
    }
    
    public String GetName()
    {
        return this.name;
    }
    
    public void SetStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }
    
    public Strategy GetStrategy()
    {
        return this.strategy;
    }
    
    public boolean IsHumanPlayer()
    {
        return strategy == null;
    }
}
