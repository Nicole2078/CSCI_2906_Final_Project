//package com.example.capstone;
/*
Author: Nicole Hackler
Date: 3/11/2024 (started 4 weeks prior)

This is one of the Object classes for my Match 3 tile game.
This class contains the information to create a Tile. It holds 3 different Enum class for the different shapes,
tile state, and if the tile is shown or hidden (Hidden is for if a tile has been matched and needs to be recreated.

This class also contains a method to randomly get the tiles based on their shape.
The shape of the tile shown on the Grid pane in the game is determined by a SVGPath design that I used to design each
shape. I used this website: ( https://codepen.io/anthonydugois/pen/mewdyZ ) to build my SVGPaths on. After drawing the
shapes I had to divide each point and line by 8 to get them to be the size I wanted for the game.

There is a total of 16 shapes I created, but I've only allowed 12 of them to show up on the Grid pane by adjusting the
max value to 12 from 16 in the getRandomTileShape() method, I've noted in case I want to change it back.
*/
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import java.io.Serializable;

public class Tile implements Serializable {
    private TileShape tile;
    private TileState state;
    private TileVisible view;
    private SVGPath svg = new SVGPath();

    //constructor
    Tile(){
    }

    Tile(TileShape tile){
        this.tile = tile;
    }

    Tile(TileShape tile, TileState state, TileVisible view){
        this.tile = tile;
        this.state = state;
        this.view = view;
    }

    Tile(TileShape tile, TileState state, TileVisible view, SVGPath svg){
        this.tile = tile;
        this.state = state;
        this.view = view;
        this.svg = svg;
    }

    //Needed Enum TileShape, TileState, TileView classes for all the comparing, getting and setting
    public enum TileShape{
        HEART, //1
        CIRCLE, //2
        SQUARE, //3
        FLOWER, //4
        STAR, //5
        RHOMBUS, //6
        DIAMOND, //7
        TRIANGLE, //8
        PENTAGON, //9
        HEXAGON, //10
        OCTAGON, //11
        CAT_HEAD, //12
        SIX_POINT_STAR, //13
        ELLIPSE, //14
        RECTANGLE, //15
        PARALLELOGRAM //16
    }

    public enum TileState{
        IDLE,
        MATCHABLE,
        ACTIVE,
        SELECTED,
    }

    public enum TileVisible{
        SHOW,
        HIDE,
    }

    //Getter and setter
    public TileShape getTile(){
        return tile;
    }

    public void setTile(TileShape tile){
        this.tile = tile;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public TileVisible getView() {
        return view;
    }

    public void setView(TileVisible view) {
        this.view = view;
    }

    public SVGPath getSvg(){
        return svg;
    }

    public void setSvg(SVGPath svg){
        this.svg = svg;
    }

    //Setting method for the content of the SVGpath shape
    public String setShape(TileShape shape){
        switch (shape){
            case HEART: //1
                return "M 50 26 C 46 12 24 24 50 44 C 76 24 54 12 50 26 Z";
            case CIRCLE: //2
                return "M 50 24 C 66 26 66 46 50 48 C 34 46 34 26 50 24 Z";
            case SQUARE: //3
                return "M 50 20 L 38 32 L 50 44 L 62 32 Z";
            case FLOWER: //4
                return "M 50 34 C 50 16 38 22 50 34 C 38 22 30 34 50 34 C 30 34 38 48 50 34 " +
                        "C 38 48 50 52 50 34 C 50 52 62 48 50 34 C 62 48 70 34 50 34 " +
                        "C 70 34 62 22 50 34 C 62 22 50 16 50 34 Z";
            case STAR: //5
                return "M 50 24 L 42 46 L 50 24 L 58 46 L 38 32 L 62 32 L 42 46 Z";
            case RHOMBUS: //6
                return "M 50 20 Q 48 30 38 32 Q 48 34 50 44 Q 52 34 62 32 Q 52 30 50 20 Z";
            case DIAMOND: //7
                return "M 56 24 L 44 24 L 38 30 L 50 44 L 62 30 Z";
            case TRIANGLE: //8
                return "M 50 24 L 62 44 L 38 44 Z";
            case PENTAGON: //9
                return "M 50 24 L 38 32 L 42 46 L 58 46 L 62 32 Z";
            case HEXAGON: //10
                return "M 50 22 L 38 28 L 38 40 L 50 46 L 62 40 L 62 28 Z";
            case OCTAGON: //11
                return "M 44 22 L 38 28 L 38 38 L 44 44 L 56 44 L 62 38 L 62 28 L 56 22 Z";
            case CAT_HEAD: //12
                return "M 56 26 Q 52 24 48 26 L 42 18 L 40 28 Q 40 36 46 40 Q 52 44 58 40 " +
                        "Q 64 36 64 28 L 62 18 L 56 26 Z";
            case SIX_POINT_STAR: //13
                return "M 50 22 L 48 30 L 38 28 L 46 34 L 38 40 L 48 38 L 50 46 L 52 38 L 62 40 " +
                        "L 54 34 L 62 28 L 52 30 Z";
            case ELLIPSE: //14
                return "M 50 20 Q 62 18 62 28 Q 60 40 50 44 Q 38 46 38 36 Q 40 24 50 20 Z";
            case RECTANGLE: //15
                return "M 50 20 L 38 28 L 50 44 L 62 36 Z";
            case PARALLELOGRAM: //16
                return "M 56 20 L 42 20 L 52 42 L 66 42 Z";
            default:
                break;
        }
        return null;
    }//End of setShape method

    //for creating a new tile when the hidden tile reaches the top for replacing
    public Tile getNewTile(Tile tile){ //TODO may need to rework this but it works ok for now
        Tile newTile = tile;
        newTile.setTile(newTile.getRandomTileShape());
        newTile.setColorContent(newTile);
        newTile.setState(Tile.TileState.IDLE);
        newTile.setView(Tile.TileVisible.SHOW);
        return newTile;
    }

    //Setting the color and content in SVGPath for updating the Grid pane (frontend board)
    public SVGPath setColorContent(Tile tile){
        SVGPath svgTemp = new SVGPath(); //needed this to return a Node type for the Grid pane display

        if(tile.getTile() == TileShape.HEART) {
            svgTemp.setFill(Color.CRIMSON);
            svgTemp.setContent(tile.setShape(TileShape.HEART));
        }
        else if (tile.getTile() == Tile.TileShape.CIRCLE) {
            svgTemp.setFill(Color.ORCHID);
            svgTemp.setContent(tile.setShape(Tile.TileShape.CIRCLE));
        }
        else if (tile.getTile() == Tile.TileShape.SQUARE) {
            svgTemp.setFill(Color.MEDIUMSPRINGGREEN);
            svgTemp.setContent(tile.setShape(Tile.TileShape.SQUARE));
        }
        else if (tile.getTile() == Tile.TileShape.FLOWER) {
            svgTemp.setFill(Color.HOTPINK);
            svgTemp.setContent(tile.setShape(Tile.TileShape.FLOWER));
        }
        else if (tile.getTile() == Tile.TileShape.STAR) {
            svgTemp.setFill(Color.YELLOW);
            svgTemp.setContent(tile.setShape(Tile.TileShape.STAR));
        }
        else if (tile.getTile() == Tile.TileShape.RHOMBUS) {
            svgTemp.setFill(Color.DEEPSKYBLUE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.RHOMBUS));
        }
        else if (tile.getTile() == Tile.TileShape.DIAMOND) {
            svgTemp.setFill(Color.PALETURQUOISE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.DIAMOND));
        }
        else if (tile.getTile() == Tile.TileShape.TRIANGLE) {
            svgTemp.setFill(Color.GREEN);
            svgTemp.setContent(tile.setShape(Tile.TileShape.TRIANGLE));
        }
        else if (tile.getTile() == Tile.TileShape.PENTAGON) {
            svgTemp.setFill(Color.ORANGE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.PENTAGON));
        }
        else if (tile.getTile() == Tile.TileShape.HEXAGON) {
            svgTemp.setFill(Color.BLUE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.HEXAGON));
        }
        else if (tile.getTile() == Tile.TileShape.OCTAGON) {
            svgTemp.setFill(Color.PURPLE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.OCTAGON));
        }
        else if (tile.getTile() == Tile.TileShape.CAT_HEAD) {
            svgTemp.setFill(Color.PERU);
            svgTemp.setContent(tile.setShape(Tile.TileShape.CAT_HEAD));
        }
        else if (tile.getTile() == Tile.TileShape.SIX_POINT_STAR) {
            svgTemp.setFill(Color.WHITE);
            svgTemp.setContent(tile.setShape(Tile.TileShape.SIX_POINT_STAR));
        }
        else if (tile.getTile() == Tile.TileShape.ELLIPSE) {
            svgTemp.setFill(Color.INDIGO);
            svgTemp.setContent(tile.setShape(Tile.TileShape.ELLIPSE));
        }
        else if (tile.getTile() == Tile.TileShape.RECTANGLE) {
            svgTemp.setFill(Color.MAROON);
            svgTemp.setContent(tile.setShape(Tile.TileShape.RECTANGLE));
        }
        else if (tile.getTile() == Tile.TileShape.PARALLELOGRAM) {
            svgTemp.setFill(Color.BLANCHEDALMOND);
            svgTemp.setContent(tile.setShape(Tile.TileShape.PARALLELOGRAM));
        }
        else {
            return null;
        }
        return svgTemp;
    }// end of setColorContent method

    //Method containing the switch case for getting the TileShapes at random
    public TileShape getRandomTileShape(){
        int max = 12; //TODO it is originally set to 16 changed to make less shapes showing up, change back if I need
        int min = 1;
        int range = max - min + 1;
        int random = (int)(Math.random() * range) + min;
        System.out.print(random + ", "); //Testing TODO comment out later
        switch (random){
            case 1: //HEART //1
                return TileShape.HEART;
            case 2: // CIRCLE //2
                return TileShape.CIRCLE;
            case 3: //SQUARE //3
                return TileShape.SQUARE;
            case 4: //FLOWER //4
                return TileShape.FLOWER;
            case 5: //STAR //5
                return TileShape.STAR;
            case 6: //RHOMBUS //6
                return TileShape.RHOMBUS;
            case 7: //DIAMOND //7
                return TileShape.DIAMOND;
            case 8: //TRIANGLE //8
                return TileShape.TRIANGLE;
            case 9: //PENTAGON //9
                return TileShape.PENTAGON;
            case 10: //HEXAGON //10
                return TileShape.HEXAGON;
            case 11: //OCTAGON //11
                return TileShape.OCTAGON;
            case 12: //CAT_HEAD //12
                return TileShape.CAT_HEAD;
            case 13: //SIX_POINT_STAR //13
                return TileShape.SIX_POINT_STAR;
            case 14: //ELLIPSE //14
                return TileShape.ELLIPSE;
            case 15: //RECTANGLE //15
                return TileShape.RECTANGLE;
            case 16: //PARALLELOGRAM //16
                return TileShape.PARALLELOGRAM;
            default:
                break;
        }
        return null;
    }//End of getRandomTileShape method

    //Boolean "is" methods for checking if the tile is in the same state or view for if statements
    //Maybe use TODO remove if i don't need later
    public boolean isIdle(Tile tile){
        if(tile.getState() == Tile.TileState.IDLE){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isMatchable(Tile tile){
        if(tile.getState() == Tile.TileState.MATCHABLE){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isActive(Tile tile){
        if(tile.getState() == Tile.TileState.ACTIVE){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isSelected(Tile tile){
        if(tile.getState() == Tile.TileState.SELECTED){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isHidden(Tile tile){
        if(tile.getView() == Tile.TileVisible.HIDE){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isShown(Tile tile){
        if(tile.getView() == Tile.TileVisible.SHOW){
            return true;
        }
        return false;
    }// end of "is" methods

    //To string method for printing to each enum class to console
    public static String toStringShape(TileShape shape){
        switch (shape){
            case HEART: //1
                return "HEART";
            case CIRCLE: //2
                return "CIRCLE";
            case SQUARE: //3
                return "SQUARE";
            case FLOWER: //4
                return "FLOWER";
            case STAR: //5
                return "STAR";
            case RHOMBUS: //6
                return "RHOMBUS";
            case DIAMOND: //7
                return "DIAMOND";
            case TRIANGLE: //8
                return "TRIANGLE";
            case PENTAGON: //9
                return "PENTAGON";
            case HEXAGON: //10
                return "HEXAGON";
            case OCTAGON: //11
                return "OCTAGON";
            case CAT_HEAD: //12
                return "CAT_HEAD";
            case SIX_POINT_STAR: //13
                return "SIX_POINT_STAR";
            case ELLIPSE: //14
                return "ELLIPSE";
            case RECTANGLE: //15
                return "RECTANGLE";
            case PARALLELOGRAM: //16
                return "PARALLELOGRAM";
            default:
                break;
        }
        return null;
    }//End of toString method

    public static String toStringState(TileState state){
        switch (state){
            case IDLE:
                return "IDLE";
            case MATCHABLE:
                return "MATCHABLE";
            case ACTIVE:
                return "ACTIVE";
            case SELECTED:
                return "SELECTED";
            default:
                break;
        }
        return null;
    }//end of toStringState

    public static String toStringVisible(TileVisible view){
        switch (view){
            case SHOW:
                return "SHOW";
            case HIDE:
                return "HIDE";
            default:
                break;
        }
        return null;
    }//end of toStringVisible
}//End of class
