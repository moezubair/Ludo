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
    
    private int distanceTravelled = 0;
    
    public Pawn(int pawnId, Player owner, Home home)
    {
        this.pawnId = pawnId;
        this.owner = owner;
        this.home = home;
        this.isOn = home;
    }
    
    public int GetPawnId()
    {
        return pawnId;
    }
    
    public int GetPlayerId()
    {
        return owner.GetPlayerId();
    }
    
    public boolean GetIsHome()
    {
        return isOn instanceof Home;
    }
    
    public boolean GetIsAtGoal()
    {
        return isOn instanceof Goal;
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
    
    public Field GetSquare()
    {
        return isOn;
    }
    
    public void AddDistance(int distance)
    {
        distanceTravelled += distance;
    }
    
    public int GetDistance()
    {
        return distanceTravelled;
    }
    
    public void ReturnHome()
    {
        distanceTravelled = 0;
        SetSquare(home);
    }
}
