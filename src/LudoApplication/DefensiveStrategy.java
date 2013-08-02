/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import java.util.ArrayList;

/**
 *
 * @author Jason
 */
public class DefensiveStrategy extends SimpleCPUStrategy{
    public DefensiveStrategy(Player owner)
    {
        super(owner);
    }
    
    @Override
    public String GetStrategyName()
    {
        return "Defensive Strategy";
    }
    
    @Override
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.DEFENSIVE;
    }
    
    @Override
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        int[] vBefore = new int[gameboard.PLAYERPAWNS];
        int[] vAfter = new int[gameboard.PLAYERPAWNS];
        Field[] next = new Field[gameboard.PLAYERPAWNS];
        
        // Determine each square that each available pawn can move to.
        for (Pawn p : available)
            next[p.GetPawnId()] = p.GetSquare().GetNextForPlayer(owner, valueRolled);
        
        // Calculate the immediate and after moving vulnerability of each pawn.
        for (int i = 0; i < all.length; i++)
        {
            if(i == ownerId)
                continue;
            
            for (Pawn p : all[i])
            {
                Field square = p.GetSquare();
                int maxDistance = gameboard.GetDie().GetSides();
                
                // A pawn at home can only move a distance of 1.
                if (square instanceof Home)
                    maxDistance = 1;

                // Check all squares ahead of the current pawn.
                for (int j = 0; j < maxDistance && square != null; j++)
                {
                    Pawn occupant = square.GetOccupant();
                    
                    // Check if this pawn threatens any of the owner's pawns.
                    if (occupant != null && occupant.GetPlayerId() == ownerId)
                        vBefore[occupant.GetPawnId()]++;
                
                    // Check if this pawn threatens any post-move squares.
                    for (int k = 0; k < next.length; k++)
                        if (next[k] == square)
                            vAfter[k]++;
                    
                    square = square.GetNext();
                }
            }
        }
        
            
        // Determine the pawns with the largest vulnerability difference
        // before and after moving.
        int mvDif = -99; // Arbitrarily low number to be below all differences.
        ArrayList<Pawn> result = new ArrayList<Pawn>();
        for (Pawn p : available)
        {
            int vDif = vBefore[p.GetPawnId()] - vAfter[p.GetPawnId()];
            
            if (vDif > mvDif)
            {
                mvDif = vDif;
                result.clear();
            }
            
            if (vDif == mvDif)
                result.add(p);
        }
        
        // Choose which pawn to move from the endangered pawns.
        return super.ChoosePawn(result.toArray(new Pawn[0]), all, valueRolled);
    }
}
