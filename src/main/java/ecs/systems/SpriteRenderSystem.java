package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.PositionComponent;
import ecs.components.RotationComponent;
import ecs.components.SpriteComponent;

import java.util.Objects;

public class SpriteRenderSystem extends IteratingSystem {
    private ComponentMapper<SpriteComponent> renderMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private SpriteBatch batch;

    public SpriteRenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class, PositionComponent.class).get());
        this.batch = batch;
        renderMapper = ComponentMapper.getFor(SpriteComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent render = renderMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);

        render.sprite.setPosition(position.x - render.sprite.getWidth() / 2, position.y - render.sprite.getHeight() / 2);
        if (Objects.nonNull(entity.getComponent(RotationComponent.class))) {
            RotationComponent rotation = entity.getComponent(RotationComponent.class);
            render.sprite.setRotation(rotation.a);
        }
        render.sprite.draw(batch);
    }
}

