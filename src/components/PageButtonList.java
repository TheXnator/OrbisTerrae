package components;

public class PageButtonList //extends ListExtended
{
    //private final IntHashMap<> field_178073_v = new IntHashMap();
    //private final List<TextField> field_178072_w = Lists.<TextField>newArrayList();
    private final PageButtonList.ListEntry[][] field_178078_x;
    private int field_178077_y;
    private PageButtonList.Responder field_178076_z;

    public PageButtonList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, PageButtonList.Responder p_i45536_7_, PageButtonList.ListEntry[]... p_i45536_8_)
    {
        //super(widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.func_178055_t();
    }

    private void func_178055_t()
    {
        for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2)
        {
            PageButtonList.ListEntry pagebuttonlist$listentry = this.field_178078_x[this.field_178077_y][i];
            PageButtonList.ListEntry pagebuttonlist$listentry1 = i < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][i + 1] : null;
        }
    }

    public void func_181156_c(int p_181156_1_)
    {
        if (p_181156_1_ != this.field_178077_y)
        {
            int i = this.field_178077_y;
            this.field_178077_y = p_181156_1_;
            this.func_178055_t();
        }
    }

    public int func_178059_e()
    {
        return this.field_178077_y;
    }

    public void func_178071_h()
    {
        if (this.field_178077_y > 0)
        {
            this.func_181156_c(this.field_178077_y - 1);
        }
    }

//    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent)
//    {
//        boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
//        int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
//
//        if (i >= 0)
//        {
//            PageButtonList.Entry pagebuttonlist$entry = this.getListEntry(i);
//
//            if (this.field_178075_A != pagebuttonlist$entry.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof TextField)
//            {
//                ((TextField)this.field_178075_A).setFocused(false);
//            }
//
//            this.field_178075_A = pagebuttonlist$entry.field_178028_d;
//        }
//
//        return flag;
//    }
//
//    private Slider func_178067_a(int p_178067_1_, int p_178067_2_, PageButtonList.SlideEntry p_178067_3_)
//    {
//        Slider slider = new Slider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g());
//        slider.visible = p_178067_3_.func_178934_d();
//        return slider;
//    }

//    private ListButton func_178065_a(int p_178065_1_, int p_178065_2_, PageButtonList.ButtonEntry p_178065_3_)
//    {
//        ListButton listbutton = new ListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
//        listbutton.visible = p_178065_3_.func_178934_d();
//        return listbutton;
//    }

//    private TextField func_178068_a(int p_178068_1_, int p_178068_2_, PageButtonList.EditBoxEntry p_178068_3_)
//    {
//        TextField textfield = new TextField(p_178068_3_.func_178935_b(), p_178068_1_, p_178068_2_, 150, 20);
//        textfield.setText(p_178068_3_.func_178936_c());
//        textfield.func_175207_a(this.field_178076_z);
//        textfield.setVisible(p_178068_3_.func_178934_d());
//        textfield.func_175205_a(p_178068_3_.func_178950_a());
//        return textfield;
//    }
//
//    private Label func_178063_a(int p_178063_1_, int p_178063_2_, PageButtonList.LabelEntry p_178063_3_, boolean p_178063_4_)
//    {
//        Label label;
//
//        if (p_178063_4_)
//        {
//            label = new Label(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
//        }
//        else
//        {
//            label = new Label(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
//        }
//
//        label.visible = p_178063_3_.func_178934_d();
//        label.func_175202_a(p_178063_3_.func_178936_c());
//        label.setCentered();
//        return label;
//    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 400;
    }

//    protected int getScrollBarX()
//    {
//        return super.getScrollBarX() + 32;
//    }

//    public static class ButtonEntry extends PageButtonList.ListEntry
//        {
//            private final boolean field_178941_a;
//
//            public ButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_)
//            {
//                super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
//                this.field_178941_a = p_i45535_4_;
//            }
//
//            public boolean func_178940_a()
//            {
//                return this.field_178941_a;
//            }
//        }
//
//    public static class LabelEntry extends PageButtonList.ListEntry
//        {
//            public LabelEntry(int p_i45532_1_, String p_i45532_2_, boolean p_i45532_3_)
//            {
//                super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
//            }
//        }

    public static class ListEntry
        {
            private final int field_178939_a;
            private final String field_178937_b;
            private final boolean field_178938_c;

            public ListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_)
            {
                this.field_178939_a = p_i45531_1_;
                this.field_178937_b = p_i45531_2_;
                this.field_178938_c = p_i45531_3_;
            }

            public int func_178935_b()
            {
                return this.field_178939_a;
            }

            public String func_178936_c()
            {
                return this.field_178937_b;
            }

            public boolean func_178934_d()
            {
                return this.field_178938_c;
            }
        }

    public interface Responder
    {
        void func_175321_a(int p_175321_1_, boolean p_175321_2_);

        void onTick(int id, float value);

        void func_175319_a(int p_175319_1_, String p_175319_2_);
    }

    public static class SlideEntry //extends PageButtonList.ListEntry
    {
            private final float field_178947_b;
            private final float field_178948_c;
            private final float field_178946_d;

            public SlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_)
            {
                //super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
                this.field_178947_b = p_i45530_5_;
                this.field_178948_c = p_i45530_6_;
                this.field_178946_d = p_i45530_7_;
            }

            public float func_178943_e()
            {
                return this.field_178947_b;
            }

            public float func_178944_f()
            {
                return this.field_178948_c;
            }

            public float func_178942_g()
            {
                return this.field_178946_d;
            }
        }
}
