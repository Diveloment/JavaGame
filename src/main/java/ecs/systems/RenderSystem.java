package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.PositionComponent;
import ecs.components.TextureComponent;

public class RenderSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<TextureComponent> renderMapper;
    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get());
        this.batch = batch;
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        renderMapper = ComponentMapper.getFor(TextureComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        TextureComponent render = renderMapper.get(entity);

        // Отрисовка игрока
        batch.draw(render.texture, position.x, position.y);

        // Другие операции отрисовки, если необходимо
    }
}

