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
    private Field next;
    private int index;
    
    public Field(int index)
    {
        this.index = index;
    }
    
    public void SetNext(Field next)
    {
        this.next = next;
    }
    
    public Field GetNext()
    {
        return this.next;
    }
}
