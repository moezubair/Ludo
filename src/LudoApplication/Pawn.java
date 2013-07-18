/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Pawn {
    private Player owner = null;
    private Field isOn = null;
    
    private boolean reachedGoal = false;
    
    public Pawn(Player owner)
    {
        this.owner = owner;
        this.isOn = null;
    }
    
    public void Advance()
    {
        Field next = null;
        
        if (isOn == null)
            ;// next = owner.GetEntrySquare();
        else if(isOn instanceof Goal && ((Goal)isOn).GetOwner() == owner)
            next = ((Goal)isOn).GetNextGoal();
        else
            next = isOn.GetNext();
        
        
        
        SetSquare(isOn.GetNext());
    }
    
    public void SetSquare(Field square)
    {
        this.isOn = square;
    }
    
    public Field GetSquare()
    {
        return this.isOn;
    }
}
