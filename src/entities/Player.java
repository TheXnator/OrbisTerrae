package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import gameStates.Game;
import models.TexturedModel;
import renderEngine.DisplayManager;
import settings.Settings;
import terrains.Terrain;

public class Player extends Entity
{
	private static final float WALK_SPEED = 20;
	private static final float RUN_SPEED = 30;
	private static final float SIDE_SPEED = 20;
	public static float GRAVITY = -60;
	private static final float JUMP_POWER = 20;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currentSidewaysSpeed = 0;
	private float upwardsSpeed = 0;
		
	private boolean isInAir = false;
	
	public boolean onFire = false;
	public float health = 50;
	public float breath = 50;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) 
	{
		super(model, position, rotX, rotY, rotZ, scale);
	}
		
	public void move(Terrain terrain)
	{
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		float sidewaysDistance = currentSidewaysSpeed * DisplayManager.getFrameTimeSeconds();
		float dxSideways = (float) (sidewaysDistance * Math.sin(Math.toRadians(super.getRotY() + 90)));
		float dzSideways = (float) (sidewaysDistance * Math.cos(Math.toRadians(super.getRotY() + 90)));
		super.increasePosition(dxSideways, 0, dzSideways);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight)
		{
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump()
	{
		if(!isInAir)
		{
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			{
				this.currentSpeed = RUN_SPEED;
			}
			else
			{
				this.currentSpeed = WALK_SPEED;
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			{
				this.currentSpeed = -RUN_SPEED;
			}
			else
			{
				this.currentSpeed = -WALK_SPEED;
			}
		}
		else
		{
			currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			this.currentSidewaysSpeed = -SIDE_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			this.currentSidewaysSpeed = SIDE_SPEED;
		}
		else
		{
			currentSidewaysSpeed = 0;
		}
		
		currentTurnSpeed = -((Mouse.getDX() * 4) * Settings.sensitivity);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			if(getPosition().y < 0)
			{
				GRAVITY = 10;
			}
			else if(getPosition().y == -0.5)
			{
				GRAVITY = 2;
			}
			else
			{
				jump();
			}
		}
		
		if(getPosition().y > -2)
		{
			GRAVITY = -60;	
		}
		else
		{
			if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				GRAVITY = -5;
			}
		}
		
		//- and = breath
		if(getPosition().y <= -6)
		{
			breath = breath - 0.1f;
		}
		else
		{
			if(breath < 50)
			{
				breath = 50;
			}
		}
		if(breath <= 0)
		{
			health = health - 0.1f;
		}
		if(breath < 0)
		{
			breath = 0;
		}
				
		//Regen and death
		if(health < 50 && breath > 0)
		{
			health = health + 0.1f;
		}
		if(health <= 0)
		{
			Game.gameover = true;
		}
		//Mouse.setGrabbed(true);
	}
}
