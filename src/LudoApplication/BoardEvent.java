/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import java.awt.event.*;

/**
 *
 * @author Jason
 */
public class BoardEvent extends ActionEvent {
    private Object tags = null;
    
    public BoardEvent(Object source, Object tags, int id, String command)
    {
        super(source, id, command);
        
        this.tags = tags;
    }
    
    public BoardEvent(Object source, Object tags,
            int id, String command, int modifiers)
    {
        super(source, id, command, modifiers);
    }   
    
    public BoardEvent(Object source, Object tags, int id, String command, 
            long when, int modifiers)
    {
        super(source, id, command, when, modifiers);
    }   
    
    public void setTags(Object tags)
    {
        this.tags = tags;
    }
    
    public Object getTags()
    {
        return this.tags;
    }
}
