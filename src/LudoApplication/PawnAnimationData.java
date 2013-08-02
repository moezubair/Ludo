/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Jason
 */
public class PawnAnimationData {
    public PawnAnimationData(JLabel pawn, Point endPoint)
    {
        this.pawn = pawn;
        this.endPoint = endPoint;
    }
    
    public JLabel pawn;
    public Point endPoint;
}
