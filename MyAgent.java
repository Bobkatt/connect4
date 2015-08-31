import java.util.Random;

public class MyAgent extends Agent
{
    Random r;
    private static final int SLOTS_TO_CHECK = 3;
    
    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        int nextMove = 0;
        int redCanWin = getNextMove(iAmRed);
        int yellowCanWin = getNextMove(!iAmRed);
        if (redCanWin >= 0)//is there a winning move for red
        {
            nextMove = redCanWin;
        }
        else if (yellowCanWin >= 0)//is there a winning move for yellow can be used to block
        {
            nextMove = yellowCanWin;
        }
        else//no stratigic moves, choose random slot
        {
            nextMove = randomMove();//check position of random move, could this lead to op winning?
        }
        moveOnColumn(nextMove);
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column// If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) 
    {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }
   
    /**
     * compare adjacent slots based on direction and number of slots to check
     * 
     * @param checkColour boolean is the colour?
     * @param slot1 Connect4Slot - 1st slot to compare for match
     * @param slot2 Connect4Slot - 2nd slot to compare for match
     * @param slot3 Connect4Slot - 3rd slot to compare for match
     * @return true if all match checkColour otherwise will return false
     */
    public boolean allThreeMatch(boolean checkColour, Connect4Slot slot1, Connect4Slot slot2, Connect4Slot slot3)
    {
        if(slot1.getIsFilled() && slot2.getIsFilled() && slot3.getIsFilled())//check to see if all 3 slots are currently occupied 
        {
            if(slot1.getIsRed()==checkColour && slot2.getIsRed()==checkColour && slot3.getIsRed()==checkColour)//compare each selected slot with selected colout for match 
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * check posilbilites from selected slot to determine if there is a winning combination
     * 
     * @param checkColour boolean used to pass to allThreeMatch routine at end for comparision
     * @param startCol int column of selected slot
     * @param startRow int row of selected slot
     * @param direction String which direction to check for winning combination
     * @return true if winning combination is found otherwise will return false
     */
    public boolean getSlotsToCheck(boolean checkColour, int startColumn, int startRow, String direction)
    {
        int gridRows = myGame.getRowCount();
        int gridColumns = myGame.getColumnCount();
        boolean isAWinner = false;
        int positionCounter = 0;
        while(positionCounter < (SLOTS_TO_CHECK + 1) && !isAWinner)//loop for all possible positions either side of the selected slot
        {
            boolean okToCheck = true;
            int [][] slotPairs={{0,0}, {0,0}, {0,0}};//array for slot positions to be checked
            int startLocation = -SLOTS_TO_CHECK;
            while (startLocation < 0 && okToCheck)
            {
                int offset = startLocation + positionCounter;
                if (offset > -1)//skip startSlot Position
                {
                    offset += 1;
                }
                int checkColumn = 0;
                int checkRow = 0;
                switch(direction)//based on direction, get next slot position
                {
                    case "Diagonal Left"://alter row and column
                        checkColumn = startColumn + offset;
                        checkRow = startRow - offset;
                        break;
                    case "Diagonal Right"://alter row and column
                        checkColumn = startColumn + offset;
                        checkRow = startRow + offset;
                        break;
                    case "Horizontal"://alter column only
                        checkColumn = startColumn + offset;
                        checkRow = startRow;
                        break;
                    case "Vertical"://alter row only
                        checkColumn = startColumn;
                        checkRow = startRow + offset;
                        break;
                }   
                if(checkRow >= 0 && checkRow < gridRows && checkColumn >= 0 && checkColumn  < gridColumns)//is slot position within bounds of the board
                {
                    slotPairs[startLocation + SLOTS_TO_CHECK][0]= checkColumn;
                    slotPairs[startLocation + SLOTS_TO_CHECK][1]= checkRow;
                }
                else
                {
                    okToCheck = false;//if outside bounds of board, not worth checking
                }
                startLocation++;
            }
            if(okToCheck && !isAWinner)
            {
                isAWinner = allThreeMatch(checkColour, myGame.getColumn(slotPairs[0][0]).getSlot(slotPairs[0][1]), myGame.getColumn(slotPairs[1][0]).getSlot(slotPairs[1][1]), myGame.getColumn(slotPairs[2][0]).getSlot(slotPairs[2][1]));
            }
            positionCounter++;
        }
        return isAWinner;
    }
    
    /**
     * Check for the best move available
     * Finds next available slot in each row
     * Scans adjacent slots for matching colours and returns column ID if finds winning solution
     * 
     * @param playerRed boolean is the matching colour red?
     * @return column id for next move, -1 if none found
     */
    public int getNextMove(boolean playerRed)
    {
        for(int i = 0; i < myGame.getColumnCount(); i++) 
        {
            Connect4Column column = myGame.getColumn(i);
            if (!column.getIsFull())//check if column is not yet full
            {
                int nextSlot = getLowestEmptyIndex(column);//get next move on current column
                if (nextSlot >= 0)
                {
                    if(getSlotsToCheck(playerRed, i, nextSlot, "Vertical"))//check for winning move on vertical axis 
                    {
                        return i;
                    }
                    else if (getSlotsToCheck(playerRed, i, nextSlot, "Horizontal"))//check for winning move on horizontal axis
                    {
                        return i;
                    }
                    else if (getSlotsToCheck(playerRed, i, nextSlot, "Diagonal Left"))//check for winning move on diagonal left axis
                    {
                        return i;
                    }
                    else if (getSlotsToCheck(playerRed, i, nextSlot, "Diagonal Right"))//check for winning move on diagonal right axis
                    {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
