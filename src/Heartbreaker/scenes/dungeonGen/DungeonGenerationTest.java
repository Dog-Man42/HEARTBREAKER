package Heartbreaker.scenes.dungeonGen;

import Heartbreaker.engine.input.KeyInput;
import Heartbreaker.engine.scenes.DefaultTestScene;
import Heartbreaker.main.Heartbreaker;

import java.awt.event.KeyEvent;

public class DungeonGenerationTest extends DefaultTestScene {


    private CellGrid grid;
    private Generator generator;

    private int cooldown = 60;

    private int xSize = 200;
    private int ySize = 200;
    private int index = 0;
    @Override
    public boolean initialize(){
        super.initialize();
        camera.setZoom(.3);
        grid = new CellGrid(xSize,ySize);
        addObject(grid);
        Heartbreaker.stopTrack();
        generator = new Generator(grid);
        generator.placeRooms();
        return true;
    }

    @Override
    public void updateScene(double delta){
        super.updateScene(delta);

        if(cooldown <= 0){
            if(KeyInput.isKeyPressed(KeyEvent.VK_G)){
                removeObject(grid);
                grid = new CellGrid(xSize,ySize);
                addObject(grid);
                generator = new Generator(grid);
                generator.placeRooms();
                cooldown = 30;
            }
            if(KeyInput.isKeyPressed(KeyEvent.VK_H)){
                removeObject(grid);
                grid = new CellGrid(xSize,ySize);
                addObject(grid);
                generator = new Generator(grid);
                generator.placeRooms();
                generator.calculateAverageDistance();
                cooldown = 30;
            }
            if(KeyInput.isKeyPressed(KeyEvent.VK_O)){
                grid.printGrid();
                cooldown = 30;
            }


        } else {
            cooldown--;
        }

/*
        removeObject(grid);
        grid = new CellGrid(100,100);
        addObject(grid);
*/

    }
}
