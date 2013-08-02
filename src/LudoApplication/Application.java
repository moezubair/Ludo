/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

/**
 *
 * @author Jason
 */
public class Application {
    public static Board gameboard;
    public static LudoFrame boardView;
    
    // Debug switches.
    public static final boolean SPEED_DIE = false; // Doubles chance to roll a 6.
    public static final boolean DISABLE_DIE_ANIMATION = false;
    public static final boolean DISABLE_PAWN_ANIMATION = false;
    
    // Setting this < 25 causes graphical errors when pawn animations are enabled.
    public static final int GAME_UPDATE_INTERVAL = 100;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        gameboard = new Board();
        boardView = new LudoFrame();
        
        boardView.setVisible(true);
    }
}
