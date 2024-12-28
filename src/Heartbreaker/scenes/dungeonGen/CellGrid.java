package Heartbreaker.scenes.dungeonGen;

import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.vectors.Vector2;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.NoSuchElementException;
import java.util.Random;

public class CellGrid extends GameObject {

    private Cell[][] cells;
    private int cooldown = 60;


    public CellGrid(int cellsX, int cellsY){
        if(cellsX < 1){
            cellsX = 1;
        }
        if(cellsY < 1){
            cellsY = 1;
        }
        cells = new Cell[cellsX][cellsY];

        //create cells
        Random rand = new Random(System.currentTimeMillis());
        int n = 0;
        for(int y = 0; y < cells[0].length; y++){
            for(int x = 0; x < cells.length; x++){
                cells[x][y] = new Cell(0,n);
                cells[x][y].setX(x);
                cells[x][y].setY(y);
                n++;
            }
        }

        //create references
        for(int y = 0; y < cells[0].length; y++){
            for(int x = 0; x < cells.length; x++){
                Cell cell = cells[x][y];
                if(x - 1 > -1){
                    cell.setWestNeighbor(cells[x-1][y]);
                }
                if(x + 1 < cells.length){
                    cell.setEastNeighbor(cells[x+1][y]);
                }
                if(y - 1 > -1){
                    cell.setNorthNeighbor(cells[x][y-1]);
                }
                if(y + 1 < cells[x].length){
                    cell.setSouthNeighbor(cells[x][y+1]);
                }
            }
        }

    }

    public Cell getCell(int x, int y){
        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell){
        cells[x][y] = cell;
    }

    public boolean inBounds(int x, int y){
        return x > -1 && x < cells.length && y > -1 && y < cells[x].length;
    }

    public int getWidth(){
        return cells.length;
    }

    public int getHeight(){
        return cells[0].length;
    }

    public void printGrid(){
        System.out.print("\n\n\n");
        for(int y = 0; y < cells[0].length; y++){
            for(int x = 0; x < cells.length; x++){
                System.out.print(cells[x][y] + ", ");
            }
            System.out.print("\n");
        }
    }

    @Override
    public void update(double delta) {

    }





    @Override
    public void draw(Graphics2D g, double delta) {
        int cellSize = 20;
        double cellMult = getScene().getCamera().getZoom() * cellSize;
        Point2D.Double pos = getPositionCameraSpace();

        g.setColor(Color.MAGENTA);
        for(int y = 0; y < cells[0].length; y++){
            for(int x = 0; x < cells.length; x++) {
                Cell cell = cells[x][y];
                int prop = cell.getProperty("type");
                if (prop > 0 || cell.getColor() != null)  {
                    switch(prop) {
                        case 1 -> g.setColor(Color.RED);
                        case 2 -> g.setColor(Color.YELLOW);
                        case 3 -> g.setColor(Color.GREEN);
                        case 4 -> g.setColor(Color.CYAN);
                        case 5 -> g.setColor(Color.BLUE);
                    }
                    if(cell.getColor() != null){
                        g.setColor(cell.getColor());
                    }
                    g.fillRect((int) Math.floor(pos.x + (x * cellMult)), (int) Math.floor(pos.y + (y * cellMult)), (int) Math.ceil(cellMult), (int) Math.ceil(cellMult));
                }
            }
        }
    }


}
