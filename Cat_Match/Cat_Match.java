//package com.example.capstone;
/*
Author: Nicole Hackler
Date: 3/11/2023 (Started 4 weeks prior)

This is the main class for the Game.
It contains all the javaFX animations and GUI information.

This is a Tile 3 match game. Before the user even makes a move the board is loaded (future adjustment for
letting the user pick the board size could come later). The program will already search for matches and add
them to the score of the game. There is currently no winning condition that I've set in place, but still
felt there should be a score being kept.
Each Tile is worth 100 points.
The user picks a Tile and then should pick a tile either up, left, right or below. the game is programmed
to only match two tiles in that fashion, picking another tile further away will reset it to the 'first' tile.
The game then proceeds to see if what was selected to be a match, if it is the score goes. The game then
checks again to see if any of the newly made tiles and the ones that dropped from above are a match before
letting the user pick again.
There is a button to allow for 'shuffling' the board.
The game does not currently check to see if there are no matches, and does not have an auto shuffle action.
I gave the option to shuffle the board to the user as there was also not a winning condition, so you could play
for as long as you'd want without having to reload the game.
*/
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;


public class Cat_Match extends Application {
    Tile tempA = new Tile();
    Tile tempB = new Tile();
    int aPosX;
    int aPosY;
    int bPosX;
    int bPosY;

    @Override
    public void start(Stage primaryStage) {
        //TODO if I have time: make options for board sizes with buttons or drop down in the Border Pane
        Board board = new Board(10, 10);

        System.out.println(); //TODO take out later here for testing (remember to take the print line out in Tile class for random tiles being made)
        //Hbox for buttons, labels and Score
        HBox buttonBox = new HBox(20);
        Button btShuffle = new Button("Shuffle");

        //Label for Score
        Label lbScore = new Label("Score:");
        lbScore.setTextFill(Color.WHITE);

        //Score (designed as a label)
        Label score = new Label();
        score.setText("0");
        score.setFont(Font.font("", FontWeight.BOLD,20));
        score.setTextFill(Color.WHITE);

        //Adding label score and button to the Hbox
        buttonBox.getChildren().addAll(lbScore, score, btShuffle);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-background-color: black");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setGridLinesVisible(true); //for testing
        gridPane.setStyle("-fx-background-color: black"); //change to grey for testing
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(5); //spacing between the tiles
        gridPane.setVgap(5);

        //Row and Column constraints were for when my shapes wouldn't fit but leaving in for now TODO maybe remove
        RowConstraints rc = new RowConstraints(); //Row constraints making sure the cells are smaller
        rc.setPercentHeight(10 / (board.getBoard().length) / 3);
        for (int i = 0; i < board.getBoard().length; i++) {
            gridPane.getRowConstraints().add(rc);
        }
        ColumnConstraints cc = new ColumnConstraints(); //Column Constraints, making sure the cells are smaller
        cc.setPercentWidth(10 / (board.getBoard().length) / 3);
        for (int i = 0; i < board.getBoard().length; i++) {
            gridPane.getColumnConstraints().add(cc);
        }

        //To display the board to the user for the first time.
        upDateGridPane(gridPane, board); //Updating the Grid gridPane with the board content.

        //Timelines... might only need one of these...
        Timeline timeline = new Timeline();
        Timeline timeline1 = new Timeline(); // this is commented out down below, don't remove just yet
        Timeline timeline2 = new Timeline();
        Timeline timeline3 = new Timeline();
        Timeline timelineSB = new Timeline();

        //Border pane for holding buttons, label, score and grid pane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(buttonBox);

        //Create a scene and place it in the stage
        //TODO if you add new things to the border pane add the size to the scene or you will mess the Grid Pane up
        Scene scene = new Scene(borderPane, 450, 470);
        primaryStage.setTitle("Cat (Shape) Match!"); //Set the stage title
        primaryStage.setScene(scene); //Place the scene in the stage
        primaryStage.show(); //Display the stage

        //####################################################################################################//
        //Beginning of the Game

        //Checking if the board that loaded contains any matches before the user can pick tiles
        Board.checkForMatch(board.getBoard());

        //Recreating tiles if ones got matched before the user picks Tiles
        while (board.lookingHidden(board)) {
            //first find a hidden tile
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard().length; j++) {
                    //Checking if the hidden tile is at the top or not
                    if (board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j != 0) {
                        Tile tileTemp = board.getBoard()[i][j - 1]; //swapping tiles UP
                        board.getBoard()[i][j - 1] = board.getBoard()[i][j];
                        board.getBoard()[i][j] = tileTemp;
                    } //If it's at the top create a new tile
                    else if (board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j == 0) {
                        board.getBoard()[i][j].getNewTile(board.getBoard()[i][j]); //making a new Tile
                        //Updating the score for every tile that is re-created
                        int tempScore = Integer.parseInt(score.getText());
                        int tileValue = tempScore + 100;
                        score.setText(Integer.toString(tileValue));
                    }
                    KeyFrame kf = new KeyFrame(Duration.seconds(2 + j), e -> upDateGridPane(gridPane, board));
                    timeline.getKeyFrames().add(kf);
                    timeline.play();
//                    upDateShapes(timeline, j, gridPane, board); //method for using one Timeline
                }
//                timeline.play(); //This was the original place I had timeline.play()
            }
            Board.checkForMatch(board.getBoard()); //Re-Checking for matches before moving on
        }//End of while loop for changing hidden tiles

        //First mouse click, tempA hold the first variable obtained from the event
        tempA = new Tile();// making sure the tempA tile isn't null but a NEW empty tile for the event handler for the Grid gridPane

        //Mouse Pressed Event Handler
        gridPane.setOnMousePressed(e -> {
            System.out.println("\nHello"); //testing line remove later
            //matching the mouse click with the array behind the gridPane
            int x = (int) (((e.getX() - 50) / 30) - 1);
            int y = (int) (((e.getY() - 50) / 30) - 1);
            //accounting for mathematical clicking outside the gridPane... my board is weird
            if (x < 0) {
                x = 0;
            }
            if (x > 9) {
                x = 9;
            }
            if (y < 0) {
                y = 0;
            }
            if (y > 9) {
                y = 9;
            }

            System.out.println("First mouse click x: " + x + " y: " + y); //Testing line remove later

            //Acquiring each variable based on the status of tempA. TempA changes after all the checks at the bottom of all
            if(tempA.getTile() == null){ //get first variable if tempA is empty
                tempA = board.getChildAt(x, y); //Setting first mouse click variable

                //letting the user know which tile they have chosen //TODO it only working sometimes
                FadeTransition ft = new FadeTransition(new Duration(3000), (Node)e.getTarget());
                ft.setFromValue(1.0);
                ft.setToValue(0.3);
                ft.setAutoReverse(true);
//                ft.setDuration(Duration.INDEFINITE); //TODO might need to remove..
                ft.setCycleCount(2); //TODO change this to a int value if i can't figure out how to not click on the screen

                //playing fade transition on first mouse click
                ((Node) e.getTarget()).setOnMouseClicked(fadeEvent -> ft.play());

                System.out.println(Tile.toStringShape(tempA.getTile()) + " = First tile after set the second"); //Testing line remove later
            }
            else{ //Gets second variable to compare if tempA is not null/empty
                tempB = board.getChildAt(x,y); //Second mouse click variable
            }

            //Setting the tiles around the first one to selected
            if(tempA.getState() == Tile.TileState.IDLE) {
                tempA.setState(Tile.TileState.SELECTED);
                Board.setToActive(board.getBoard(), x, y); //changing UP, DOWN, LEFT, and RIGHT ot active state

//                System.out.println("\nView State after first tiles is picked:");//testing line remove later
//                board.displayBoardState(board.getBoard()); //Testing line remove later
                System.out.println(Tile.toStringShape(tempA.getTile())); //Testing line remove later

                aPosX = x; //setting tempA x and y values base on the mouse click
                aPosY = y;
            }
            //Allowing a second variable to be obtained
            else if(tempA.getState() == Tile.TileState.SELECTED && tempB.getState() == Tile.TileState.ACTIVE){
                tempB.setState(Tile.TileState.SELECTED); //TODO testing this for the second swap back when I don't need it

                System.out.println("Second click x: " + x + " y: " + y); //testing line remove later
                System.out.println(Tile.toStringShape(tempB.getTile()) + " = Second variable"); //Testing line remove later
                System.out.println(Tile.toStringShape(tempA.getTile()) + " = First tile after set the second"); //Testing line remove later

                bPosX = x; //Setting tempB's x and y value after the tempA's x and y values are set
                bPosY = y;

                System.out.println("APos = " + aPosX + " " + aPosY + " BPos = " + bPosX + " " + bPosY); //Testing line

                //First swap before checks
                board.swapTiles(aPosX, aPosY, bPosX, bPosY); //The swap: call this to swap them back if they don't match

//                KeyFrame kf1 = new KeyFrame(Duration.seconds( 2 ), e1 -> upDateGridPane(gridPane, board));
//                timeline1.getKeyFrames().add(kf1);
//                timeline1.play();

//                upDateShapes(timeline, 1, gridPane, board); //method for using one Timeline
//                timeline.play(); //for when I'm using one timeline

//                System.out.println("\nView State after Second variable changed to selected:"); //Testing line remove later
//                board.displayBoardState(board.getBoard());//for testing remove later***

//                ft.stop(); //Fade transition for the first tile chosen

//                This slows down the first swap
                try {
                    Thread.sleep(750);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                upDateGridPane(gridPane, board); //Updating the Grid gridPane after first swap

                //first time it checks for a match after the swap
                Board.checkForMatch(board.getBoard()); //Checking if either tile is a match

//                System.out.println("\nDisplay View after there is a match to show the HIDE:"); //Testing line remove later
//                board.displayBoardView(board.getBoard());//Testing line remove later

                //Boolean to control the swap back if needed
                boolean ifSwapped = false;

                //move hidden tiles up and recreate them
                while(board.lookingHidden(board)){
                    //first find a hidden tile
                    for(int i = 0; i < board.getBoard().length; i++) {
                        for (int j = 0; j < board.getBoard().length; j++) {
                            //Checking if the hidden tile is at the top or not
                            if (board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j != 0) {
                                Tile tileTemp = board.getBoard()[i][j - 1];
                                board.getBoard()[i][j - 1] = board.getBoard()[i][j];
                                board.getBoard()[i][j] = tileTemp;
                            }//If it's at the top create a new tile
                            else if(board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j == 0){
                                board.getBoard()[i][j].getNewTile(board.getBoard()[i][j]); //making a new Tile
                                //If a new tile was created this won't let the tile be a swap back of the first x, y tile
                                ifSwapped = true;
                                //Updating the score for every tile that is re-created
                                int tempScore = Integer.parseInt(score.getText());
                                int tileValue = tempScore + 100;
                                score.setText(Integer.toString(tileValue));
                            }
                            KeyFrame kf2 = new KeyFrame(Duration.seconds(2 + j), e2 -> upDateGridPane(gridPane, board));
                            timeline2.getKeyFrames().add(kf2);
                            timeline2.play();
//                            upDateShapes(timeline, j, gridPane, board); //method for using one Timeline
                        }
//                        timeline2.play(); //This was the original place I had timeline2.play()
//                        timeline.play(); //for when I'm using one timeline
                    }
                    Board.checkForMatch(board.getBoard()); //Re-checking for matches
                }//End of while loop for checking for matches

//                System.out.println("\nShowing the States after the match, making sure states haven't changed:");//testing line remove later
//                board.displayBoardState(board.getBoard());//Testing line remove later
//                System.out.println("\nSeeing if all hidden tiles are gone:");//testing line
//                board.displayBoardView(board.getBoard());//Testing line remove later

                //if not tiles are changed to a match state it will swap the 2 original tiles back
                //Controlled by a boolean variable inside the while loop for checking for matches.
                if(!ifSwapped){
                    board.swapTiles(aPosX, aPosY, bPosX, bPosY);

//                    System.out.println("\nDisplay the states after the second swap if swapped back:"); //testing line remove later
//                    board.displayBoardState(board.getBoard());//for testing remove later
                } //End of if no matches found

                KeyFrame kf3 = new KeyFrame(Duration.seconds(1), e3 -> upDateGridPane(gridPane, board));
                timeline3.getKeyFrames().add(kf3);
                timeline3.play();

//                upDateShapes(timeline, 1, gridPane, board); //method for using one Timeline
//                timeline.play(); //for when I'm using one timeline

                //#-#-#-#-#-#-#-#-#-#-#-#-#//
                tempA = new Tile(); //TODO MUST KEEP!!!!!!! this resets the mouse event and changes tempA to be 'null'
                //#-#-#-#-#-#-#-#-#-#-#-#-#//

                Board.boardToIDLE(board); //Resetting all tiles to IDLE

//                System.out.println("\nDisplaying the board changed to IDLE"); //Testing line remove later
//                board.displayBoardState(board.getBoard());//testing line remove later

            } //End of the if else statement for obtaining the second variable.
            //Resetting tempA and tempB back to what they were before the events
            else {
                tempA = new Tile();
                tempB = new Tile();

//                board.displayBoardState(board.getBoard());//testing line remove later
            }
        }); //End of mouse event handler;

        //Shuffle button handler
        btShuffle.setOnAction(eBT -> {
            board.shuffleBoard(board.getBoard());

            //Checking if the board that loaded contains any matches after the shuffle
            Board.checkForMatch(board.getBoard());

            //Recreating tiles if ones got matched during the shuffle
            while (board.lookingHidden(board)) {
                //first find a hidden tile
                for (int i = 0; i < board.getBoard().length; i++) {
                    for (int j = 0; j < board.getBoard().length; j++) {
                        //Checking if the hidden tile is at the top or not
                        if (board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j != 0) {
                            Tile tileTemp = board.getBoard()[i][j - 1]; //swapping tiles UP
                            board.getBoard()[i][j - 1] = board.getBoard()[i][j];
                            board.getBoard()[i][j] = tileTemp;
                        } //If it's at the top create a new tile
                        else if (board.getBoard()[i][j].getView() == Tile.TileVisible.HIDE && j == 0) {
                            board.getBoard()[i][j].getNewTile(board.getBoard()[i][j]); //making a new Tile
                            //Updating the score for every tile that is re-created
                            int tempScore = Integer.parseInt(score.getText());
                            int tileValue = tempScore + 100;
                            score.setText(Integer.toString(tileValue));
                        }
                        KeyFrame kfSB = new KeyFrame(Duration.seconds( 1 + j), eSB -> upDateGridPane(gridPane, board));
                        timelineSB.getKeyFrames().add(kfSB);
                        timelineSB.play();
//                        upDateShapes(timeline, j, gridPane, board); //method for using one Timeline
                    }
//                    timelineSB.play(); //This was the original place I had timelineSB.play()
//                    timeline.play(); //for when I'm using one timeline
                }
                Board.checkForMatch(board.getBoard()); //Re-Checking for matches before moving on
            }//End of while loop for changing hidden tiles
        });//End of the shuffle button handler

    }//End of Start method

    //Method for KeyFrame for if I use one Timeline
    public void upDateShapes(Timeline timeline, int step, GridPane pane, Board board){
        KeyFrame kf = new KeyFrame(Duration.millis(1000 * step), e ->{
            upDateGridPane(pane, board);
        });
        timeline.getKeyFrames().add(kf);
    }

    //Method to update the Grid pane after a move is made.
    public void upDateGridPane(GridPane pane, Board board){
        pane.getChildren().clear();
        for (int x = 0; x < board.getBoard().length; x++) {
            for (int y = 0; y < board.getBoard()[x].length; y++) {
                pane.add(board.getBoard()[x][y].setColorContent(board.getBoard()[x][y]), x, y);
            }
        }
    }

    //Main method
    public static void main (String[] args) {
        Application.launch(args);
    }
}//End of GameTest class
