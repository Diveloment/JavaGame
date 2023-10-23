package app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class PowersDemo extends ApplicationAdapter {

    OrthographicCamera camera;
    OrthographicCamera textCamera;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    BitmapFont font;
    Sprite playerSprite;
    Sprite cube;
    Texture powerSupplyAtlas;
    Animation<TextureRegion> powerSupplyAnimation;

    float X = 200;
    float Y = 100;

    float xSpeed = 2;
    float ySpeed = 1;

    float stateTime = 0f; // Время анимации

    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textCamera = new OrthographicCamera();
        textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        cube = new Sprite(new Texture(Gdx.files.internal("assets/cube.png")));
        cube.setPosition(100, 100);
        playerSprite = new Sprite(new Texture(Gdx.files.internal("assets/cube.png")));
        playerSprite.setScale(2.0f);

        powerSupplyAtlas = new Texture("assets/particle.png");
        TextureRegion[] animationFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            animationFrames[i] = new TextureRegion(powerSupplyAtlas, i * 32, 0, 32, 32);
        }
        float frameDuration = 0.03f; // Длительность каждого кадра анимации (в секундах)
        powerSupplyAnimation = new Animation<>(frameDuration, animationFrames);

    }

    @Override
    public void render () {

        float spriteX = playerSprite.getX();
        float spriteY = playerSprite.getY();

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        Vector3 worldCoordinates = new Vector3(mouseX, mouseY, 0);
        camera.unproject(worldCoordinates);

        float mouseWorldX = worldCoordinates.x;
        float mouseWorldY = worldCoordinates.y;


        float angle = MathUtils.atan2(mouseWorldY - spriteY, mouseWorldX - spriteX) * MathUtils.radiansToDegrees;

        playerSprite.setPosition(X, Y);
        playerSprite.setRotation(angle);

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(playerSprite.getX() + playerSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2, 0);
        camera.update();

        stateTime += Gdx.graphics.getDeltaTime(); // Обновление времени анимации
        Sprite powerSupplySprite = new Sprite(powerSupplyAnimation.getKeyFrame(stateTime));
        powerSupplySprite.setPosition(100, 200);
        powerSupplySprite.setScale(5);
        if (powerSupplyAnimation.isAnimationFinished(stateTime))
            stateTime = 0;

        shapeRenderer.setProjectionMatrix(camera.combined); // Настройка проекционной матрицы
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Начало рисования
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        powerSupplySprite.draw(batch);
        playerSprite.draw(batch);
        cube.draw(batch);
        batch.end();

        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();
        font.draw(batch, "Player pos: " + playerSprite.getX() + " " + playerSprite.getY(), 100, 200);
        font.draw(batch, "Cursor pos: " + mouseWorldX + " " + mouseWorldY, 100, 300);
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.A)) X -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.D)) X += 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.S)) Y -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.W)) Y += 200 * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
