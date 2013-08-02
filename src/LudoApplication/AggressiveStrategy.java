/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class AggressiveStrategy extends SimpleCPUStrategy{
    public AggressiveStrategy(Player owner)
    {
        super(owner);
    }
    
    @Override
    public String GetStrategyName()
    {
        return "Aggressive Strategy";
    }
    
    @Override
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.AGGRESSIVE;
    }
    
    @Override
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        Pawn result = null;
        
        // Check for an opportunity to bump another pawn.
        for (Pawn p : available)
        {
            Field curSquare = p.GetSquare();
            Field nextSquare = curSquare.GetNextForPlayer(owner, valueRolled);
            Pawn nextPawn = (nextSquare != null ? nextSquare.GetOccupant() : null);
            
            if(nextPawn != null && nextPawn.GetPlayerId() != ownerId)
                result = p;
        }
        
        // Revert to the simple CPU strategy if no opportunities are available.
        if (result == null)
            result = super.ChoosePawn(available, all, valueRolled);
        
        return result;
    }
}
