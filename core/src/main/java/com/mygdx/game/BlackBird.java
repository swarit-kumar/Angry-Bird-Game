package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BlackBird extends Birds {
    Body body;
    public BlackBird(Texture texture, float x, float y, int impactPower, World world) {
        super(texture, x, y, world, impactPower);
        createBody(world, x, y); // Create the body upon instantiation
    }

    @Override
    public void onImpact(Pig pig, Block block) {
        // Apply damage to pig and block
        if (pig != null) {
            pig.takeDamage(impactPower); // Direct impact damage to pig
        }
        if (block != null) {
            block.takeDamage(impactPower); // Direct impact damage to block
        }

        // Explosion logic (e.g., area damage)
//        applyExplosionEffect();
    }
//
//    private void applyExplosionEffect() {
//        // Define explosion radius and position
//        System.out.println("BlackBird exploded! Dealing area damage.");
//        float explosionRadius = 50f;  // Define the explosion radius
//        Vector2 position = getPosition(); // Get the current position of the BlackBird
//
//        // Find bodies in the explosion radius
//        for (Body body : getBodiesInExplosionRadius(position, explosionRadius)) {
//            if (body.getUserData() instanceof Pig) {
//                ((Pig) body.getUserData()).takeDamage(impactPower); // Apply damage to pig
//            } else if (body.getUserData() instanceof Block) {
//                ((Block) body.getUserData()).takeDamage(impactPower); // Apply damage to block
//            }
//        }
//        System.out.println("Explosion at position: " + position);
//    }

//    private Iterable<Body> getBodiesInExplosionRadius(Vector2 position, float radius) {
//        // Logic to get bodies within the radius (you can use Box2D's AABB query for that)
//        Array<Body> bodiesInRadius = new Array<>();
//
//        // Define the area to query (bounding box for explosion)
//        AABB aabb = new AABB(position.x - radius, position.y - radius, position.x + radius, position.y + radius);
//
//        // Query the world and get bodies within the explosion radius
//        world.getBodies(bodiesInRadius, aabb);
//
//        return bodiesInRadius;
//    }

    @Override
    public void update(float delta) {
        // Update the position of the bird using Box2D body
        if (getBody() != null) {
            Vector2 bodyPosition = getBody().getPosition();
            setPosition(bodyPosition.x * GameConstants.PPM, bodyPosition.y * GameConstants.PPM); // Scale by PPM if needed
        }
    }

    @Override
    public void createBody(World world) {
        // Optionally implement custom body creation logic (called by the constructor)
        // This method is left empty as we're using createBody(World world, float x, float y)
    }

    @Override
    public void createBody(World world, float x, float y) {
        // Create the Box2D body for BlackBird (similar to RedBird but customized for BlackBird)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameConstants.PPM, y / GameConstants.PPM); // Proper scaling with PPM

        // Define the shape (using a circle for simplicity)
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / GameConstants.PPM);  // Set radius with scaling

        // Define the fixture (collision properties)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.5f;  // Bounciness factor

        // Create the body in the Box2D world
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        // Set the body reference for the bird
        setBody(body);

        // Cleanup
        shape.dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw the bird using its sprite
        super.draw(batch);
    }

    public void launch(Vector2 force) {
        // Apply a force to launch the BlackBird (similar to RedBird)
        if (getBody() != null) {
            getBody().applyForceToCenter(force, true);
            getBody().setLinearVelocity(force); // Set velocity directly
            getBody().setActive(true);
        }
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y); // Set position using parent class's setPosition method
    }

    public Body getBody() {
        return body; // Return the Box2D body of the BlackBird
    }
}
