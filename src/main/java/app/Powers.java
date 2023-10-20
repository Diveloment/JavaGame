package app;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import ecs.components.*;
import ecs.components.collision.CollisionComponent;
import ecs.components.physics.PhysicsComponent;
import ecs.systems.*;
import managers.CollisionComputer;
import managers.ResourceManager;
import models.PhysicsWorldModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Powers extends ApplicationAdapter {

    public static final float DEFAULT_SCALING = 16.0f;

    OrthographicCamera camera;
    OrthographicCamera textCamera;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    BitmapFont font;
    Engine engine;
    CollisionComputer collisionComputer;
    PhysicsWorldModel physics;
    Box2DDebugRenderer debugRenderer;
    ResourceManager resourceManager;

    @Override
    public void create () {
        physics = PhysicsWorldModel.getInstance();
        engine = new Engine();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
        textCamera = new OrthographicCamera();
        textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        collisionComputer = CollisionComputer.getInstance(engine);
        resourceManager = new ResourceManager();

        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new HardpointsSystem());
        engine.addSystem(new WeaponSystem(engine));
        engine.addSystem(new BulletSystem(engine));
        engine.addSystem(new HealthSystem(engine));
        engine.addSystem(new StatusSystem());
        engine.addSystem(new SpriteRenderSystem(batch));

        Entity cube = new Entity();
        cube.add(new PositionComponent(100, 0));
        cube.add(new VelocityComponent(0, 10));
        cube.add(new PhysicsComponent(physics, 8));
        cube.add(new CollisionComponent(16));
        cube.add(new HealthComponent(100.0f));
        cube.add(new SpriteComponent((Sprite) resourceManager.getResource("assets/cube.png", Sprite.class)));
        cube.add(new TeamComponent("Neutrals"));
        engine.addEntity(cube);

        Entity playerEntity = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(camera);
        playerComponent.maxSpeed = 200;
        PhysicsComponent physicsComponent = new PhysicsComponent(physics);
        playerEntity.add(physicsComponent);
        playerEntity.add(playerComponent);
        playerEntity.add(new PositionComponent(0, 0));
        playerEntity.add(new RotationComponent(0));
        playerEntity.add(new VelocityComponent(0, 0));
        playerEntity.add(new CollisionComponent());
        playerEntity.add(new SpriteComponent((Sprite) resourceManager.getResource("assets/cube.png", Sprite.class)));
        playerEntity.add(new TeamComponent("Players"));

        Entity playerWeapon = new Entity();
        WeaponComponent weaponComponent = new WeaponComponent(25, 25);
        weaponComponent.phase = 0.2f;
        playerWeapon.add(weaponComponent);
        playerWeapon.add(new PositionComponent(0, 0));
        playerWeapon.add(new RotationComponent(0));
        playerWeapon.add(new AimPointComponent(0, 0));
        playerWeapon.add(new SpriteComponent((Sprite) resourceManager.getResource("assets/cube.png", Sprite.class)));
        engine.addEntity(playerWeapon);
        Entity playerWeapon2 = new Entity();
        playerWeapon2.add(new WeaponComponent(25, -25));
        playerWeapon2.add(new PositionComponent(0, 0));
        playerWeapon2.add(new RotationComponent(0));
        playerWeapon2.add(new AimPointComponent(0, 0));
        playerWeapon2.add(new SpriteComponent((Sprite) resourceManager.getResource("assets/cube.png", Sprite.class)));
        engine.addEntity(playerWeapon2);

        HardpointsComponent hardpointsComponent = new HardpointsComponent();
        hardpointsComponent.weapons.add(playerWeapon);
        hardpointsComponent.weapons.add(playerWeapon2);

        playerEntity.add(hardpointsComponent);
        engine.addEntity(playerEntity);

        createWall(new Vector2(200, 100), 100, 1, "Neutrals", true);
    }

    @Override
    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        physics.update(1/60f);
        // Очистка экрана
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(physics.world, camera.combined.scl(DEFAULT_SCALING));

        // Обновление игры
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

    public void createWall(Vector2 position, int width, int height, String team, boolean isPhysics) {
        // Создаем пул потоков для выполнения задач
        int numThreads = Runtime.getRuntime().availableProcessors(); // Определение количества доступных процессоров
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int x = i;
                final int y = j;
                executor.submit(() -> Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        createCube(position, x, y, team, isPhysics);
                    }
                }));
            }
        }
        executor.shutdown();
    }

    private void createCube(Vector2 position, int x, int y, String team, boolean isPhysics) {
        Entity cube = new Entity();
        SpriteComponent sprite = new SpriteComponent((Sprite) resourceManager.getResource("assets/cube.png", Sprite.class));
        cube.add(new PositionComponent(position.x + (x * sprite.sprite.getWidth()), position.y + (y * sprite.sprite.getHeight())));
        cube.add(new VelocityComponent(0, 0));
        cube.add(sprite);
        cube.add(new CollisionComponent(8));
        cube.add(new HealthComponent(30.f));
        cube.add(new TeamComponent(team));
        if (isPhysics) {
            PhysicsComponent physicsComponent = PhysicsComponent.buildKinematicBody(physics, 8.0f);
            physicsComponent.body.setTransform(cube.getComponent(PositionComponent.class).getPosition().scl(1/DEFAULT_SCALING), 0);
            cube.add(physicsComponent);
        }
        engine.addEntity(cube);
    }
}
