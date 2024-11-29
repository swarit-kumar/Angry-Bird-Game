package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class YellowBird extends Birds {
    private boolean isBoosted;
    private boolean isLaunched; // Track if the bird has been launched
    private float boostDuration;
    private float elapsedBoostTime;
    private Body body;

    public YellowBird(Texture texture, float x, float y, int impactPower, World world) {
        super(texture, x, y, world, impactPower);
        this.isBoosted = false;
        this.isLaunched = false; // Initially, the bird is not launched
        this.boostDuration = 0.5f;
        this.elapsedBoostTime = 0;
    }

    @Override
    public void update(float delta) {
        if (isLaunched) {
            // Log touch detection
            if (Gdx.input.justTouched()) {
                System.out.println("Screen touched, trying to apply boost...");
                if (!isBoosted) {
                    applyBoost();
                }
            }

            // If boost is active, count down the time
            if (isBoosted) {
                elapsedBoostTime += delta;
                if (elapsedBoostTime >= boostDuration) {
                    // Reset boost after the duration
                    isBoosted = false;
                    Body body = getBody();
                    if (body != null) {
                        body.setLinearVelocity(body.getLinearVelocity().scl(1 / 2.0f));  // Scale the velocity back down
                    }
                }
            }
        }
    }

    private void applyBoost() {
        // Boost the velocity by applying an impulse to the body
        Body body = getBody();
        if (body != null) {
            Vector2 boostDirection = body.getLinearVelocity().cpy().nor();  // Normalize the velocity direction
            float boostFactor = 2.0f; // Adjust boost factor as needed
            body.applyLinearImpulse(boostDirection.scl(boostFactor), body.getWorldCenter(), true);  // Apply impulse to the body
            isBoosted = true; // Boost happens only once
            elapsedBoostTime = 0;  // Reset the timer for the boost duration
            System.out.println("Boost applied!");
        }
    }

    @Override
    public void createBody(World world) {
        // Optionally, this method is left empty if you don't need to implement anything specific
    }

    @Override
    public void createBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameConstants.PPM, y / GameConstants.PPM); // Proper scaling with PPM

        CircleShape shape = new CircleShape();
        shape.setRadius(25 / GameConstants.PPM);  // Set radius with scaling

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.5f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    // Overridden method to check if the bird has been launched
    public boolean isLaunched() {
        return isLaunched; // Explicitly use this to check if the bird is launched
    }

    public void setLaunched() {
        this.isLaunched = true;  // Set the bird as launched
        System.out.println("Bird launched!");
    }

//    @Override
//    public void onImpact(Pig pig, Block block) {
//        if (pig != null) pig.takeDamage(getImpactPower());
//        if (block != null) block.takeDamage(getImpactPower());
//    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);  // Use the superclass method to draw the sprite
    }

    // Method to update the launched state after the bird is launched
    public Body getBody() {
        return body;  // Access the Box2D body from the superclass
    }
}

