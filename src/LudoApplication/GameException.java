/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class GameException extends Exception{
    private TYPE type = TYPE.UNKNOWN;
    
    public GameException(TYPE type)
    {
        super();
        
        this.type = type;
    }
    
    public GameException(TYPE type, String message)
    {
        super(message);
        
        this.type = type;
    }
    
    public TYPE getType()
    {
        return type;
    }
    
    public enum TYPE
    {
        UNKNOWN,
        PHASE,
        PAWN_NOT_OWNED,
        PAWN_BLOCKED,
        PAWN_AT_HOME
    }
}
