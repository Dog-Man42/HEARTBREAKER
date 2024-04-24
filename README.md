HEARTBREAKER
====
A simple 2D Bullet Hell game in its early stages that I work on as a small project during spare time.

Currently working on improving collison and physics. Next focuses will be on the UI, audio loading, and enabling the creation of levels with relative ease.

Currently there are some slight issues regarding player collision resolution. If the player collides with the "shield circles" they can either be pushed away from the circle predictably, or into it and move somewhat irratically for a short amount of time.


## Controls:
 - LMB: Shoot Bullet
 - RMB: Shoot Grav Bullet
 - A: Rotate Clockwise (Left with Shift)
 - D: Rotate Counterclockwise (Right with Shift)
 - W: Move Inwards (Up with Shift)
 - S: Move Outwards (Down with Shift)
 - Shift: Enables precise WASD controls

## Physics Scene

A testing scene created to test and experiment with the collision system. Entered by pressing Slash (/) in the main menu.

### Controls
- Slash (/): Return to Main Menu
- LMB (Hold): Drag the camera
- LMD: Click on a circle to follow it. Click again anywhere to unfollow
- Scoll: Zoom in and out
- Arrow Keys: Move camera
- Minus (-): Zoom out
- Equals (=): Zoom in

## How to Build and Run
Using a command line
* ` ...\src> javac  Heartbreaker/main/*.java Heartbreaker/engine/*.java Heartbreaker/objects/*.java Heartbreaker/objects/*.java Heartbreaker/scenes/*.java`

* `...\src> java Heartbreaker.heartbreaker.Heartbreaker`

Alternatively, you can use IntelliJ to build.

### Command line arguments
HEARTBREAKER has two command line arguments
 - -DEBUG -> Shows a frametime graph and shows links between physics bodies in Physics Scene
 - -FPS: -> Sets the FPS instead of the default of 60. Example: FPS:120
