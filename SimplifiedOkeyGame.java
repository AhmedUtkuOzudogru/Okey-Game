import java.util.Arrays;
import java.util.Random;
import java.util.Arrays;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     * --------------------------DONE---------------------------------
     */
    public void shuffleTiles() {
        Random random = new Random();
        for (int i = 0; i < tiles.length; i++) {
            int rand = random.nextInt(tiles.length);
            Tile temp = tiles[i];
            tiles[i] = tiles[rand];
            tiles[rand] = temp;
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already
     * shuffled
     */
    // not done yet if we cant use arraylist. Should be changed in that case.
    // ------------------------- DONE BUT NOT TESTED -----------------------------

    public void distributeTilesToPlayers() {
        int index = 0;
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            int tilesToDistribute;
            if (i == 0) {
                tilesToDistribute = 15;
            } else {
                tilesToDistribute = 14;
            }

            for (int j = 0; j < tilesToDistribute; j++) {
                player.addTile(tiles[index]);
                index++;
            }
        }
        tiles = Arrays.copyOfRange(tiles, index, tiles.length);
    }


    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we
     * picked
     */
    // -------------------------------------DONE-----------------------------------
    public String getLastDiscardedTile() {
        int currentPlayerIndex = getCurrentPlayerIndex();
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        String lastDiscardedTileInString = lastDiscardedTile.toString();
        lastDiscardedTile = null;
        return lastDiscardedTileInString;
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top
     * tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    // ------------------------- DONE BUT NOT TESTED -----------------------------
    public String getTopTile() {
        // Check if there are any tiles left
        if (tiles.length == 1) {
            Tile topTile = tiles[0];
            players[getCurrentPlayerIndex()].addTile(topTile);
            return topTile.toString();
            
        }        
        else if (tiles.length > 1) {
            Tile topTile = tiles[0];
            tiles = Arrays.copyOfRange(tiles, 1, tiles.length - 1);
            players[getCurrentPlayerIndex()].addTile(topTile);
            return topTile.toString();
        }

        return " not tiles";
        
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */
    // -----------------------------------DONE--------------------------------------
    public boolean didGameFinish() {
        if (tiles.length != 0) {
            return players[getCurrentPlayerIndex()].checkWinning();
            /*
             * for (Player element : players )
             * {
             * if(element.checkWinning()==true){
             * return true;
             * }
             * }
             */
        } else {
            return true;
        }
    }

    /*
     * TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    // ---------------------- DONE------------------------------------
    public Player[] getPlayerWithHighestLongestChain() {
        Player[] winners = new Player[1];
        int longestChain = 0;
        for (Player player : players) {
            if (player.findLongestChain() >= longestChain) {
                longestChain = player.findLongestChain();
                winners[0] = player;
            }

        }

        return winners;
    }

    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        System.out.println("There is Tiles in the stack: "+ (tiles.length != 1));
        return tiles.length != 1;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    // ----------------------- DONE --------------------------
    public void pickTileForComputer() {
        if(tiles.length!=1){
        int currLongest = players[getCurrentPlayerIndex()].findLongestChain();
        Tile testLastTile = lastDiscardedTile;
        players[getCurrentPlayerIndex()].addTile(testLastTile);
        int newLongestChain = players[currentPlayerIndex].findLongestChain();
        int position = players[currentPlayerIndex].findPositionOfTile(testLastTile);
        players[currentPlayerIndex].getAndRemoveTile(position);

        if (currLongest < newLongestChain) {
            System.out.println(players[getCurrentPlayerIndex()].getName() + " picked The Last Discarded Tile"
                    + getLastDiscardedTile());
        } else {
            System.out.println(players[getCurrentPlayerIndex()].getName() + " picked The Top Tile" + getTopTile());
        }
    }else{
        System.out.println(players[getCurrentPlayerIndex()].getName() + " picked The Last Discarded Tile"
                    + getLastDiscardedTile());
    }

    }

        /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() {
        Tile[] tiles = players[getCurrentPlayerIndex()].getTiles();
        // Check for duplicates
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].matchingTiles(tiles[i + 1])) {
                discardTile(i);
                return;
            }
        }int leastUsefulIndex = 0;
        int longestChain = 0;
    
        for (int i = 0; i < players[currentPlayerIndex].numberOfTiles; i++) {
            Tile currentTile = players[currentPlayerIndex].getTiles()[i];
    
            // Simulate removing and re-adding the current tile to find the longest chain
            players[currentPlayerIndex].getAndRemoveTile(i);
            int tempLongestChain = players[currentPlayerIndex].findLongestChain();
            players[currentPlayerIndex].addTile(currentTile);
    
            // Update least useful index if a longer chain is found
            if (tempLongestChain > longestChain) {
                leastUsefulIndex = i;
                longestChain = tempLongestChain;
            }
        }
        this.discardTile(leastUsefulIndex);

        
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    // -----------------------------DONE------------------------------------
    public void discardTile(int tileIndex) {
        System.out.println(getCurrentPlayerName()+" discarded " + players[getCurrentPlayerIndex()].getTiles()[tileIndex].getValue());
        lastDiscardedTile = players[getCurrentPlayerIndex()].getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if (index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
