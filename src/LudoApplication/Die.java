/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import java.util.Random;

/**
 *
 * @author Jason
 */
public class Die {
    private static Random r;
    
    private int sides;
    private int faceValue;
    
    public Die(int sides)
    {
        this.sides = sides;
        this.faceValue = 1;
    }
    
    public void Roll()
    {
        if (r == null)
            r = new Random();
        
        faceValue  = r.nextInt(sides) + 1;
    }
    
    public int GetFaceValue()
    {
        return this.faceValue;
    }
    
    public int GetSides()
    {
        return this.sides;
    }
}
