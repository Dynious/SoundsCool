package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GuiAdminPanel extends GuiScreen
{
    private EntityPlayer player;
    private GuiTextField maxSounds;

    public GuiAdminPanel(EntityPlayer player)
    {
        this.player = player;
        SoundsCool.proxy.getChannel().writeOutbound(new GetUploadedSoundsPacket(player));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.field_146292_n.add(new GuiButton(0, getWidth() / 2, getHeight() - 42, I18n.getStringParams("gui.done")));
        maxSounds = new GuiTextField(field_146289_q, getWidth()/2, getHeight()/2, 20, 15);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {

    }

    @Override
    protected void func_146284_a(GuiButton button)
    {
        if (button.field_146124_l)
        {
            switch (button.field_146127_k)
            {
                case 0:
                    this.field_146297_k.func_147108_a(null);
                    this.field_146297_k.setIngameFocus();
                    break;

            }
        }
    }

    public int getWidth()
    {
        return field_146294_l;
    }

    public int getHeight()
    {
        return field_146295_m;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
