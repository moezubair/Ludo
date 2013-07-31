/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import java.util.Random;

/**
 *
 * @author Jason
 */
public class RandomStrategy extends Strategy{
    
    private Random r = null;
    
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
        Pawn[] pawns = gameboard.GetAvailablePawns(ownerId);
        int p = r.nextInt(pawns.length);
        
        return pawns[p];
    }
}
