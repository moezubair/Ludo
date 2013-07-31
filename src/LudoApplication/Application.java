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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        gameboard = new Board();
        boardView = new LudoFrame();
        
        boardView.setVisible(true);
    }
}
