/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LudoApplication;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Jason
 */
public class Board {
    // Game objects.
    private Player[] players;
    
    private Pawn[][] pawns;
    
    private Field[] exteriorSquares;
    private Field[][] goalSquares;
    
    private Die die = null;
    
    
    private int playerTurn = 0;
    private boolean turnEnded = false;
    
    private PHASE playerPhase = PHASE.NONE;
    private boolean phaseEnded = false;
    
    private int movesTaken = 0;
   
    
    private Player actingPlayer = null;
    private boolean paused = false;
    private boolean pausedForInput = false;
    private Pawn selectedPawn = null;
    
    
    private Timer gameTimer = null;
    private TimerTask gameTask = null;
    private int gameUpdateInterval = 100;
    
    // Event listeners.
    private ArrayList<ActionListener> gameEventListeners = null;
            
    
    public final int MAXPLAYERS = 4;
    public final int PLAYERPAWNS = 4;
    public final int EXTERIORSQUARES = 40;
    public final int PLAYERDISTANCE = 10;
    public final int INTERIORSQUARES = 4;
    
    public Board() throws Exception
    {
        initiate();
    }
    
    public void initiate() throws Exception
    {
        // Initialize players.
        
        // As there are elements such as board squares which are associated
        // with a player that persist even if that player is not participating,
        // we create all 4 players regardless of the actual number of players.
        players = new Player[MAXPLAYERS];
        for (int i = 0; i < players.length; i++)
            players[i] = new Player("Player " + String.valueOf(i + 1));
        
        // Initialize board squares.
        // Board squares are given a unique index to allow them to be
        // differentiated when displaying them in the view.
        exteriorSquares = new Field[EXTERIORSQUARES];
        
        // Create the main exterior ssquares.
        for (int i = 0; i < exteriorSquares.length; i++)
        {
            if (i % PLAYERDISTANCE == 0)
                exteriorSquares[i] = new Goal(i, players[i / PLAYERDISTANCE]);
            else if ((i - 1) % PLAYERDISTANCE == 0)
                exteriorSquares[i] = new Entry(i, players[i / PLAYERDISTANCE]);
            else
                exteriorSquares[i] = new Field(i);
            
            if (i > 0)
                exteriorSquares[i - 1].SetNext(exteriorSquares[i]);
            
            if (i == exteriorSquares.length - 1)
                exteriorSquares[i].SetNext(exteriorSquares[0]);
        }
        
        // Create the interior squares. These squares are treated as regular
        // squares that a pawn needs to traverse after reaching the player's
        // exterior goal square.
        goalSquares = new Field[MAXPLAYERS][];
        for (int i = 0 ; i < goalSquares.length; i++)
        {
            goalSquares[i] = new Field[INTERIORSQUARES];
            for (int j = 0; j < goalSquares[i].length; j++)
            {
                int index = EXTERIORSQUARES + (i * INTERIORSQUARES) + j;
                
                goalSquares[i][j] = new Field(index);
                
                if (j > 0)
                    goalSquares[i][j - 1].SetNext(goalSquares[i][j]);
            }
        }
        
        // Create each player's pawns.
        pawns = new Pawn[MAXPLAYERS][];
        for (int i = 0; i < pawns.length; i++)
        {
            pawns[i] = new Pawn[PLAYERPAWNS];
            for (int j = 0; j < pawns[i].length; j++)
                pawns[i][j] = new Pawn(players[i]);
        }
        
        // Create a new 6 sided die.
        die = new Die(6);
        
        gameUpdateInterval = 100;
        gameTask = new TimerTask() { public void run() { AdvanceGame(); } };
        gameTimer = new Timer();
        gameTimer.schedule(gameTask, 0, gameUpdateInterval);
        
        this.gameEventListeners = new ArrayList<ActionListener>();
    }
    
     private void AdvanceGame()
    {
        if (paused || pausedForInput)
            return;
        
        if (turnEnded)
            AdvanceTurn();
        else if (phaseEnded)
            AdvancePhase();
        else
            DoPhase();
    }
    
    // Game Operations
    
    public void StartGame()
    {
        SetPlayerTurn(0);
    }
    
    public void PauseGame()
    {
        paused = true;
    }
    
    public void UnpauseGame()
    {
        paused = false;
    }
    
    public void AdvanceTurn()
    {
        int next = (playerTurn + 1) % MAXPLAYERS;
        
        SetPlayerTurn(next);
        
    }
    
    public void EndTurn()
    {
        turnEnded = true;
        
        OnTurnEnd();
    }
    
    public void AdvancePhase()
    {
        switch(playerPhase)
        {
            case NONE: SetPlayerPhase(PHASE.STARTTURN); break;
            case STARTTURN: SetPlayerPhase(PHASE.ROLLDIE); break;
            case ROLLDIE: SetPlayerPhase(PHASE.MOVEPAWNS); break;
            case MOVEPAWNS: SetPlayerPhase(PHASE.ENDTURN); break;
            case ENDTURN: SetPlayerPhase(PHASE.NONE); break;
        }
    }
        
    public void EndPhase()
    {
        phaseEnded = true;
        
        OnPhaseEnd();
    }
    
    public void DoPhase()
    {
        switch(playerPhase)
        {
            case STARTTURN: DoStartTurnPhase(); break;
            case ROLLDIE: DoRollDicePhase(); break;
            case MOVEPAWNS: DoMovePawnsPhase(); break;
            case ENDTURN: DoEndTurnPhase(); break;
        }
    }
    
    public void DoStartTurnPhase()
    {
        if (playerPhase != PHASE.STARTTURN)
        {
            // Log error.
            return;
        }
        
        // Non-action phase.
        
        EndPhase();
    }
    
    public void DoRollDicePhase()
    {
        if (playerPhase != PHASE.ROLLDIE)
        {
            // Log error.
            return;
        }
        
        die.Roll();
        
        OnDieRolled();
        
        EndPhase();
    }
    
    public void DoMovePawnsPhase()
    {
        if (playerPhase != PHASE.MOVEPAWNS)
        {
            // Log error.
            return;
        }
        
        
        Pawn pawn = actingPlayer.GetStrategy().ChoosePawn();

        pawn.Advance();
        movesTaken++;

        OnPawnMoved();
        
        if (movesTaken >= die.GetFaceValue())
            EndPhase();
    }
    
    public void DoEndTurnPhase()
    {
        if (playerPhase != PHASE.ENDTURN)
        {
            // Log error.
            return;
        }
        
        EndTurn();
        
        EndPhase();
    }
    
    // Events
    public void AddGameEventListener(ActionListener event)
    {
        gameEventListeners.add(event);
    }
    
    public void RemoveGameEventListener(ActionListener event)
    {
        gameEventListeners.remove(event);
    }
    
    public void OnTurnStart()
    {
        ActionEvent ae = new ActionEvent(this, 0, "TURNSTART");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnTurnEnd()
    {
        ActionEvent ae = new ActionEvent(this, 0, "TURNEND");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnPhaseStart()
    {
        if (actingPlayer.IsHumanPlayer())
        {
            if(playerPhase == PHASE.ROLLDIE
            || playerPhase == PHASE.MOVEPAWNS)
                pausedForInput = true;
        }
        
        ActionEvent ae = new ActionEvent(this, 0, "PHASESTART");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnPhaseEnd()
    {   
        if (actingPlayer.IsHumanPlayer())
        {
            if (playerPhase == PHASE.ROLLDIE
             || playerPhase == PHASE.MOVEPAWNS)
                pausedForInput = false;
        }
        
        ActionEvent ae = new ActionEvent(this, 0, "PHASEEND");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnDieRolled()
    {
        ActionEvent ae = new ActionEvent(this, 0, "DIEROLLED");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnPawnMoved()
    {
        ActionEvent ae = new ActionEvent(this, 0, "PAWNMOVED");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    
    
    // Accessors
    
    public Die GetDie()
    {
        return die;
    }
    
    public void SetSelectedPawn(Pawn selected)
    {
        this.selectedPawn = selected;
    }
    
    public Pawn GetSelectedPawn()
    {
        return selectedPawn;
    }
    
    public void SetPlayerTurn(int player)
    {
        playerTurn = player;
        actingPlayer = players[player];
        
        movesTaken = 0;
        turnEnded = false;
        SetPlayerPhase(PHASE.STARTTURN);
        
        OnTurnStart();
    }
    
    public int GetPlayerTurn()
    {
        return this.playerTurn;
    }
    
    public Player GetActingPlayer()
    {
        return this.actingPlayer;
    }
    
    public void SetPlayerPhase(PHASE phase)
    {
        this.playerPhase = phase;
        phaseEnded = false;
        
        OnPhaseStart();
    }
    
    public PHASE GetPlayerPhase()
    {
        return this.playerPhase;
    }
    
    public enum PHASE
    {
        NONE,
        STARTTURN,
        ROLLDIE,
        MOVEPAWNS,
        ENDTURN
    }
}
