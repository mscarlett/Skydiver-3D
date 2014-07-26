// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsScreen extends MenuScreen {

	public OptionsScreen(SkyDiver3D game) {
		super(game);

		Table table = this.table;
		table.defaults().spaceBottom(30);
		table.columnDefaults(0).padRight(20);
		Label title = new Label("Options", skin);
		title.setFontScale(2f);
		table.add(title).colspan(3);

		// create the labels widgets
		final CheckBox soundEffectsCheckbox = new CheckBox("", skin);
		//soundEffectsCheckbox.setChecked(game.getPreferencesManager()
		//		.isSoundEnabled());
		soundEffectsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//boolean enabled = soundEffectsCheckbox.isChecked();
				//game.getPreferencesManager().setSoundEnabled(enabled);
				//game.getSoundManager().setEnabled(enabled);
				//game.getSoundManager().play(TyrianSound.CLICK);
				
				if (SkyDiver3D.DEV_MODE) {
					Gdx.app.log(SkyDiver3D.LOG, "Changing sound effects checkbox");
				}
			}
		});
		table.row();
		table.add("Sound Effects");
		table.add(soundEffectsCheckbox).colspan(2).left();

		final CheckBox musicCheckbox = new CheckBox("", skin);
		//musicCheckbox.setChecked(game.getPreferencesManager().isMusicEnabled());
		musicCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//boolean enabled = musicCheckbox.isChecked();
				//game.getPreferencesManager().setMusicEnabled(enabled);
				//game.getMusicManager().setEnabled(enabled);
				//game.getSoundManager().play(TyrianSound.CLICK);

				// if the music is now enabled, start playing the menu music
				//if (enabled)
					//game.getMusicManager().play(TyrianMusic.MENU);
				
				if (SkyDiver3D.DEV_MODE) {
					Gdx.app.log(SkyDiver3D.LOG, "Changing music checkbox");
				}
			}
		});
		table.row();
		table.add("Music");
		table.add(musicCheckbox).colspan(2).left();

		// range is [0.0,1.0]; step is 0.1f
		Slider volumeSlider = new Slider(0f, 1f, 0.1f, true, skin);
		//volumeSlider.setValue(game.getPreferencesManager().getVolume());
		volumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//float value = ((Slider) actor).getValue();
				//game.getPreferencesManager().setVolume(value);
				//game.getMusicManager().setVolume(value);
				//game.getSoundManager().setVolume(value);
				//updateVolumeLabel();
			}
		});

		// create the volume label
		Label volumeValue = new Label("50%", skin);
		updateVolumeLabel();

		// add the volume row
		table.row();
		table.add("Volume");
		table.add(volumeSlider);
		table.add(volumeValue).width(40);

		// register the back button
		TextButton backButton = new TextButton("Back to main menu", skin);
		backButton.addListener(new ClickListener() {
			
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				backToMainMenu();
	        }
			/*@Override
			public void touchUp(ActorEvent event, float x, float y,
					int pointer, int button) {
				//super.touchUp(event, x, y, pointer, button);
				//game.getSoundManager().play(TyrianSound.CLICK);
				//game.setScreen(new MenuScreen(game));
			}*/
		});
		table.row();
		table.add(backButton).size(250, 60).colspan(3);
	}
	
	/**
	 * Updates the volume label next to the slider.
	 */
	private void updateVolumeLabel() {
		//float volume = (game.getPreferencesManager().getVolume() * 100);
		//volumeValue.setText(String.format(Locale.US, "%1.0f%%", volume));
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
		game.setScreen(game.mainMenuScreen);
	}

}
