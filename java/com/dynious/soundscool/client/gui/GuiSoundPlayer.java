package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.CheckPresencePacket;
import com.dynious.soundscool.sound.Sound;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiSoundPlayer extends GuiScreen
{
    private TileSoundPlayer tile;
    private String selectedSound = "companionhurt1.ogg";
    private GuiTextField soundField;

    public GuiSoundPlayer(TileSoundPlayer tile)
    {
        this.tile = tile;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, this.field_146295_m - 38, "Play"));
        soundField = new GuiTextField(field_146289_q, field_146294_l/2 - 50, field_146295_m/2 - 7, 100, 15);
        soundField.func_146180_a(selectedSound);
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        soundField.func_146194_f();
    }

    @Override
    public void keyTyped(char c, int i)
    {
        super.keyTyped(c, i);
        soundField.func_146201_a(c, i);
        selectedSound = soundField.func_146179_b();
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        soundField.func_146192_a(x, y, type);
    }

    @Override
    protected void func_146284_a(GuiButton button)
    {
        if (button.field_146124_l)
        {
            switch (button.field_146127_k)
            {
                case 0:
                    SoundHandler.playSound(selectedSound, tile.field_145851_c, tile.field_145848_d, tile.field_145849_e);
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        soundField.func_146180_a(selectedSound);
    }
}
