/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Field {
    protected int id;
    protected Field next;
    
    protected Pawn occupant;    
    
    public Field(int id)
    {
        this.id = id;
    }
    
    public int GetId()
    {
        return id;
    }
    
    public void SetNext(Field value)
    {
        next = value;
    }
    
    public Field GetNext()
    {
        return next;
    }
    
    public Field GetNextForPlayer(Player p, int distance)
    {
        if (distance == 0)
            return this;
        
        if (next != null)
            return next.GetNextForPlayer(p, distance - 1);
                    
        return null;
    }
   
    public void SetOccupant(Pawn value)
    {
        if (occupant == value)
            return;
        
        Pawn old = occupant;
        occupant = value;
        
        if (old != null && old.GetSquare() == this)
            old.ReturnHome();
        
        if (occupant != null)
            occupant.SetSquare(this);
    }
    
    public Pawn GetOccupant()
    {
        return occupant;
    }
}
