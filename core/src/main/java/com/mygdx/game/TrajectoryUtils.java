package com.mygdx.game;
import com.badlogic.gdx.math.Vector2;

public class TrajectoryUtils {
    public static Vector2 getTrajectoryPoint(Vector2 startingPosition, Vector2 startingVelocity, float n, float gravity) {
        float t = 1 / 60.0f; // seconds per time step (assuming 60fps)
        Vector2 stepVelocity = new Vector2(startingVelocity).scl(t); // m/s
        Vector2 stepGravity = new Vector2(0, gravity * t * t); // m/s/s

        return startingPosition.cpy().add(stepVelocity.scl(n)).add(stepGravity.scl(0.5f * (n*n + n)));
    }
}
