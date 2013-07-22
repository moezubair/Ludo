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
    private int id;
    private Field next;
    
    private Pawn occupant;    
    
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
