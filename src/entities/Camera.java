package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import settings.Settings;
import terrains.Terrain;

public class Camera
{	
	private float distanceFromPlayer = 0.1f;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 0;
	private float yaw;
	private float roll;
			
	private Player player;
	
	public Camera(Player player)
	{
		this.player = player;
	}
	
	public Camera()
	{
		
	}
	
	public void move()
	{
		calculateZoom();
		calculatePitch();
		calculateAngleAroudPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY());
		yaw %= 360;
	}
	
	public void moveNoPlayer()
	{
		calculateZoom();
		pitch = 10;
		calculateAngleAroudPlayer();
	}
	
	public void invertPitch()
	{
		this.pitch = -pitch;
	}

	public Vector3f getPosition() 
	{
		return position;
	}

	public float getPitch()
	{
		return pitch;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getRoll()
	{
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance)
	{
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + 6;
	}
	
	private float calculateHorizontalDistance()
	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance()
	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			if(distanceFromPlayer <= 1f)
			{
				distanceFromPlayer = 49.9f;
			}
			else
			{
				distanceFromPlayer = 0.01f;
			}
		}
	}
	
	private void calculatePitch()
	{
		pitch += -((Mouse.getDY() / 4) * Settings.sensitivity);
		if(pitch <= -30)
		{
			pitch = -29.999999999f;
		}
		if(pitch >= 25)
		{
			pitch = 24.999999999f;
		}
	}
	
	private void calculateAngleAroudPlayer()
	{
		if(Mouse.isButtonDown(0))
		{
			angleAroundPlayer = Mouse.getDX();
		}
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

	public float getAngleAroundPlayer()
	{
		return angleAroundPlayer;
	}

	public void setAngleAroundPlayer(float angleAroundPlayer)
	{
		this.angleAroundPlayer = angleAroundPlayer;
	}
	
}
