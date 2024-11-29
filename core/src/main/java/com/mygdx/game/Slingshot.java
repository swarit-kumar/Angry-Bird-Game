package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import java.io.Serializable;

public class Slingshot implements Serializable, Disposable {
    private static final long serialVersionUID = 1L;
    private Vector2 position;
    private Texture slingshotTexture;
    private World world;
    private float width = 100f;  // Example width
    private float height = 50f;  // Example height

    public Slingshot(Vector2 position, World world) {
        this.position = position;
        this.slingshotTexture = new Texture("catapult.png");
        this.world = world;
    }

    public void draw(SpriteBatch batch) {
        // Ensure the slingshot texture is drawn correctly with the intended width and height
        batch.draw(slingshotTexture, position.x, position.y, width, height);
    }

    public void launchBird(Birds bird, Vector2 launchVector) {
        if (bird.getBody() != null) {
            bird.getBody().setLinearVelocity(launchVector); // Set the Box2D velocity
            bird.getBody().setActive(true); // Activate the bird's body
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        slingshotTexture.dispose(); // Dispose texture to free resources
    }

    public boolean contains(float x, float y) {
        // Check if the point (x, y) is inside the slingshot's rectangle
        return x >= position.x && x <= position.x + width &&
            y >= position.y && y <= position.y + height;
    }

    public float getX() {
        return getPosition().x;
    }
    public float getY() {
        return getPosition().y;
    }

    public void setPosition(float slingshotX, float slingshotY) {
        this.position.x = slingshotX;
        this.position.y = slingshotY;
    }
}
