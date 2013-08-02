/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Junction extends Field{
    private Player belongsTo;
    
    private Goal nextGoal;
    
    public Junction(int index, Player owner)
    {
        super(index);
        
        this.belongsTo  = owner;
    }
    
    public void SetNextGoal(Goal next)
    {
        this.nextGoal = next;
    }
    
    public Goal GetNextGoal()
    {
        return this.nextGoal;
    }
    
    @Override
    public Field GetNextForPlayer(Player p, int distance)
    {
        if (distance == 0)
            return this;
        
        // Switch to goal junction if this junction belongs to the player.
        if (belongsTo == p && nextGoal != null)
            return nextGoal.GetNextForPlayer(p, distance - 1);
        
        if (next != null)
            return next.GetNextForPlayer(p, distance - 1);
        
        return null;
    }
    
    public Player GetOwner()
    {
        return this.belongsTo;
    }
}
