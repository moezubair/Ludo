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
public class MoveBackStrategy extends SimpleCPUStrategy{
    
    public MoveBackStrategy(Player owner)
    {
        super(owner);
    }
    
    public String GetStrategyName()
    {
        return "Move-back Strategy";
    }
    
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.MOVE_BACK;
    }
    
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        int backmost = 0;
        for (int i = 0; i < available.length; i++)
            if (available[i].GetDistance() < available[backmost].GetDistance())
                backmost = i;
        
        return available[backmost];
    }
}
