package app;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ecs.components.*;
import ecs.components.collision.CollisionComponent;
import ecs.systems.*;
import managers.CollisionComputer;

public class Powers extends ApplicationAdapter {

    OrthographicCamera camera;
    OrthographicCamera textCamera;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    BitmapFont font;
    Engine engine;
    CollisionComputer collisionComputer;

    @Override
    public void create () {
        engine = new Engine();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textCamera = new OrthographicCamera();
        textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        collisionComputer = CollisionComputer.getInstance(engine);

        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new HardpointsSystem());
        engine.addSystem(new WeaponSystem(engine));
        engine.addSystem(new BulletSystem(engine));
        engine.addSystem(new SpriteRenderSystem(batch));

        Entity cube = new Entity();
        cube.add(new PositionComponent(100, 0));
        cube.add(new VelocityComponent(0, 0));
        cube.add(new CollisionComponent());
        cube.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        cube.add(new TeamComponent("Players"));
        engine.addEntity(cube);

        Entity playerEntity = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(camera);
        playerComponent.maxSpeed = 100;
        playerEntity.add(playerComponent);
        playerEntity.add(new PositionComponent(0, 0));
        playerEntity.add(new RotationComponent(0));
        playerEntity.add(new VelocityComponent(0, 0));
        playerEntity.add(new CollisionComponent());
        playerEntity.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        playerEntity.add(new TeamComponent("Players"));

        Entity playerWeapon = new Entity();
        WeaponComponent weaponComponent = new WeaponComponent(25, 25);
        weaponComponent.phase = 0.2f;
        playerWeapon.add(weaponComponent);
        playerWeapon.add(new PositionComponent(0, 0));
        playerWeapon.add(new RotationComponent(0));
        playerWeapon.add(new AimPointComponent(0, 0));
        playerWeapon.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        engine.addEntity(playerWeapon);
        Entity playerWeapon2 = new Entity();
        playerWeapon2.add(new WeaponComponent(25, -25));
        playerWeapon2.add(new PositionComponent(0, 0));
        playerWeapon2.add(new RotationComponent(0));
        playerWeapon2.add(new AimPointComponent(0, 0));
        playerWeapon2.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        engine.addEntity(playerWeapon2);

        HardpointsComponent hardpointsComponent = new HardpointsComponent();
        hardpointsComponent.weapons.add(playerWeapon);
        hardpointsComponent.weapons.add(playerWeapon2);

        playerEntity.add(hardpointsComponent);
        engine.addEntity(playerEntity);
    }

    @Override
    public void render () {
        // Очистка экрана
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Обновление игры
        float deltaTime = Gdx.graphics.getDeltaTime();
        collisionComputer.update();
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        engine.update(deltaTime);
        batch.end();
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
