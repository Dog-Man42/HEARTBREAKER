package Heartbreaker.scenes.dungeonGen;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameWindow;
import Heartbreaker.engine.vectors.Vector2;

import java.awt.*;
import java.io.DataInput;
import java.util.*;

public class Generator {

    public class RectBounds {

        int width;
        int height;

        int minX;
        int minY;
        int maxX;
        int maxY;

        Vector2 position;

        public RectBounds(Vector2 position, int width, int height){
            this.position = position;
            this.width = width;
            this.height = height;
            this.minX = (int) position.x;
            this.minY = (int) position.y;
            this.maxX = minX + width;
            this.maxY = minY + height;
        }

    }

    class Room {


        Vector2 position = null;

        int width = 0;
        int height = 0;
        Vector2 min = null;
        Vector2 max = null;

        private Vector2[] interior = null;
        private Vector2[] exterior = null;

        public Room(Vector2 position, int width, int height){
            this.width = width;
            this.height = height;
            
            int halfWidth = (int) Math.ceil(width / 2.0);
            int halfHeight = (int) Math.ceil(height / 2.0);

            min = new Vector2(position.x - halfWidth, position.y - halfHeight);
            max = new Vector2(position.x + (halfWidth - 1), position.y + (halfHeight - 1));

            exterior = new Vector2[2 * width + 2 * height];
            interior = new Vector2[width * height];

            
            int extIndex = 0;
            int inIndex = 0;
            for(int y = (int) min.y; y < (int) max.y; y++){
                for(int x = (int) min.x; x < (int) max.x; x++){
                    if(x == (int) min.x || x == (int) max.x || y == (int) min.y || y == (int) max.y){
                        exterior[extIndex] = new Vector2(x,y);
                        extIndex++;
                    } else {
                        interior[inIndex] = new Vector2(x,y);
                    }
                }
            }
            System.out.print("ASD");


        }



        public static boolean intersect(Room a, Room b){
            return (a.min.x < b.max.x &&
                    b.min.x < a.max.x &&
                    a.min.y < b.max.y &&
                    b.min.y < a.max.y);
        }
    }


    CellGrid grid;
    Random random;
    ArrayList<Room> rooms;
    public Generator(CellGrid grid){
        this.grid = grid;
        random = new Random();
        rooms = new ArrayList<>();
    }



    public void placeRooms(){
        for( int i = 0; i < 40; i++){
            Vector2 position = new Vector2(random.nextInt(0, grid.getWidth()),random.nextInt(0, grid.getHeight()));
            Vector2 size = new Vector2(random.nextInt(2, 11),random.nextInt(2, 11));

            boolean add = true;
            Room newRoom = new Room(position, (int) size.x, (int) size.y);
            Room buffer = new Room(Vector2.difference(position, new Vector2(2,2)),(int) (size.x + 4), (int) (size.y + 4));

            //test for intersections
            for(Room room : rooms){
                if(Room.intersect(room,buffer)){
                    add = false;
                    break;
                }
            }

            //test if in bounds
            if(newRoom.min.x <= 0 || newRoom.max.x >= grid.getWidth() || newRoom.min.y <= 0 || newRoom.max.y >= grid.getHeight()){
                add = false;
            }

            if(add){
                rooms.add(newRoom);
                buildRoom(newRoom);
            }

        }
    }

    private void buildRoom(Room room){

        //Create Room

        Color color = new Color(.3f,.3f,.3f);
        Cell[] roomCells = new Cell[(room.width) * (room.height)];
        Cell[] exteriorCells = new Cell[2 * room.width + 2 * room.height];
        int n = 0;
        int ex = 0;
        for(int y = (int) room.min.y; y <= (int) room.max.y; y++){
            for(int x = (int) room.min.x; x <= (int) room.max.x; x++){
                if(grid.inBounds(x,y)){

                    Cell cell = grid.getCell(x,y);
                    Cell newCell = new Cell(1,cell.getCellID());
                    newCell.setNorthNeighbor(cell.getNorthNeighbor());
                    newCell.setEastNeighbor(cell.getEastNeighbor());
                    newCell.setSouthNeighbor(cell.getSouthNeighbor());
                    newCell.setWestNeighbor(cell.getWestNeighbor());
                    newCell.setColor(color);
                    newCell.setProperty("type",1);
                    newCell.setX(cell.getX());
                    newCell.setY(cell.getY());

                    boolean isExterior = ( (x == room.min.x && newCell.getWestNeighbor() != null) ||
                            (x == room.max.x && newCell.getEastNeighbor() != null) ||
                            (y == room.min.y && newCell.getNorthNeighbor() != null) ||
                            (y == room.max.y && newCell.getSouthNeighbor() != null) );
                    if(isExterior) {
                        exteriorCells[ex] = newCell;
                        ex++;
                    }

                    grid.setCell(x,y,newCell);
                    roomCells[n] = newCell;
                    n++;

                }

            }
        }

        //create a door
        boolean doorCreated = false;
        while(!doorCreated){
            Cell door = exteriorCells[random.nextInt(0,exteriorCells.length)];
            if(door != null) {
                door.setProperty("type", 2);
                door.setProperty("isDoor",1);
                door.setColor(Color.GREEN);
                //room.setDoorPosition(new Vector2(door.getX(),door.getY()));
                doorCreated = true;
            }
        }

    }

    public void calculateAverageDistance(){
        if(!rooms.isEmpty()){
            for(int y = 0; y < grid.getHeight(); y++){
                for(int x = 0; x < grid.getWidth(); x++){
                    Cell cell = grid.getCell(x,y);
                    if(cell.getProperty("type") == 0) {
                        double[] distances = new double[rooms.size()];
                        double[] weights = new double[rooms.size()];
                        double[] weights2 = new double[rooms.size()];
                        int result = 0;
                        int i = 0;
                        for (Room room : rooms) {
                            //calculate the manhattan distance from the cell at x,y to the door
                            int doorX = (int) room.position.x;
                            int doorY = (int) room.position.y;
                            double dist = Math.abs(x - doorX) + Math.abs(y - doorY);
                            double inverseDist = 100/(dist * dist);
                            distances[i] = dist;
                            weights[i] = inverseDist;
                            weights2[i] = dist;
                            i++;
                        }
                        result = (int) weightedAverage(distances,weights);
                        result = (int) (.6 * result + .4 * weightedAverage(distances,weights2));


                        cell.setProperty("distance", result);

                        if (result > 255) {
                            result = 255;
                        }
                        cell.setColor(new Color(result, result, result));
                    }
                }
            }
        }
    }

    private double weightedAverage(double[] elements, double[] weights){
        double numerator = 0;
        for(int i =0; i < elements.length; i++){
            numerator += elements[i] * weights[i];
        }
        double denominator = 0;
        for(int i = 0; i < elements.length; i++){
            denominator += weights[i];
        }

        return numerator / denominator;
    }

}
