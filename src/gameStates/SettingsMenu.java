package gameStates;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import components.Button;
import components.PageButtonList.Responder;
import components.Slider;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import settings.Settings;

public class SettingsMenu
{
	private static GUIText sensitivity;
	private static GUIText FOV;
	
	public static void run()
	{
		Loader loader = new Loader();
		TextMaster.init(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		Responder guiResponder = new Responder()
		{		
			@Override
			public void onTick(int id, float value)
			{
				
			}
			
			@Override
			public void func_175321_a(int p_175321_1_, boolean p_175321_2_)
			{
				
			}
			
			@Override
			public void func_175319_a(int p_175319_1_, String p_175319_2_)
			{
				
			}
		};
		
		FontType arialMenuFont = new FontType(loader.loadFontTexture("segoeui"), "segoeui");
		GUIText settingsText = new GUIText("Settings", 4, arialMenuFont, new Vector2f(0.4f, 0.05f), 1, false);
		settingsText.setColour(0.3f, 0.2f, 0.4f);
		
		List<GuiTexture> settingsGUIs = new ArrayList<GuiTexture>();
		settingsGUIs.add(new GuiTexture(loader.loadTexture("settingsBackground"), new Vector2f(0, -0.87f), new Vector2f(2f, 2f)));
		
		Button fullscreen = new Button(0, 0.05f, 0.41f, 0.25f, 0.075f, "Fullscreen");
		Slider sensitivitySlider = new Slider(guiRenderer, guiResponder, 0, 0.05f, 0.7f, "sensitivity", 0, 2, 1);
		
		while(!Display.isCloseRequested())
		{
			guiRenderer.render(settingsGUIs);
						
			fullscreen.drawButton((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f, loader, guiRenderer, 0.46f, 0.26f, 2);
			if(fullscreen.mousePressed((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f))
			{
				Settings.setFullscreen();
			}
			
			sensitivitySlider.drawButton((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f, loader, guiRenderer, 0.2f, 0.4f, 1);
			
			if(sensitivitySlider.mousePressed((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f))
			{
				sensitivitySlider.mouseDragged((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f, loader);
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
		
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
