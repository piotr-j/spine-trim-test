package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.AttachmentLoader;

public class MyGdxGame extends ApplicationAdapter {
	PolygonSpriteBatch batch;
	ExtendViewport viewport;
	OrthographicCamera camera;
	TextureAtlas atlas;

	SkeletonRenderer renderer;
	Skeleton skeleton;
	AnimationState state;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(640, 480, camera);
		batch = new PolygonSpriteBatch();

		atlas = new TextureAtlas(Gdx.files.internal("out/skeleton.atlas"));
		renderer = new SkeletonRenderer();
		AttachmentLoader loader = new AtlasAttachmentLoader(atlas);
		SkeletonJson skeletonJson = new SkeletonJson(loader);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("out/skeleton.json"));

		skeleton = new Skeleton(skeletonData);
		skeleton.setSkin("base");
		state = new AnimationState(new AnimationStateData(skeletonData));
		state.setAnimation(0, "animation", true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.justTouched()) {
            if (skeleton.getSkin().getName().equals("base")) {
                skeleton.setSkin("alt");
            } else {
                skeleton.setSkin("base");
            }
        }

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		state.update(Gdx.graphics.getDeltaTime());
		state.apply(skeleton);
		skeleton.setPosition(camera.viewportWidth/2f, camera.viewportHeight/2f);
		skeleton.updateWorldTransform();
		renderer.draw(batch, skeleton);

		batch.end();
	}

	@Override public void resize (int width, int height) {
		super.resize(width, height);
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {
		batch.dispose();
		atlas.dispose();
	}
}
