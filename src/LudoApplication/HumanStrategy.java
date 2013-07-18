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
    
    public Pawn ChoosePawn()
    {
        return Application.gameBoard.GetSelectedPawn();
    }
}
