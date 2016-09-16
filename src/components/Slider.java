package components;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.Loader;

public class Slider extends Button
{
    private float sliderPosition = 0;
    public boolean isMouseDown;
    private String name;
    private final float min;
    private final float max;
    private final PageButtonList.Responder responder;
    private GuiRenderer guiRenderer;

    public Slider(GuiRenderer guiRenderer, PageButtonList.Responder guiResponder, int idIn, float x, float y, String name, float min, float max, float defaultValue)
    {
        super(idIn, x, y, 150, 20, "");
        this.guiRenderer = guiRenderer;
        this.name = name;
        this.min = min;
        this.max = max;
        this.sliderPosition = (defaultValue - min) / (max - min);
        this.responder = guiResponder;
        //this.displayString = this.getDisplayString();
    }
    
    @Override
    public void drawButton(float mouseX, float mouseY, Loader loader, GuiRenderer renderer, float textX, float textY, float textScale)
    {
    	super.drawButton(mouseX, mouseY, loader, renderer, textX, textY, textScale);
    	List<GuiTexture> sliderTexture = new ArrayList<GuiTexture>();
        sliderTexture.add(new GuiTexture(loader.loadTexture("sliderBackground"), new Vector2f(this.xPosition, this.yPosition), new Vector2f(0.25f, 0.125f)));
        
        List<GuiTexture> sliderBarTexture = new ArrayList<GuiTexture>();
        sliderBarTexture.add(new GuiTexture(loader.loadTexture("sliderBar"), new Vector2f(this.xPosition + this.sliderPosition, this.yPosition), new Vector2f(0.25f, 0.125f)));

        guiRenderer.render(sliderTexture);
        guiRenderer.render(sliderBarTexture);
    }

    public float func_175220_c()
    {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void func_175218_a(float p_175218_1_, boolean p_175218_2_)
    {
        this.sliderPosition = (p_175218_1_ - this.min) / (this.max - this.min);
        //this.displayString = this.getDisplayString();

        if (p_175218_2_)
        {
            this.responder.onTick(this.id, this.func_175220_c());
        }
    }

    public float func_175217_d()
    {
        return this.sliderPosition;
    }

//    private String getDisplayString()
//    {
//        return 
//    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    public void mouseDragged(float mouseX, float mouseY, Loader loader)
    {
        if (this.visible)
        {
            if (this.isMouseDown)
            {
                this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.sliderPosition < 0.0F)
                {
                    this.sliderPosition = 0.0F;
                }

                if (this.sliderPosition > 1.0F)
                {
                    this.sliderPosition = 1.0F;
                }

                //this.displayString = this.getDisplayString();
                this.responder.onTick(this.id, this.func_175220_c());
            }

//            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
              List<GuiTexture> sliderTexture = new ArrayList<GuiTexture>();
              sliderTexture.add(new GuiTexture(loader.loadTexture("sliderBackground"), new Vector2f(this.xPosition, this.yPosition), new Vector2f(1, 1)));
              
              List<GuiTexture> sliderBarTexture = new ArrayList<GuiTexture>();
              sliderBarTexture.add(new GuiTexture(loader.loadTexture("sliderBar"), new Vector2f(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.yPosition), new Vector2f(1, 1)));

              guiRenderer.render(sliderTexture);
              guiRenderer.render(sliderBarTexture);
//            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public void func_175219_a(float p_175219_1_)
    {
        this.sliderPosition = p_175219_1_;
        //this.displayString = this.getDisplayString();
        this.responder.onTick(this.id, this.func_175220_c());
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(float mouseX, float mouseY)
    {
        if (super.mousePressed(mouseX, mouseY))
        {
            this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.sliderPosition < 0.0F)
            {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F)
            {
                this.sliderPosition = 1.0F;
            }

            //this.displayString = this.getDisplayString();
            this.responder.onTick(this.id, this.func_175220_c());
            this.isMouseDown = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(float mouseX, float mouseY)
    {
        this.isMouseDown = false;
    }
}
