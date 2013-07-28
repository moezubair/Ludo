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
    private Goal[][] goalSquares;
    private Home[][] homeSquares;
    
    private Die die = null;
    
    
    private int playerTurn = 0;
    private boolean turnEnded = false;
    
    private PHASE playerPhase = PHASE.NONE;
    private boolean phaseEnded = false;

    private int valueRolled = 0;
   
    
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
    
    private final String PAWN_AT_HOME = "A pawn may only be moved into play if a six is rolled.";
    private final String PAWN_NOT_OWNED = "Only pawns owned by the acting player may be moved.";
    private final String PAWN_BLOCKED = "Pawns cannot be moved if they are blocked by pawns of the same player.";
    
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
            players[i] = new Player(i, "Player " + String.valueOf(i + 1));
        
        // Initialize board squares.
        // Board squares are given a unique index to allow them to be
        // differentiated when displaying them in the view.
        exteriorSquares = new Field[EXTERIORSQUARES];
        
        // Create the main exterior ssquares.
        for (int i = 0; i < exteriorSquares.length; i++)
        {
            if (i % PLAYERDISTANCE == 0)
                exteriorSquares[i] = new Junction(i, players[i / PLAYERDISTANCE]);
            else if (i % PLAYERDISTANCE == 1)
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
        // exterior junction square.
        goalSquares = new Goal[MAXPLAYERS][];
        for (int i = 0 ; i < goalSquares.length; i++)
        {
            goalSquares[i] = new Goal[INTERIORSQUARES];
            for (int j = 0; j < goalSquares[i].length; j++)
            {
                int id = EXTERIORSQUARES + (i * INTERIORSQUARES) + j;
                
                goalSquares[i][j] = new Goal(id);
                
                if (j == 0)
                {
                    Junction junction = (Junction)exteriorSquares[i * PLAYERDISTANCE];
                    junction.SetNextGoal(goalSquares[i][j]);
                }
                
                if (j > 0)
                    goalSquares[i][j - 1].SetNext(goalSquares[i][j]);
            }
        }
        
        // Create the home squares. These are placeholder squares that each
        // pawn rests on before being brought into play. This is to reduce
        // the amount of testing for null squares required.
        homeSquares = new Home[MAXPLAYERS][];
        for (int i = 0; i < homeSquares.length; i++)
        {
            homeSquares[i] = new Home[PLAYERPAWNS];
            for (int j = 0; j < homeSquares[i].length; j++)
            {
                int id = 0;
                id += EXTERIORSQUARES;
                id += MAXPLAYERS * INTERIORSQUARES;
                id += (i * PLAYERPAWNS) + j;
                
                homeSquares[i][j] = new Home(id);
                homeSquares[i][j].SetNext(exteriorSquares[i * PLAYERDISTANCE + 1]);
            }
        
        }
        
        // Create each player's pawns.
        pawns = new Pawn[MAXPLAYERS][];
        for (int i = 0; i < pawns.length; i++)
        {
            pawns[i] = new Pawn[PLAYERPAWNS];
            for (int j = 0; j < pawns[i].length; j++)
                pawns[i][j] = new Pawn(j, players[i], homeSquares[i][j]);
        }
        
        // Create a new 6 sided die.
        die = new Die(6);
        
        // Setup the game update loop.
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
        
        try
        {
            if (turnEnded)
                AdvanceTurn();
            else if (phaseEnded)
                AdvancePhase();
            else
                DoPhase();
        }
        catch (GameException e)
        {
            System.out.println("GameException thrown in DoPhase function... This should never happen.");
            System.out.println(String.valueOf(e.getType()));
            System.out.println(e.getMessage());
        }
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
    
    public void DoPhase() throws GameException
    {
        switch(playerPhase)
        {
            case STARTTURN: DoStartTurnPhase(); break;
            case ROLLDIE: DoRollDicePhase(); break;
            case MOVEPAWNS: DoMovePawnsPhase(); break;
            case ENDTURN: DoEndTurnPhase(); break;
        }
    }
    
    public void DoStartTurnPhase() throws GameException
    {
        if (playerPhase != PHASE.STARTTURN)
        {
            String msg = "Invalid phase: " + String.valueOf(playerPhase);
            
            throw new GameException(GameException.TYPE.PHASE, msg);
        }
        
        // Non-action phase.
        
        EndPhase();
    }
    
    public void DoRollDicePhase() throws GameException
    {
        if (playerPhase != PHASE.ROLLDIE)
        {
            String msg = "Invalid phase: " + String.valueOf(playerPhase);
            
            throw new GameException(GameException.TYPE.PHASE, msg);
        }
        
        die.Roll();
        
        valueRolled = die.GetFaceValue();
        
        OnDieRolled();
        
        EndPhase();
    }
    
    public void DoMovePawnsPhase() throws GameException
    {
        if (playerPhase != PHASE.MOVEPAWNS)
        {
            String msg = "Invalid phase: " + String.valueOf(playerPhase);
            
            throw new GameException(GameException.TYPE.PHASE, msg);
        }
        
        
        //Pawn pawn = actingPlayer.GetStrategy().ChoosePawn();
        Pawn pawn = this.GetSelectedPawn();
        Field curSquare = pawn.GetSquare();
        Field nextSquare = curSquare.GetNextForPlayer(actingPlayer, valueRolled);
        Pawn next = (nextSquare != null ? nextSquare.GetOccupant() : null);
        
        // Only allow pawns owned by the acting player to be moved.
        if (pawn.GetPlayerId() != actingPlayer.GetPlayerId())
            throw new GameException(GameException.TYPE.PAWN_NOT_OWNED, PAWN_NOT_OWNED);
        
        // Only allow pawns to be moved onto the board if a six was rolled.
        if (curSquare instanceof Home && valueRolled < 6)
            throw new GameException(GameException.TYPE.PAWN_AT_HOME, PAWN_AT_HOME);
        
        // Only allow pawns that are not blocked to be moved.
        if (next != null && next.GetPlayerId() == actingPlayer.GetPlayerId())
            throw new GameException(GameException.TYPE.PAWN_BLOCKED, PAWN_BLOCKED);
        
        // Restrict pawns from moving past the end of their goal section.
        if (nextSquare == null)
            throw new GameException(GameException.TYPE.EXCEEDS_GOAL);
        
        pawn.SetSquare(nextSquare);
        
        // Update the distance travelled for a.i. purposes.
        if (curSquare instanceof Home)
            pawn.addDistance(1);
        else
            pawn.addDistance(valueRolled);

        // Event trigger for the "bumped" pawn.
        if (next != null)
            OnPawnMoved(next);
        
        // Event trigger for the moved pawn.
        OnPawnMoved(pawn);
        
        // Allow the player to roll again if they rolled a 6.
        if (valueRolled == 6 && !GetGameWon())
            SetPlayerPhase(PHASE.ROLLDIE);
        else
            EndPhase();
    }
    
    public void DoEndTurnPhase() throws GameException
    {
        if (playerPhase != PHASE.ENDTURN)
        {
            String msg = "Invalid phase: " + String.valueOf(playerPhase);
            
            throw new GameException(GameException.TYPE.PHASE, msg);
        }
        
        if (GetGameWon())
        {
            SetPlayerPhase(PHASE.GAMEWON);
            
            OnGameWon(actingPlayer);
        }
        else
        {
            EndTurn();

            EndPhase();
        }
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
        ActionEvent ae = new BoardEvent(this, actingPlayer, 0, "TURNSTART");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnTurnEnd()
    {
        ActionEvent ae = new BoardEvent(this, actingPlayer, 0, "TURNEND");
        
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
        
        ActionEvent ae = new BoardEvent(this, actingPlayer, 0, "PHASESTART");
        
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
        
        ActionEvent ae = new BoardEvent(this, actingPlayer, 0, "PHASEEND");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnDieRolled()
    {
        ActionEvent ae = new BoardEvent(this, die, 0, "DIEROLLED");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnPawnMoved(Pawn pawn)
    {
        Object[] tags = new Object[] { pawn, pawn.GetSquare() };
        ActionEvent ae = new BoardEvent(this, tags, 0, "PAWNMOVED");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    public void OnGameWon(Player p)
    {
        Object[] tags = new Object[] { p };
        ActionEvent ae = new BoardEvent(this, tags, 0, "GAMEWON");
        
        for (ActionListener e : gameEventListeners)
            e.actionPerformed(ae);
    }
    
    // Accessors
    
    public Die GetDie()
    {
        return die;
    }
    
    public void SetSelectedPawn(int player, int pawn)
    {
        this.selectedPawn = this.pawns[player][pawn];
    }
    
    public Pawn GetSelectedPawn()
    {
        return selectedPawn;
    }
    
    public int GetRemainingMoves()
    {
        return this.valueRolled;
    }
    
    public void SetPlayerTurn(int player)
    {
        playerTurn = player;
        actingPlayer = players[player];
        
        valueRolled = 0;
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
    
    public Pawn[] GetPlayerPawns(int player)
    {
        return (Pawn[])pawns[player].clone();
    }
    
    public Pawn[] GetAvailablePawns(int player)
    {
        Player p = players[player];
        
        ArrayList<Pawn> result = new ArrayList<Pawn>();
        for (int i = 0; i < PLAYERPAWNS; i++)
        {
            boolean available = true;
            Pawn pawn = pawns[player][i];
            Field next = pawn.GetSquare().GetNextForPlayer(p, valueRolled);
            Pawn nextPawn = (next != null ? next.GetOccupant() : null);
            
            available &= (!pawn.GetIsHome() || valueRolled == 6);
            available &= (nextPawn == null || nextPawn.GetPlayerId() != player);
            
            if (available)
                result.add(pawn);
        }
        
        // Because apparently this is how you use toArray in Java...
        return result.toArray(new Pawn[0]);
    }
    
    public boolean GetGameWon()
    {
        Player p = actingPlayer;
        int index = p.GetPlayerId();
        
        boolean gameWon = true;
        for (int i = 0; i < PLAYERPAWNS; i++)
            gameWon &= pawns[index][i].GetIsAtGoal();
        
        return gameWon;
    }
    
    public enum PHASE
    {
        NONE,
        STARTTURN,
        ROLLDIE,
        MOVEPAWNS,
        ENDTURN,
        GAMEWON
    }
}
