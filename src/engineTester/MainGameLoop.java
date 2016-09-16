package engineTester;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import fontRendering.TextMaster;
import gameStates.Game;
import gameStates.MainMenu;
import gameStates.SettingsMenu;
import renderEngine.DisplayManager;
import settings.Settings;

public class MainGameLoop 
{	
	public static int gameState = 0;
		
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		while(true)
		{
			if(gameState == 0)
			{
				MainMenu.run();
			}
			
			if(gameState == 1)
			{
				TextMaster.cleanUp();
				Game.run();
			}
			
			if(gameState == 3)
			{
				SettingsMenu.run();
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_F11))
			{
				Settings.setFullscreen();
			}
			
			try
			{
				Display.setFullscreen(Settings.fullscreen);
			} 
			catch (LWJGLException e)
			{
				e.printStackTrace();
			}
			
			DisplayManager.updateDisplay();
			
		}
		
	}

}
