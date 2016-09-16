package settings;

public class Settings
{
	public static float sensitivity = 1f;
	public static float FOV = 70;
	
	public static boolean fullscreen = true;

	public static void setSensitivity(float sensitivity)
	{
		Settings.sensitivity = sensitivity;
	}

	public static void setFOV(float fOV)
	{
		FOV = fOV;
	}

	public static void setFullscreen()
	{
		if(fullscreen)
		{
			fullscreen = false;
		}
		else
		{
			fullscreen = true;
		}
	}
	
}
