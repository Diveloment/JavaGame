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
import ecs.systems.PlayerControlSystem;
import ecs.systems.RenderSystem;
import ecs.systems.SpriteRenderSystem;

public class Powers extends ApplicationAdapter {

    OrthographicCamera camera;
    OrthographicCamera textCamera;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    BitmapFont font;
    Engine engine;

    @Override
    public void create () {
        engine = new Engine();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new SpriteRenderSystem(batch));

        Entity playerEntity = new Entity();
        PlayerComponent playerComponent = new PlayerComponent();
        playerComponent.maxSpeed = 250;
        playerEntity.add(playerComponent);
        playerEntity.add(new PositionComponent(0, 0));
        playerEntity.add(new VelocityComponent(0, 0));
        playerEntity.add(new SpriteComponent(new Texture(Gdx.files.internal("assets/cube.png"))));
        engine.addEntity(playerEntity);
    }

    @Override
    public void render () {
        // Очистка экрана
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Обновление игры
        float deltaTime = Gdx.graphics.getDeltaTime();
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
