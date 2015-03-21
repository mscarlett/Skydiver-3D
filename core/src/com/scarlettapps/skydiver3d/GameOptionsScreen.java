// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.scarlettapps.skydiver3d.resources.AssetFactory.MusicType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.MusicFactory;
import com.scarlettapps.skydiver3d.resources.PreferenceFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.worldstate.GameController;

public class GameOptionsScreen extends MenuScreen {

	private Label volumeValue;
	private Label sensitivityValue;
	
	public GameOptionsScreen(final Skydiver3D game) {
		super(game);
		
		FontFactory fontFactory = FontFactory.getInstance();
		final SoundFactory sound = SoundFactory.getInstance();
		final MusicFactory music = MusicFactory.getInstance();
		final PreferenceFactory preferences = PreferenceFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(20);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);

		Table table = this.table;
		table.defaults().spaceBottom(30);
		table.columnDefaults(0).padRight(20);
		Label title = new Label("Options", skin, "Title font", Color.WHITE);
		table.add(title).colspan(3);

		// create the labels widgets
		final CheckBox soundEffectsCheckbox = new CheckBox("", skin);
		soundEffectsCheckbox.setScale(2);
		soundEffectsCheckbox.setChecked(preferences.isSoundEnabled());
		soundEffectsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean enabled = soundEffectsCheckbox.isChecked();
				preferences.setSoundEnabled(enabled);
				sound.play(SoundType.CLICK);
				
				if (Skydiver3D.DEV_MODE) {
					Gdx.app.log(Skydiver3D.LOG, "Changing sound effects checkbox");
				}
			}
		});
		table.row();
		table.add("Sound Effects");
		table.add(soundEffectsCheckbox).colspan(2).left();

		final CheckBox musicCheckbox = new CheckBox("", skin);
		musicCheckbox.setScale(2);
		musicCheckbox.setChecked(preferences.isMusicEnabled());
		musicCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean enabled = musicCheckbox.isChecked();
				preferences.setMusicEnabled(enabled);
				sound.play(SoundType.CLICK);

				// if the music is now enabled, start playing the menu music
				if (enabled)
					music.play(MusicType.WIND);
				else
					music.stop();
				
				if (Skydiver3D.DEV_MODE) {
					Gdx.app.log(Skydiver3D.LOG, "Changing music checkbox");
				}
			}
		});
		table.row();
		table.add("Music");
		table.add(musicCheckbox).colspan(2).left();

		// range is [0.0,1.0]; step is 0.1f
		SliderStyle sliderStyle = skin.get(SliderStyle.class);
		Pixmap pixmap = new Pixmap(10, 10, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		Slider volumeSlider = new Slider(0f, 1f, 0.1f, true, sliderStyle);
		volumeSlider.setScaleX(6);
		volumeSlider.setValue(preferences.getVolume());
		volumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float value = ((Slider) actor).getValue();
				preferences.setVolume(value);
				music.updateVolume(value);
				updateVolumeLabel();
			}
		});

		// create the volume label
		volumeValue = new Label("50%", skin);
		updateVolumeLabel();

		// add the volume row
		table.row();
		table.add("Volume");
		table.add(volumeSlider);
		table.add(volumeValue).width(40);
		
		Slider sensitivitySlider = new Slider(0.5f, 1.5f, 0.1f, true, sliderStyle);
		sensitivitySlider.setScaleX(6);
		sensitivitySlider.setValue(preferences.getSensitivity());
		sensitivitySlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float value = ((Slider) actor).getValue();
				preferences.setSensitivity(value);
				GameController.setSensitivity(value);
				updateSensitivityLabel();
			}
		});

		// create the volume label
		sensitivityValue = new Label("100%", skin);
		updateSensitivityLabel();

		/*pixmap = new Pixmap(30, 30, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		sensitivityIndicator = new Image(new Texture(pixmap));
		sensitivityIndicator.setSize(30, 30);
		sensitivityIndicator.addAction(new Action() {
			
			GameController gameController = GameController.newGameController();
			float velocity = 0f;
			float x = 50f;
			
			@Override
			public boolean act(float delta) {
				gameController.update(delta);
				Image image = (Image)getActor();
				velocity += gameController.getAx()*delta;
				float dx = velocity*delta;
				x += dx*100;
				image.setX(x);
				return false;
			}
		});*/
		
		// add the volume row
		table.row();
		//Table group = new Table(skin);
		//group.add("Motion Sensitivity");
		//group.row();
		//group.add(sensitivityIndicator).align(Align.left);
		table.add("Motion Sensitivity");
		table.add(sensitivitySlider);
		table.add(sensitivityValue).width(40);

		// register the back button
		TextButton backButton = new TextButton("Back to Game", skin);
		backButton.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
	        }
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				sound.play(SoundType.CLICK);
				backToMainMenu();
			}
		});
		table.row();
		table.add(backButton).fill().width(400).center();
	}
	
	/**
	 * Updates the volume label next to the slider.
	 */
	private void updateVolumeLabel() {
		float volume = (PreferenceFactory.getInstance().getVolume() * 100);
		volumeValue.setText(String.format(Locale.US, "%1.0f%%", volume));
	}
	
	private void updateSensitivityLabel() {
		float sensitivity = (PreferenceFactory.getInstance().getSensitivity() * 100);
		sensitivityValue.setText(String.format(Locale.US, "%1.0f%%", sensitivity));
	}

	@Override
	public void showScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disposeScreen() {
		// TODO Auto-generated method stub
		
	}
	
	private void backToMainMenu() {
		Gdx.gl.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		game.playingScreen.renderWorld(0);
		game.setScreen(game.pauseScreen);
	}

}
