package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/fontRendering/fontFragment.txt";
	
	private int location_colour;
	private int location_translation;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_outlineColour;
	
	public FontShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_outlineColour = super.getUniformLocation("outlineColour");
	}

	@Override
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void loadBorderWidth(float borderWidth)
	{
		super.loadFloat(location_borderWidth, borderWidth);
	}
	
	protected void loadBorderEdge(float borderEdge)
	{
		super.loadFloat(location_borderEdge, borderEdge);
	}
	
	protected void loadOutlineColour(Vector3f outlineColour)
	{
		super.loadVector(location_outlineColour, outlineColour);
	}
	
	protected void loadColour(Vector3f colour)
	{
		super.loadVector(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation)
	{
		super.load2DVector(location_translation, translation);
	}
}
