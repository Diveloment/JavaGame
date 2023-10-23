package ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import models.PhysicsWorldModel;

import static app.Powers.DEFAULT_SCALING;

public class PhysicsComponent implements Component {
    public Body body;
    public boolean initialized = false;

    public PhysicsComponent(Body body) {
        this.body = body;
    }

    private PhysicsComponent() {
    }

    public PhysicsComponent(PhysicsWorldModel worldModel) {
        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // add it to the world
        Body bodyd = worldModel.world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        /*PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);*/
        CircleShape shape = new CircleShape();
        shape.setRadius(1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        this.body = bodyd;
    }

    public PhysicsComponent(PhysicsWorldModel worldModel, float scale) {
        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // add it to the world
        Body bodyd = worldModel.world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(scale / DEFAULT_SCALING, scale / DEFAULT_SCALING);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        this.body = bodyd;
    }

    public static PhysicsComponent buildKinematicBody(PhysicsWorldModel worldModel, float scale) {
        PhysicsComponent physicsComponent = new PhysicsComponent();

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        // add it to the world
        Body bodyd = worldModel.world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(scale / DEFAULT_SCALING, scale / DEFAULT_SCALING);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        physicsComponent.body = bodyd;
        return physicsComponent;
    }

    public static PhysicsComponent buildStaticBody(PhysicsWorldModel worldModel, float scale) {
        PhysicsComponent physicsComponent = new PhysicsComponent();

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // add it to the world
        Body bodyd = worldModel.world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(scale / DEFAULT_SCALING, scale / DEFAULT_SCALING);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        physicsComponent.body = bodyd;
        return physicsComponent;
    }
}

