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
    protected Player owner = null;
    protected int ownerId = -1;
    protected Board gameboard = null;
    
    public Strategy(Player owner)
    {
        this.owner = owner;
        this.ownerId = owner.GetPlayerId();
        this.gameboard = Application.gameboard;
    }
    
    public abstract String GetStrategyName();
    
    public abstract Pawn ChoosePawn();
}
