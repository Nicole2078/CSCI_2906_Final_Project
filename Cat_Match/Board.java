//package com.example.capstone;
/*
Author: Nicole Hackler
Date:  3/11/2023 (Started 4 weeks prior)

This Class Contains the Board for the Tiles.
The Board class makes only one board with the ability to change its size if programmed in. That is not part of the
program yet. It constructs a board already loaded with randomized Tiles.

This class contains the methods for shuffling the board, searching the board for a match, changing the board's status
whether it's the 'State' or 'View'. It also contains boolean searches for each state and view if needed.
*/
import java.util.Random;

public class Board {
    private Tile[][] board;
    protected static int mRow;
    protected static int mCol;

    //Constructors
    Board(int mRow, int mCol){
        this.board = new Tile[mRow][mCol];
        for(int i = 0; i < mRow; i++){
            for(int j = 0; j < mCol; j++){
                Tile temp = new Tile();
                board[i][j] = temp;
                temp.setTile(temp.getRandomTileShape());
                temp.setState(Tile.TileState.IDLE);
                temp.setView(Tile.TileVisible.SHOW);
            }
        }
    }

    //Getter and setters
    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board){
        this.board = board;
    }

    public void setCol(int mCol) {
        this.mCol = mCol;
    }

    public int getCol() {
        return mCol;
    }

    public void setRow(int mRow) {
        this.mRow = mRow;
    }

    public int getRow() {
        return mRow;
    }

    //Able to retrieve a Tile at this location
    public Tile getChildAt(int row, int col){
        return board[row][col];
    }

    //Changing the tiles up, down, left and right of the selected tile to IDLE to ACTIVE
    public static void setToActive(Tile[][] tiles, int i, int j){
        if(j > 0){ //checking only the tile up if out of bounds
            Tile tileUp = tiles[i][j - 1];
            tileUp.setState(Tile.TileState.ACTIVE);
        }
        if(j < 9) { //checking only the tile down if out of bounds
            Tile tileDown = tiles[i][j + 1];
            tileDown.setState(Tile.TileState.ACTIVE);
        }
        if(i < 9) { //checking only the tile right if out of bounds
            Tile tileRight = tiles[i + 1][j];
            tileRight.setState(Tile.TileState.ACTIVE);
        }
        if(i > 0) { //checking only the tile left if out of bounds
            Tile tileLeft = tiles[i - 1][j];
            tileLeft.setState(Tile.TileState.ACTIVE);
        }
    }

    //Swap tiles when knowing the x and y cords
    public void swapTiles(int a, int b, int c, int d){
        Tile temp = board[a][b];
        this.board[a][b] = this.board[c][d];
        this.board[c][d] = temp;
    }

    //Shuffling the tiles
    public void shuffleBoard(Tile[][] tiles){
        System.out.println("shuffled board");
        Random random = new Random();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles.length; j++){
                int x = random.nextInt(i + 1);
                int y = random.nextInt(j + 1);
                //swapping tiles around
                Tile temp = tiles[i][j];
                tiles[i][j] = tiles[x][y];
                tiles[x][y] = temp;
            }
        }
    }

    //method to check match
    public static void checkForMatch(Tile[][] tiles){
        //only looking in the row first
        for(int i  = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles.length - 2; j++){
                //Tile.TileShape temp = tiles[i][j].getTile();
                //Comparing the tile to the next two columns
                if (tiles[i][j].getTile() == tiles[i][j + 1].getTile()
                        && tiles[i][j].getTile() == tiles[i][j + 2].getTile()){
                    //Only goes through to change the state and view of the matched tiles
                    for(int n = 0; n <= 2; n++){
                        Tile matchTemp = tiles[i][j + n];
                        //Set at Matchable to check for if it needs to swap back
                        matchTemp.setState(Tile.TileState.MATCHABLE);
                        //set to Hide to change into new tiles.
                        matchTemp.setView(Tile.TileVisible.HIDE);
                    }
                }
            }
        }
        //looking in the columns next
        for(int j = 0; j < tiles.length; j++){
            for(int i = 0; i < tiles.length - 2; i++){
                //Tile.TileShape temp = tiles[i][j].getTile();
                //Comparing the tiles to the next two rows
                if(tiles[i][j].getTile() == tiles[i + 1][j].getTile()
                        && tiles[i][j].getTile()== tiles[i + 2][j].getTile()){
                    //Only goes through to change the state and view of the matched tiles
                    for(int n = 0; n <= 2; n++){
                        Tile matchTemp = tiles[i + n][j];
                        //Set at Matchable to check for if it needs to swap back
                        matchTemp.setState((Tile.TileState.MATCHABLE));
                        //set to Hide to change into new tiles.
                        matchTemp.setView(Tile.TileVisible.HIDE);
                    }
                }
            }
        }
        //System.out.println("\nOne round of checks");
    }//end of checking for match

    //Swap Hidden Tiles
    public static void moveHiddenTiles(Tile[][] tiles){
        //first find a hidden tile
        for(int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                //Checking if the hidden tile is at the top or not
//                Tile tileTemp = tiles[i][j];
                if (tiles[i][j].getView() == Tile.TileVisible.HIDE && j != 0) {
                    Tile tileTemp = tiles[i][j - 1];
                    tiles[i][j - 1] = tiles[i][j];
                    tiles[i][j] = tileTemp;
                } //If it's at the top create a new tile
                if(tiles[i][j].getView() == Tile.TileVisible.HIDE && j == 0){
                    tiles[i][j].getNewTile(tiles[i][j]);
                }
            }
        }
    }

    //Changing the whole board back to IDLE TileState
    public static void boardToIDLE(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                board.getBoard()[i][j].setState(Tile.TileState.IDLE);
            }
        }
    }

    //Returns true if Hidden Tile is found inside the board
    public boolean lookingHidden(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE){
                    return true;
                }
            }
        }
        return false;
    }

    //Methods for checking states and visible inside the board
    public boolean lookingIdle(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getState() == Tile.TileState.IDLE){
                    return true;
                }
            }
        }
        return false;
    }

    //Returns true if a Tile is in the Matchable State inside the board
    public boolean lookingMatchable(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getState() == Tile.TileState.MATCHABLE){
                    return true;
                }
            }
        }
        return false;
    }

    //Returns true if an Active Tile State is found inside the board
    public boolean lookingActive(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getState() == Tile.TileState.ACTIVE){
                    return true;
                }
            }
        }
        return false;
    }

    //Returns true if a Selected Tile State is found inside the board
    public boolean lookingSelected(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getState() == Tile.TileState.SELECTED){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean lookingShown(Board board){
        for(int i = 0; i < board.getBoard().length; i++){
            for(int j = 0; j < board.getBoard()[i].length; j++){
                if(board.getBoard()[i][j].getView() == Tile.TileVisible.SHOW){
                    return true;
                }
            }
        }
        return false;
    }

    //ToString Methods Mainly for testing
    //Method to display the Board class board TileShapes reference TODO For Testing
    public Tile[][] displayBoard(Tile[][] board){
        System.out.println();
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                //needed to swap where 'i' and 'j' are to go row then column
                System.out.print(Tile.toStringShape(board[j][i].getTile()) + ", ");
            }
            System.out.println();
        }
        System.out.println("_____ **** TileShape **** _____"); //testing line
        return board;
    }

    //Method to display the Board class board TileState reference TODO For Testing
    public Tile[][] displayBoardState(Tile[][] board){
        System.out.println();
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                //needed to swap where 'i' and 'j' are to go row then column
                System.out.print(Tile.toStringState(board[j][i].getState()) + ", ");
            }
            System.out.println();
        }
        System.out.println("_____ **** State **** _____"); //testing line
        return board;
    }

    //Method to display the Board class board TileVisible reference TODO For Testing
    public Tile[][] displayBoardView(Tile[][] board){
        System.out.println();
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                //needed to swap where 'i' and 'j' are to go row then column
                System.out.print(Tile.toStringVisible(board[j][i].getView()) + ", ");
            }
            System.out.println();
        }
        System.out.println("_____ **** View **** _____"); //testing line
        return board;
    }
}//End of Board class
