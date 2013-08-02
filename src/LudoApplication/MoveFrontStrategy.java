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
public class MoveFrontStrategy extends SimpleCPUStrategy{

    public MoveFrontStrategy(Player owner)
    {
        super(owner);
    }
    
    public String GetStrategyName()
    {
        return "Move-front Strategy";
    }
    
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.MOVE_FRONT;
    }
    
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        int frontmost = 0;
        for (int i = 0; i < available.length; i++)
            if (available[i].GetDistance() > available[frontmost].GetDistance())
                frontmost = i;
        
        return available[frontmost];
    }
}
