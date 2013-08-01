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
public class SimpleStrategy extends Strategy{
    
    private Random r = null;
    
    public SimpleStrategy(Player owner)
    {
        super(owner);
        
        r = new Random();
    }
    
    public String GetStrategyName()
    {
        return "Simple Strategy";
    }
    
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.SIMPLE;
    }
    
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        Pawn result = null;
        for (Pawn p : available)
            if (p.GetDistance() == 0)
            {
                result = p;
                break;
            }
        
        if (result == null)
            result = available[r.nextInt(available.length)];
        
        return result;
    }
}
