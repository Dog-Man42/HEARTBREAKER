package Heartbreaker.scenes.dungeonGen;

import java.awt.*;
import java.util.HashMap;

public class Cell {


    private Cell northNeighbor = null;
    private Cell eastNeighbor = null;
    private Cell southNeighbor = null;
    private Cell westNeighbor = null;

    private int cellID = 0;
    private int x = 0;
    private int y = 0;

    private Color color = null;


    private HashMap<String, Integer> properties = new HashMap<>();

    Cell(int prop) {
        setProperty("type", prop);
    }

    Cell(int prop, int ID) {
        setProperty("type", prop);
        cellID = ID;
    }

    public void setProperty(String prop, int value) {
        properties.put(prop, value);
    }

    public int getProperty(String prop) {
        if (!properties.containsKey(prop)) {
            properties.put(prop, 0);
            return 0;
        } else {
            return properties.get(prop);
        }
    }

    public int getCellID() {
        return cellID;
    }

    public void setCellID(int cellID) {
        this.cellID = cellID;
    }

    public Cell getNorthNeighbor() {
        return northNeighbor;
    }

    public void setNorthNeighbor(Cell northNeighbor) {
        this.northNeighbor = northNeighbor;
    }

    public Cell getEastNeighbor() {
        return eastNeighbor;
    }

    public void setEastNeighbor(Cell eastNeighbor) {
        this.eastNeighbor = eastNeighbor;
    }

    public Cell getSouthNeighbor() {
        return southNeighbor;
    }

    public void setSouthNeighbor(Cell southNeighbor) {
        this.southNeighbor = southNeighbor;
    }

    public Cell getWestNeighbor() {
        return westNeighbor;
    }

    public void setWestNeighbor(Cell westNeighbor) {
        this.westNeighbor = westNeighbor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#" + cellID + " Properties[" + properties.toString() + "] Neighbors[");
        if (northNeighbor != null) {
            sb.append(northNeighbor.getCellID() + ", ");
        } else {
            sb.append("null, ");
        }
        if (eastNeighbor != null) {
            sb.append(eastNeighbor.getCellID() + ", ");
        } else {
            sb.append("null, ");
        }
        if (southNeighbor != null) {
            sb.append(southNeighbor.getCellID() + ", ");
        } else {
            sb.append("null, ");
        }
        if (westNeighbor != null) {
            sb.append(westNeighbor.getCellID() + "]");
        } else {
            sb.append("null]");
        }
        return sb.toString();
    }
}
