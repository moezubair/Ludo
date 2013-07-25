/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 * Pawn.java
 * @author Jason Lib
 * 
 */
public class Pawn {
    private int pawnId = -1;
    private Player owner = null;
    private Home home = null;
    private Field isOn = null;
    
    private boolean reachedGoal = false;
    
    public Pawn(int pawnId, Player owner, Home home)
    {
        this.pawnId = pawnId;
        this.owner = owner;
        this.home = home;
        this.isOn = home;
    }
    
    public void Advance()
    {
        if (isOn == null)
            return;
        
        if(isOn instanceof Goal && ((Goal)isOn).GetOwner() == owner)
            SetSquare(((Goal)isOn).GetNextGoal());
        else
            SetSquare(isOn.GetNext());
    }
    
    public int GetPawnId()
    {
        return pawnId;
    }
    
    public int GetPlayerId()
    {
        return owner.GetPlayerId();
    }
    
    public void SetSquare(Field value)
    {
        if (isOn == value)
            return;
        
        Field old = isOn;
        isOn = value;
        
        if (old != null && old.GetOccupant() == this)
            old.SetOccupant(null);
        
        if (isOn != null)
            isOn.SetOccupant(this);
    }
    
    public void ReturnHome()
    {
        SetSquare(home);
    }
    
    public Field GetSquare()
    {
        return isOn;
    }
}
