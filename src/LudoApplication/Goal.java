/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Goal extends Field{
    private Player belongsTo;
    
    private Field nextGoal;
    
    public Goal(int index, Player owner)
    {
        super(index);
        
        this.belongsTo  = owner;
    }
    
    public void SetNextGoal(Field next)
    {
        this.nextGoal = next;
    }
    
    public Field GetNextGoal()
    {
        return this.nextGoal;
    }
    
    public Player GetOwner()
    {
        return this.belongsTo;
    }
}
