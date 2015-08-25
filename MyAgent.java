import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

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
            nextMove = randomMove();
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
     * @param isRed boolean is the matching colour red?
     * @param startCol int starting column for 1st slot
     * @param startRow int starting row for 1st slot
     * @param numberSlots int number of adjacent slots to compare
     * @direction String which directio to check
     * @return true if all match isRed otherwise will return false
     */
    public boolean checkForMatch(boolean bolRed, Connect4Slot slot1, Connect4Slot slot2, Connect4Slot slot3)
    {
        if(slot1.getIsFilled() && slot2.getIsFilled() && slot3.getIsFilled()) 
        {
            if(slot1.getIsRed()==bolRed && slot2.getIsRed()==bolRed && slot3.getIsRed()==bolRed) 
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check for the best move available
     * Finds next available slot in each row
     * Scans adjacent slots for matching colours and returns column ID if finds winning solution
     * 
     * @param isRed boolean is the matching colour red?
     * @return column id for next move, -1 if none found
     */
    public int getNextMove(boolean bolRed)
    {
        int gridRows = myGame.getRowCount();
        int gridColumns = myGame.getColumnCount();
        for(int i = 0; i < myGame.getColumnCount(); i++) 
        {
            Connect4Column column = myGame.getColumn(i);
            if (!column.getIsFull())//check if column is not yet full
            {
                int rowId = getLowestEmptyIndex(column);//get next move on current column
                if (rowId >= 0)
                {
                    //vertical (only below row)
                    //is there 3 rows below?
                    if(rowId < gridRows -3)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i).getSlot(rowId + 1), myGame.getColumn(i).getSlot(rowId + 2), myGame.getColumn(i).getSlot(rowId + 3)))//check all 3 for matching colour
                        {
                            return i;
                        }
                    }
                    //horizontal
                    //3 to the left
                    if(i > 2)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 3).getSlot(rowId), myGame.getColumn(i - 2).getSlot(rowId), myGame.getColumn(i - 1).getSlot(rowId)))
                        {
                            return i;
                        }
                    }
                    //2 to the left and 1 right
                    if(i > 1 && i < gridColumns - 1)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 2).getSlot(rowId), myGame.getColumn(i - 1).getSlot(rowId), myGame.getColumn(i + 1).getSlot(rowId)))
                        {
                            return i;
                        }
                    }
                    //1 to the left and 2 right
                    if(i > 0 && i < gridColumns - 2)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 1).getSlot(rowId), myGame.getColumn(i + 1).getSlot(rowId), myGame.getColumn(i + 2).getSlot(rowId)))
                        {
                            return i;
                        }
                    }
                    //3 to the right
                    if(i < gridColumns - 3)//is there 3 columns to the right
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i + 1).getSlot(rowId), myGame.getColumn(i + 2).getSlot(rowId), myGame.getColumn(i + 3).getSlot(rowId)))
                        {
                            return i;
                        }
                    }
                    //diagonal left to right
                    //3 left and 3 up
                    if(rowId > 2 && i > 2)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 3).getSlot(rowId - 3), myGame.getColumn(i - 2).getSlot(rowId - 2), myGame.getColumn(i - 1).getSlot(rowId - 1)))
                        {
                            return i;
                        }
                    }
                    //2 left and up + 1 right and down
                    if(rowId > 1 && i > 1 && rowId < gridRows -1 && i < gridColumns - 1)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 2).getSlot(rowId - 2), myGame.getColumn(i - 1).getSlot(rowId - 1), myGame.getColumn(i + 1).getSlot(rowId + 1)))
                        {
                            return i;
                        }
                    }
                    //1 up and left + 2 down and right
                    if(rowId > 0 && i > 0 && rowId < gridRows -2 && i < gridColumns -2)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 1).getSlot(rowId - 1), myGame.getColumn(i + 1).getSlot(rowId + 1), myGame.getColumn(i + 2).getSlot(rowId + 2)))
                        {
                            return i;
                        }
                    }
                    //3 right and 3 below for down right x 3
                    if(rowId < gridRows -3 && i < gridColumns -3)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i + 1).getSlot(rowId + 1), myGame.getColumn(i + 2).getSlot(rowId + 2), myGame.getColumn(i + 3).getSlot(rowId + 3)))
                        {
                            return i;
                        }
                    }
                    //diagional right to left
                    //3 right and 3 up
                    if(rowId > 2 && i < gridColumns - 3)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i + 1).getSlot(rowId - 1), myGame.getColumn(i + 2).getSlot(rowId - 2), myGame.getColumn(i + 3).getSlot(rowId - 3)))
                        {
                            return i;
                        }
                    }
                    //2 right and up + 1 left and down
                    if(rowId > 1 && i < gridColumns - 2 && rowId < gridRows - 1 && i > 0)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 1).getSlot(rowId + 1), myGame.getColumn(i + 1).getSlot(rowId - 1), myGame.getColumn(i + 2).getSlot(rowId - 2)))
                        {
                            return i;
                        }
                    }
                    //1 right and up + 2 left and down
                    if(rowId > 2 && i < gridColumns - 1 && rowId < gridRows - 2 && i > 1)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 2).getSlot(rowId + 2), myGame.getColumn(i - 1).getSlot(rowId + 1), myGame.getColumn(i + 1).getSlot(rowId - 1)))
                        {
                            return i;
                        }
                    }
                    //3 left and down
                    if(rowId < gridRows - 3 && i > 2)
                    {
                        if(checkForMatch(bolRed, myGame.getColumn(i - 3).getSlot(rowId + 3), myGame.getColumn(i - 2).getSlot(rowId + 2), myGame.getColumn(i - 1).getSlot(rowId + 1)))
                        {
                            return i;
                        }
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
