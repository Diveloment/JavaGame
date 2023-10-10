package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.PositionComponent;
import ecs.components.SpriteComponent;
import ecs.components.TextureComponent;

public class SpriteRenderSystem extends IteratingSystem {
    private ComponentMapper<SpriteComponent> renderMapper;
    private SpriteBatch batch;

    public SpriteRenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class).get());
        this.batch = batch;
        renderMapper = ComponentMapper.getFor(SpriteComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent render = renderMapper.get(entity);

        // Отрисовка игрока
        render.sprite.draw(batch);

        // Другие операции отрисовки, если необходимо
    }
}

