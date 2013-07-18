/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Entry extends Field {
    private Player belongsTo;
    
    public Entry(int index, Player owner)
    {
        super(index);
        
        this.belongsTo = owner;
    }
    
}
