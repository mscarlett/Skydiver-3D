// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
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

public class OptionsScreen extends MenuScreen {

	private Label volumeValue;
	private Label sensitivityValue;
	
	public OptionsScreen(final Skydiver3D game) {
		super(game);
	}
	
	@Override
	public void initializeScreen() {
		super.initializeScreen();
		
		final FontFactory fontFactory = FontFactory.getInstance();
		final PreferenceFactory preferences = PreferenceFactory.getInstance();
		final SoundFactory sound = SoundFactory.getInstance();
		final MusicFactory music = MusicFactory.getInstance();
		
		TextButtonStyle textButtonStyle = skin.get(TextButtonStyle.class);
		BitmapFont font = fontFactory.generateFont(42);
		textButtonStyle.font = font;
		//CheckBoxStyle checkBoxStyle = skin.get(CheckBoxStyle.class);
		//checkBoxStyle.checkboxOff.setMinWidth(50f);
		//checkBoxStyle.checkboxOff.setMinHeight(50f);
		//checkBoxStyle.checkboxOn.setMinWidth(50f);
		//checkBoxStyle.checkboxOn.setMinHeight(50f);
		
		LabelStyle labelStyle = skin.get(LabelStyle.class);
		font = fontFactory.generateFont(36);
		labelStyle.font = font;
		font = fontFactory.generateFont(64);
		skin.add("Title font", font, BitmapFont.class);

		table.defaults().spaceBottom(20);
		table.columnDefaults(0).padRight(20);
		Label title = new Label("Options", skin, "Title font", Color.WHITE);
		table.add(title).colspan(3);

		// create the labels widgets
		final CheckBox soundEffectsCheckbox = new CheckBox("", skin);
		soundEffectsCheckbox.setScale(4);
		soundEffectsCheckbox.setScaleX(10);
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
		musicCheckbox.setScale(4);
		musicCheckbox.setScaleX(10);
		musicCheckbox.setChecked(preferences.isMusicEnabled());
		musicCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean enabled = musicCheckbox.isChecked();
				preferences.setMusicEnabled(enabled);
				sound.play(SoundType.CLICK);

				// if the music is now enabled, start playing the menu music
				if (enabled)
					music.play(MusicType.MAIN_MENU);
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
		volumeSlider.setScaleX(10);
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
		sensitivitySlider.setScaleX(10);
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
		
		// add the volume row
		table.row();
		table.add("Motion Sensitivity");
		table.add(sensitivitySlider);
		table.add(sensitivityValue).width(40);

		addBackButton();
	}
	
	protected void addBackButton() {
		final SoundFactory sound = SoundFactory.getInstance();
		// register the back button
		TextButton backButton = new TextButton("Back to Main", skin);
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
	
	/**
	 * Updates the sensitivity label next to slider
	 */
	private void updateSensitivityLabel() {
		float sensitivity = (PreferenceFactory.getInstance().getSensitivity() * 100);
		sensitivityValue.setText(String.format(Locale.US, "%1.0f%%", sensitivity));
	}
	
	/**
	 * Return to main menu
	 */
	private void backToMainMenu() {
		game.setScreen(game.mainMenuScreen);
	}

}
