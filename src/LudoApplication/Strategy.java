/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public abstract class Strategy {
    protected Player owner;
    
    public Strategy(Player owner)
    {
        this.owner = owner;
    }
    
    public abstract String GetStrategyName();
    
    public abstract Pawn ChoosePawn();
}
