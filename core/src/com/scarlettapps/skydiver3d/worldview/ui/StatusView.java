// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.worldview.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.scarlettapps.skydiver3d.DefaultScreen;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.resources.AssetFactory;
import com.scarlettapps.skydiver3d.resources.AssetFactory.SoundType;
import com.scarlettapps.skydiver3d.resources.AssetFactory.TextureType;
import com.scarlettapps.skydiver3d.resources.FontFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.worldstate.Status;
import com.scarlettapps.skydiver3d.worldstate.StatusManager;

public class StatusView {

	private final StatusManager statusManager;
	
	private Stage stage;
	private Skin skin;
	
	private Group initial;
	private Group parachute;
	private Group collected;
	
	private HUD hud;
	
	private PooledLinkedList<Group> visibleQueue;

	private Image pauseIcon;
	private Image speedIcon;
	
	private AccuracyMeter accuracyMeter;
	
	private Viewport viewport;

	private Label jumpLabel;
	private Label readyLabel;
	private Label landLabel;
	private Label tapScreenLabel;
	
	private Action initialLabelAction;
	private Action jumpLabelAction;
	private Action parachuteAction;
	
	public StatusView(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	public void initialize() {
		jumpLabelAction = new Action() {
			
			Color white = Color.WHITE.cpy();
			float elapsedTime = 0.5f;

			@Override
			public boolean act(float delta) {
				if (Status.getInstance().jumpedOffAirplane()) {
					return true;
				}
				Label label = (Label)getActor();
				elapsedTime = (elapsedTime + 0.1f*delta) % 1f;
				label.setColor(white.lerp(Color.RED, 0.5f*Math.abs(elapsedTime-0.5f) % 1f));
				white.set(Color.WHITE);
				return false;
			}
			
		};
		
		initialLabelAction = new Action() {
			
			@Override
			public boolean act(float delta) {
				Status status = Status.getInstance();
				Group group = (Group)getActor();
				SnapshotArray<Actor> children = group.getChildren();
				if (status.jumpedOffAirplane()) {
					float elapsedTime = status.skydivingTime();
					if (elapsedTime > 2f) {
						children.get(1).setVisible(true);
						children.get(0).setVisible(false);
						return true;
					} else {
						children.get(1).setVisible(false);
						children.get(0).setVisible(false);
					}
				} else {
					children.get(0).setVisible(true);
					children.get(1).setVisible(false);
				}
				return false;
			}
			
		};
		
		parachuteAction = new Action() {
			
			private float elapsedTime = 0f;

			@Override
			public boolean act(float delta) {
				Status status = Status.getInstance();
				if (status.justOpenedParachute()) {
					status.setJustOpenedParachute(false);
					
					int success = (int)(status.getAccuracy()*100+0.5f);
					String successString = getSuccess(success);
					
					Group group = (Group)getActor();
					SnapshotArray<Actor> children = group.getChildren();
					Label successLabel = (Label)children.get(1);
					successLabel.setText(successString);
					lowerLabel(successLabel);
					successLabel.setVisible(true);
					children.get(0).setVisible(false);
				}
				if (status.parachuteDeployed()) {
					float altitude = 3.28084f*status.position().z;
					if (altitude < 1800f && elapsedTime > 2f) {
						Group group = (Group)getActor();
						SnapshotArray<Actor> children = group.getChildren();
						children.get(1).setVisible(false);
						children.get(2).setVisible(true);
						return true;
					} else {
						elapsedTime += delta;
					}
				}
				return false;
			}
			
			public void reset() {
				elapsedTime = 0f;
			}
		};
		
        visibleQueue = new PooledLinkedList<Group>(6);
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		viewport = new StretchViewport(DefaultScreen.VIRTUAL_WIDTH, DefaultScreen.VIRTUAL_HEIGHT);
		stage = new Stage(viewport);
		
		LabelStyle textButtonStyle = skin.get(LabelStyle.class);
		BitmapFont font = FontFactory.getInstance().generateFont(36);
		textButtonStyle.font = font;
		
		addHud();
		addInitial();
		addParachute();
		addCollected();
		//addScoreSummary();
		addPauseIcon();
		addSpeedIcon();
	}

	private void addHud() {
		hud = new HUD(skin);
		stage.addActor(hud.getGroup());
	}
	
	private void addInitial() {
		initial = new Group();
		initial.setVisible(false);
		jumpLabel = new Label("Tap screen to jump off plane", skin);
		centerLabel(jumpLabel);
		jumpLabel.setColor(Color.WHITE);
		jumpLabel.addAction(jumpLabelAction);
		initial.addActor(jumpLabel);
		readyLabel = new Label("Get ready", skin);
		centerLabel(readyLabel);
		readyLabel.setColor(Color.WHITE);
		readyLabel.setVisible(false);
		initial.addAction(initialLabelAction);
		initial.addActor(readyLabel);
		stage.addActor(initial);
	}
	
	private void addParachute() {
		parachute = new Group();
		parachute.setVisible(false);
		tapScreenLabel = new Label("Tap screen to open parachute", skin);
		centerLabel(tapScreenLabel);
		tapScreenLabel.setColor(Color.WHITE);
		parachute.addActor(tapScreenLabel);
		Label label = new Label("", skin);
		label.setVisible(false);
		label.setColor(Color.WHITE);
		parachute.addActor(label);
		landLabel = new Label("Get ready to land", skin);
		landLabel.setVisible(false);
		lowerLabel(landLabel);
		landLabel.setColor(Color.WHITE);
		parachute.addActor(landLabel);
		accuracyMeter = new AccuracyMeter();
		parachute.addActor(accuracyMeter);
		parachute.addAction(parachuteAction);
		stage.addActor(parachute);
	}
	
	private void addCollected() {
		collected = new Group();
		collected.setVisible(false);
		Label label = new Label("500 Points", skin);
		label.setX(DefaultScreen.VIRTUAL_WIDTH/2);
		label.setY(DefaultScreen.VIRTUAL_HEIGHT/2);
		label.setColor(Color.WHITE);
		label.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				float displayScoreTime = Status.getInstance().displayScoreTime();
				Label label = (Label)getActor();
				label.setColor(0, 0, 1, (1-(float)Math.sqrt(displayScoreTime))/2f);
        		label.setScale((1+2*displayScoreTime)*DefaultScreen.VIRTUAL_WIDTH/480f, (1+2*displayScoreTime)*DefaultScreen.VIRTUAL_HEIGHT/360f);
				return false;
			}
			
		});
		collected.addActor(label);
		stage.addActor(collected);
	}
	
	private void addPauseIcon() {
		pauseIcon = new Image(AssetFactory.getInstance().get(TextureType.PAUSE, Texture.class));
		pauseIcon.setPosition(DefaultScreen.VIRTUAL_WIDTH-pauseIcon.getWidth()-10, 10);
		pauseIcon.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				Status.getInstance().setPaused(true);
			}
			
		});
		pauseIcon.setVisible(true);
		stage.addActor(pauseIcon);
	}
	
	private void addSpeedIcon() {
		speedIcon = new Image(AssetFactory.getInstance().get(TextureType.LIGHTNING, Texture.class));
		speedIcon.setPosition(10, 10);
		speedIcon.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Status.getInstance().setSticky();
			}
			
		});
		speedIcon.addAction(new Action() {
			
			Color temp = new Color();
			
			@Override
			public boolean act(float delta) {
				Image image = (Image)getActor();
				temp.set(Color.WHITE);
				float a = Skydiver.MIN_TERMINAL_SPEED;
				float b = Skydiver.MAX_TERMINAL_SPEED;
				float speedFactor = (-Status.getInstance().velocity().z-a)/(b-a);
				temp.lerp(Color.YELLOW, speedFactor);
				image.setColor(temp);
				return false;
			}
		});
		speedIcon.setVisible(false);
		stage.addActor(speedIcon);
	}

	
	private String getSuccess(int success) {
		if (success < 10) {
			return "Crash Landing!!!";
		} else if (success < 20) {
			return "Poor Reflexes";
		} else if (success < 30) {
			return "Bad Reflexes";
		} else if (success < 40) {
			return "Sloppy Timing";
		} else if (success < 50) {
			return "OK Timing";
		} else if (success < 60) {
			return "Average Timing";
		} else if (success < 70) {
			return "Good Timing";
		} else if (success < 80) {
			return "Great Timing!";
		} else if (success < 90) {
			return "Nice Timing!";
		} else if (success < 97) {
			return "Perfect Timing!";
		} else {
			return "Incredible Timing!!!";
		}
	}
	
	public void update(float delta) {
		stage.act(delta);
	}

	public void render(float delta) {
		visibleQueue.iter();
		Group g;
		while ((g = visibleQueue.next()) != null) {
			g.setVisible(true);
		}
		stage.draw();
		
		visibleQueue.iter();
		while ((g = visibleQueue.next()) != null) {
			g.setVisible(false);
		}
		visibleQueue.clear();
	}
	
	public void drawJumpOffPlane() {
		visibleQueue.add(initial);
	}
	
	public void drawCollected() {
		visibleQueue.add(collected);
	}
	
	public void drawParachuteCaption() {
		visibleQueue.add(parachute);
	}
	
	public void drawHud() {
		visibleQueue.add(hud.getGroup());
	}

	public AccuracyMeter getAccuracyMeter() {
		return accuracyMeter;
	}
	
	private static void centerLabel(Label label) {
		float x = DefaultScreen.VIRTUAL_WIDTH/2;
		float y = DefaultScreen.VIRTUAL_HEIGHT/2;
		setLabel(label, x, y);
	}
	
	private static void lowerLabel(Label label) {
		float x = DefaultScreen.VIRTUAL_WIDTH/2;
		float y = DefaultScreen.VIRTUAL_HEIGHT/4;
		setLabel(label, x, y);
	}
	
	private static void setLabel(Label label, float x, float y) {
		LabelStyle style = new LabelStyle();
		style.font = FontFactory.getInstance().generateFont(64);
		style.fontColor = Color.WHITE;
		label.setStyle(style);
		TextBounds bounds = label.getTextBounds();
		x -= bounds.width/2;
		y -= bounds.height/2;
		label.setPosition(x, y);
	}

	public InputProcessor getInputProcessor() {
		return stage;
	}

	public void hidePause() {
		pauseIcon.setVisible(false);
	}
	
	public void showSpeedIcon(boolean visible) {
		speedIcon.setVisible(visible);
	}

	public void reset() {
		if (Skydiver3D.DEV_MODE) {
			Gdx.app.log(Skydiver3D.LOG, "Resetting StatusView");
		}

		jumpLabel.addAction(jumpLabelAction);
        initial.addAction(initialLabelAction);
        parachute.addAction(parachuteAction);
        accuracyMeter.reset();
        parachuteAction.reset();
		
		speedIcon.setVisible(false);
		landLabel.setVisible(false);
		tapScreenLabel.setVisible(true);
	}

}
