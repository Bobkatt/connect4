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
        int myAgentWin = iCanWin(iAmRed);
        moveOnColumn(myAgentWin);
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
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
    public int getLowestEmptyIndex(Connect4Column column) {
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
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin(boolean red)
    {
        return getRatingScore(red);
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin(boolean red)
    {
        return getRatingScore(red);
    }
    
    public int getRatingScore(boolean red)
    {
        //add another option here, set bounds to check to, miss 3 tokens check if both i can win and they can win both return -1
        //loop through all columns on the board
        for(int i = 0; i < myGame.getColumnCount(); i++) 
        {
            Connect4Column column = myGame.getColumn(i);
            //check if column is not yet full
            if (!column.getIsFull())
            {
                //get next move on current column
                int rowId = getNextRow(column);
                if (rowId > -1)
                {
                    //are there space for 3 slots below?
                    if(rowId < myGame.getRowCount() -3)
                    {
                        //check all three below for matching colour
                        if(checkForMatch(red, myGame.getColumn(i).getSlot(rowId + 1), myGame.getColumn(i).getSlot(rowId + 2), myGame.getColumn(i).getSlot(rowId + 3)))
                        {
                            return i;
                        }
                        //are there 3 slots to the left?
                        if(i < myGame.getColumnCount() -3)
                        {
                            //check down and right for 3 matching colour
                            if(checkForMatch(red, myGame.getColumn(i + 1).getSlot(rowId + 1), myGame.getColumn(i + 3).getSlot(rowId + 3), myGame.getColumn(i + 2).getSlot(rowId + 2))) 
                            {
                                return i;
                            }
                            //check down right x 2 and 1 x up left - row count > 0, col count > 0
                            //check down right x 1 and 2 x up left - row count > 1, col count > 1 
                            //check 3 x up left
                        }
                    }
                    //are there 3 slots to the left?
                    if(i < myGame.getColumnCount() -3)
                    {
                        //check for 3 to the right for matching colour
                        if(checkForMatch(red, myGame.getColumn(i + 1).getSlot(rowId), myGame.getColumn(i + 2).getSlot(rowId), myGame.getColumn(i + 3).getSlot(rowId))) 
                        {
                            return i;
                        }
                        //check 2 to right and 1 to left
                        //check 1 right and 2 left
                        //check 3 left
                        
                    }
                }
                
                //check up right x 3
                //check up right x 3, down left x 1
                //check up right x 1 , down left x 2
                //check down left x 3

            }
            
        }
                
        return -1;
    }
    
    /**
     * Get the next available slot on the selected column
     * 
     * @param column The column to check.
     * @return the row id starting with 0 from the top
     */
    public int getNextRow(Connect4Column column)
    {
        int newRowId = 0;
        while (newRowId < column.getRowCount() && !column.getSlot(newRowId).getIsFilled())
        {
            newRowId ++;
        }
        return newRowId;
    }
    
    /**
     * Check for a match within  the 3 slots provided
     * 
     * @param isRed boolean is the matching colour red?
     * @param slot1 Connect4Slot - 1st slot to compare
     * @param slot2 Connect4Slot - 2nd slot to compare
     * @param slot3 Connect4Slot - 3rd slot to compare
     * @return true if all match isRed otherwise will return false 
     */
    
    public boolean checkForMatch(boolean isRed, Connect4Slot slot1, Connect4Slot slot2, Connect4Slot slot3)
    {
        if(slot1.getIsRed()==isRed && slot2.getIsRed()==isRed && slot3.getIsRed()==isRed) 
        {
            return true;
        }
        return false;
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
