package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

public class DisplayManager
{
	private static final int WIDTH = Display.getDesktopDisplayMode().getWidth() - 200;
	private static final int HEIGHT = Display.getDesktopDisplayMode().getHeight() - 200;
	private static final int FPS_CAP = 3000;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createDisplay()
	{		
		ContextAttribs attribs = new ContextAttribs(3, 3)
		.withForwardCompatible(true)
		.withProfileCore(true);
				
		try 
		{
			setDisplayMode(WIDTH, HEIGHT, true);
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			Display.setTitle("Game Window");
			Display.setResizable(true);
			Display.setFullscreen(true);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		} 
				
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public static void setDisplayMode(int width, int height, boolean fullscreen) 
	{
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	    (Display.isFullscreen() == fullscreen)) 
	    {
	        return;
	    }
	 
	    try 
	    {
	        DisplayMode targetDisplayMode = null;
	         
	    if (fullscreen) 
	    {
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        int freq = 0;
	                 
	        for (int i=0;i<modes.length;i++) 
	        {
	            DisplayMode current = modes[i];
	                     
		        if ((current.getWidth() == width) && (current.getHeight() == height)) 
		        {
		            if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
		            {
		                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) 
		                {
		                targetDisplayMode = current;
		                freq = targetDisplayMode.getFrequency();
		                }
		            }
		 
		            // if we've found a match for bpp and frequence against the 
		            // original display mode then it's probably best to go for this one
		            // since it's most likely compatible with the monitor
		            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) 
		            {
		                            targetDisplayMode = current;
		                            break;
		            }
		        }
	        }
	    }
	    else
	    {
	        targetDisplayMode = new DisplayMode(width,height);
	    }
	 
	        if (targetDisplayMode == null) 
	        {
	            System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	             
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
	    }
	}
	
	public static void updateDisplay()
	{			
		Display.sync(FPS_CAP);
		Display.update();		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds()
	{
		return delta;
	}
	
	public static void closeDisplay()
	{
		Display.destroy();		
	}
	
	private static long getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static Vector2f getNormalizedMouseCoordinates()
	{
		float normalizedX = 1.0f + 2.0f * (float) Mouse.getX() / Display.getDesktopDisplayMode().getWidth();
		float normalizedY = -(1.0f - 2.0f * (float) Mouse.getY() / Display.getDesktopDisplayMode().getHeight());
//		
//		float normalizedX = (float) ((float) Mouse.getX() / Display.getDisplayMode().getWidth() * 2.0 - 1.0);
//		float normalizedY = (float) ((float) Mouse.getY() / Display.getDisplayMode().getHeight() * 2.0 - 1.0);
		
		return new Vector2f(normalizedX, normalizedY);
	}
	
}
