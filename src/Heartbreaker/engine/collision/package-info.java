
/**
 * Contains all classes responsible for collision logic and managing colliders. All collision besides Circle-Circle uses the Separating Axis Theorem.
 * As of now does not currently properly support convex polygons and has some issues regarding collision resolution.
 * Eventually will include a class which handles polygon triangulation for proper convex collision.
 *
 * @author tuckt
 */

package Heartbreaker.engine.collision;