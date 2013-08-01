/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class HumanStrategy extends Strategy{
    
    public HumanStrategy(Player owner)
    {
        super(owner);
    }
    
    public String GetStrategyName()
    {
        return "Human Strategy";
    }
    
    public Board.PTYPE GetStrategyType()
    {
        return Board.PTYPE.HUMAN;
    }
    
    public Pawn ChoosePawn(Pawn[] available, Pawn[][] all, int valueRolled)
    {
        return Application.gameboard.GetSelectedPawn();
    }
}
