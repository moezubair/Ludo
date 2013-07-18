/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class RandomStrategy extends Strategy{
    
    public RandomStrategy(Player owner)
    {
        super(owner);
    }
    
    public String GetStrategyName()
    {
        return "Random Strategy";
    }
    
    public Pawn ChoosePawn()
    {
        // Not implemented.
        return null;
    }
}
