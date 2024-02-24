import java.util.Arrays;

public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /**
     * Checks if the players longest consecutive chain is equal or more than 14.
     * If it is then the player wins the game.
     * @return true if player wins, false otherwise
     */
    public boolean checkWinning() {
        int longestChain = findLongestChain();

        if (longestChain >= 14)
        {
            return true;
        }

        return false;
    }

    /**
     * Finds the longest consecutive chain of the player.
     * @return longest consecutive chain of the player
     */
    public int findLongestChain() {
        int longestChain = 0;
        int currChain = 0;

        for (int i = 2 ; i < this.playerTiles.length ; i++)
        {
            Tile currTile = this.playerTiles [i];
            Tile preTile = this.playerTiles [i - 1];
            if (currTile.canFormChainWith (preTile) )
            {
                currChain++;

                if (currChain > longestChain)
                {
                    longestChain = currChain;
                }
            }

            else if (!currTile.canFormChainWith (preTile) && !currTile.matchingTiles (preTile) )
            {
                currChain = 0;
            }
        }
        return longestChain;
    }

    /**
     * Removes one tile from {@code this.playerTiles}
     * @param index of the tile to be removed
     * @return Tile that is being removed
     */
    public Tile getAndRemoveTile(int index) {
        Tile removingTile = this.playerTiles [index];
        this.playerTiles [index] = null;
        this.sortTiles();

        // Shift remaining tiles to the left
        for (int i = index - 1; i > 0 ; i--)
        {
            this.playerTiles [i + 1] = this.playerTiles [i];
        }

        // Clear the last slot
        this.playerTiles [0] = null;
        this.numberOfTiles--;

        return removingTile;
    }

    /**
     * Adds one tile to {@code this.playerTiles}
     * @param t Tile to be added
     */
    public void addTile(Tile t) {
        this.playerTiles [0] = t;
        this.numberOfTiles++;
        // Insert the new tile into its correct position in the sorted array
        sortTiles();
    }

    private void sortTiles ()
    {

        for (int i = 1 ; i < this.playerTiles.length ; i++)
        {
            int j = i;
            int currValue = 0;
            int nextValue = 0;
            if (this.playerTiles [j] == null)
            {
                nextValue = -1;
            }
            else 
            {
                nextValue = this.playerTiles [j].getValue();
            }
            if (this.playerTiles [j - 1] == null)
            {
                currValue = -1;
            }
            else
            {
                currValue = this.playerTiles [j - 1].getValue();
            }

            while (j > 0 && currValue > nextValue)
            {
                Tile temp = this.playerTiles [j];
                this.playerTiles [j] = this.playerTiles [j - 1];
                this.playerTiles [j - 1] = temp;
                j--;
                if (j != 0)
                {
                    if (this.playerTiles [j] == null)
                    {
                        nextValue = -1;
                    }
                    else 
                    {
                        nextValue = this.playerTiles [j].getValue();
                    }
                    if (this.playerTiles [j - 1] == null)
                    {
                        currValue = -1;
                    }
                    else
                    {
                        currValue = this.playerTiles [j - 1].getValue();
                    }
                }
            }
        }
    }

    /**
     * Finds the index of a given tile in this player's hand.
     * @param t Tile to search for
     * @return Index of the tile in the hand, or -1 if not found
     */
    public int findPositionOfTile(Tile t) {
        int tilePosition = 0;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /**
     * displays the tiles of this player
     */
    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 1; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
