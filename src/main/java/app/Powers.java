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
import com.badlogic.gdx.math.Vector2;
import ecs.components.*;
import ecs.components.collision.CollisionComponent;
import ecs.systems.*;
import managers.CollisionComputer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        cube.add(new CollisionComponent(16));
        cube.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        cube.add(new TeamComponent("Neutrals"));
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

        createWall(new Vector2(200, 100), 100, 1, "Neutrals");
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

    public void createWall(Vector2 position, int width, int height, String team) {
        // Создаем пул потоков для выполнения задач
        int numThreads = Runtime.getRuntime().availableProcessors(); // Определение количества доступных процессоров
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int x = i;
                final int y = j;

                // Передаем задачу в пул потоков
                executor.submit(() -> Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        createCube(position, x, y, team);
                    }
                }));
            }
        }

        // Завершаем работу пула потоков после выполнения всех задач
        executor.shutdown();
    }

    private void createCube(Vector2 position, int x, int y, String team) {
        Entity cube = new Entity();
        SpriteComponent sprite = new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png")));
        cube.add(new PositionComponent(position.x + (x * sprite.sprite.getWidth()), position.y + (y * sprite.sprite.getHeight())));
        cube.add(new VelocityComponent(0, 0));
        cube.add(sprite);
        cube.add(new CollisionComponent(8));
        cube.add(new TeamComponent(team));
        engine.addEntity(cube);
    }
}
