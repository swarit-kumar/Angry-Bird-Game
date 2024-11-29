package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Trajectory {

    // Method to draw the parabolic trajectory
    public static void draw(ShapeRenderer shapeRenderer, Vector2 start, Vector2 velocity, float gravity, int steps) {
        float timeStep = 0.1f;  // Adjust the time step for smoother trajectory

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);  // Use Line to draw continuous trajectory
        for (int i = 0; i < steps; i++) {
            float t1 = i * timeStep;
            float t2 = (i + 1) * timeStep;
            Vector2 point1 = calculateTrajectoryPoint(start, velocity, gravity, t1);
            Vector2 point2 = calculateTrajectoryPoint(start, velocity, gravity, t2);
            shapeRenderer.line(point1.x, point1.y, point2.x, point2.y);  // Draw line segment between points
        }
        shapeRenderer.end();
    }

    // Method to calculate a point on the trajectory
    private static Vector2 calculateTrajectoryPoint(Vector2 start, Vector2 velocity, float gravity, float time) {
        float x = start.x + velocity.x * time;
        float y = start.y + velocity.y * time - 0.5f * gravity * time * time;
        return new Vector2(x, y);
    }

    // Method to draw a straight aiming line
    public static void drawAimingLine(ShapeRenderer shapeRenderer, Vector2 start, Vector2 end) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(start, end);
        shapeRenderer.end();
    }
}
