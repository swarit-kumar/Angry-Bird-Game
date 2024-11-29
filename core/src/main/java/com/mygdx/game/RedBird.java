package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class RedBird extends Birds {

    private Body body;

    public RedBird(Texture texture, float x, float y, boolean launched, int impactPower, World world) {
        super(texture, x, y, world, impactPower);
        setPosition(x, y);
    }

    @Override
    public void createBody(World world) {

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

    @Override
    public void update(float delta) {
        // Update the bird's position based on its Box2D body
        if (getBody() != null) {
            Vector2 bodyPosition = getBody().getPosition();
            setPosition(bodyPosition.x * GameConstants.PPM, bodyPosition.y * GameConstants.PPM); // Scale by PPM if needed
        }
    }


    @Override
    public void onImpact(Pig pig, Block block) {
        System.out.println("RedBird impacts with power: " + impactPower);

        if (pig != null) {
            pig.takeDamage(impactPower);
        }

        if (block != null) {
            block.takeDamage(impactPower);
        }
    }

    @Override
    public void launch(Vector2 force) {
        body.applyForceToCenter(force, true);
        body.setLinearVelocity(force);
        body.setActive(true);
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public Body getBody() {
        return body;
    }
}
