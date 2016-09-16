package components;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.Loader;

public class Button
{
	/** Button width in pixels */
    public float width;
    /** Button height in pixels */
    public float height;
    /** The x position of this control. */
    public float xPosition;
    /** The y position of this control. */
    public float yPosition;
    /** The string displayed on this control. */
    public String displayString;
    public int id;
    /** True if this control is enabled, false to disable. */
    public boolean enabled;
    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    public GUIText buttonText;

    public Button(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public Button(int buttonId, float x, float y, float widthIn, float heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(float mouseX, float mouseY, Loader loader, GuiRenderer renderer, float textX, float textY, float textScale)
    {
        if(this.visible)
        {
        	List<GuiTexture> buttonTexture = new ArrayList<GuiTexture>();
            buttonTexture.add(new GuiTexture(loader.loadTexture("button"), new Vector2f(xPosition, yPosition), new Vector2f(width, height)));
            
            List<GuiTexture> buttonDownTexture = new ArrayList<GuiTexture>();
            buttonDownTexture.add(new GuiTexture(loader.loadTexture("buttonDown"), new Vector2f(xPosition, yPosition), new Vector2f(width, height)));
            
            this.hovered = mouseX >= this.xPosition - this.width && mouseY >= this.yPosition - this.height && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            if(i == 1)
            {
            	renderer.render(buttonTexture);
            }
            if(i == 2)
            {
            	renderer.render(buttonDownTexture);
            }
            int j = 14737632;

            buttonText = new GUIText(displayString, textScale, new FontType(loader.loadFontTexture("segoeui"), "segoeui"), new Vector2f(textX, textY), 1, false);
            buttonText.setColour(1.0f, 0.0f, 0.0f);
            TextMaster.loadText(buttonText);
            TextMaster.render();
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(float mouseX, float mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(float mouseX, float mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height && Mouse.isButtonDown(0);
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(float mouseX, float mouseY)
    {
    }

    public float getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }
}
