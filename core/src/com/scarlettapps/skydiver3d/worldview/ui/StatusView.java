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
import com.scarlettapps.skydiver3d.resources.LanguageFactory;
import com.scarlettapps.skydiver3d.resources.SoundFactory;
import com.scarlettapps.skydiver3d.world.Skydiver;
import com.scarlettapps.skydiver3d.worldstate.Status;

public class StatusView {

	private final Status status;
	
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
	
	public StatusView(Status status) {
		this.status = status;
	}

	public void initialize() {
		createJumpLabelAction();
		createInitialLabelAction();
		createParachuteAction();
		
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
		addPauseIcon();
		addSpeedIcon();
	}
	
	private void createJumpLabelAction() {
        jumpLabelAction = new Action() {
			
			Color white = Color.WHITE.cpy();
			float elapsedTime = 0.5f;

			@Override
			public boolean act(float delta) {
				if (status.jumpedOffAirplane()) {
					return true;
				}
				Label label = (Label)getActor();
				elapsedTime = (elapsedTime + 0.1f*delta) % 1f;
				label.setColor(white.lerp(Color.RED, 0.5f*Math.abs(elapsedTime-0.5f) % 1f));
				white.set(Color.WHITE);
				return false;
			}
			
		};
	}
	
	private void createInitialLabelAction() {
        initialLabelAction = new Action() {
			
			@Override
			public boolean act(float delta) {
				
				Group group = (Group)getActor();
				SnapshotArray<Actor> children = group.getChildren(); // why is this null?
				if (status.jumpedOffAirplane()) {
					float elapsedTime = status.skydivingTime();
					if (elapsedTime > 2f) {
						children.get(1).setVisible(true);
						children.get(0).setVisible(false);
						return false;
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
	}
	
	private void createParachuteAction() {
        parachuteAction = new Action() {
			
			private float elapsedTime = 0f;

			@Override
			public boolean act(float delta) {
				
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
					children.get(2).setVisible(false);
				}
				if (status.parachuteDeployed()) {
					float altitude = 3.28084f*status.position().z;
					if (altitude < 1800f && elapsedTime > 2f) {
						Group group = (Group)getActor();
						if (group == null) { // XXX fix bug?
							System.out.println("bug!");
						}
						SnapshotArray<Actor> children = group.getChildren();
						children.get(0).setVisible(false);
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
	}

	private void addHud() {
		hud = new HUD(skin, status);
		stage.addActor(hud.getGroup());
	}
	
	private void addInitial() {
		initial = new Group();
		initial.setVisible(false);
		LanguageFactory lang = LanguageFactory.getInstance();
		jumpLabel = new Label(lang.TAP_SCREEN_TO_JUMP_OFF_PLANE, skin);
		centerLabel(jumpLabel);
		jumpLabel.setColor(Color.WHITE);
		jumpLabel.addAction(jumpLabelAction);
		initial.addActor(jumpLabel);
		readyLabel = new Label(lang.GET_READY, skin);
		upperLabel(readyLabel);
		readyLabel.setColor(Color.WHITE);
		readyLabel.setVisible(false);
		initial.addAction(initialLabelAction);
		initial.addActor(readyLabel);
		stage.addActor(initial);
	}
	
	private void addParachute() {
		parachute = new Group();
		parachute.setVisible(false);
		LanguageFactory lang = LanguageFactory.getInstance();
		tapScreenLabel = new Label(lang.TAP_SCREEN_TO_OPEN_PARACHUTE, skin);
		centerLabel(tapScreenLabel);
		tapScreenLabel.setColor(Color.WHITE);
		parachute.addActor(tapScreenLabel);
		Label label = new Label("", skin);
		label.setVisible(false);
		label.setColor(Color.WHITE);
		parachute.addActor(label);
		landLabel = new Label(lang.GET_READY_TO_LAND, skin);
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
				float displayScoreTime = status.displayScoreTime();
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
		pauseIcon.setScale(1f);
		pauseIcon.setPosition(DefaultScreen.VIRTUAL_WIDTH-pauseIcon.getWidth()-10, 10);
		pauseIcon.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundFactory.getInstance().play(SoundType.CLICK);
				status.setPaused(true);
			}
			
		});
		pauseIcon.setVisible(true);
		stage.addActor(pauseIcon);
	}
	
	private void addSpeedIcon() {
		speedIcon = new Image(AssetFactory.getInstance().get(TextureType.LIGHTNING, Texture.class));
		speedIcon.setScale(1f);
		speedIcon.setPosition(10, 10);
		speedIcon.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				status.setSticky();
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
				float speedFactor = (-status.velocity().z-a)/(b-a);
				temp.lerp(Color.YELLOW, speedFactor);
				image.setColor(temp);
				return false;
			}
		});
		speedIcon.setVisible(false);
		stage.addActor(speedIcon);
	}

	
	private String getSuccess(int success) {
		LanguageFactory lang = LanguageFactory.getInstance();
		if (success < 10) {
			return lang.SUCCESS_0;
		} else if (success < 20) {
			return lang.SUCCESS_1;
		} else if (success < 30) {
			return lang.SUCCESS_2;
		} else if (success < 40) {
			return lang.SUCCESS_3;
		} else if (success < 50) {
			return lang.SUCCESS_4;
		} else if (success < 60) {
			return lang.SUCCESS_5;
		} else if (success < 70) {
			return lang.SUCCESS_6;
		} else if (success < 80) {
			return lang.SUCCESS_7;
		} else if (success < 90) {
			return lang.SUCCESS_8;
		} else if (success < 97) {
			return lang.SUCCESS_9;
		} else {
			return lang.SUCCESS_10;
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
	
	private static void upperLabel(Label label) {
		float x = DefaultScreen.VIRTUAL_WIDTH/2;
		float y = DefaultScreen.VIRTUAL_HEIGHT*3/5;
		setLabel(label, x, y);
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
		style.font = FontFactory.getInstance().generateFont(60);
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
		
		// Am I doing this right?
		accuracyMeter.reset();
        jumpLabelAction.reset();
        initialLabelAction.reset();
        parachuteAction.reset();
        
        jumpLabel.addAction(jumpLabelAction);
        initial.addAction(initialLabelAction);
        createParachuteAction();
        addParachute();
        //parachute.addAction(parachuteAction);
        
		speedIcon.setVisible(false);
		landLabel.setVisible(false);
		tapScreenLabel.setVisible(true);
	}

}
