package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.sound.Sound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import javax.swing.*;

public class GuiSounds extends GuiScreen implements IListGui
{
    private GuiLocalSoundsList soundsList;
    private int selected = -1;
    private Sound selectedSound;
    private JFileChooser fileChooser;
    private EntityPlayer player;

    public GuiSounds(EntityPlayer player)
    {
        this.player = player;
        SoundsCool.proxy.getChannel().writeOutbound(new GetUploadedSoundsPacket(player));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiLocalSoundsList(this, 150);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, this.field_146295_m - 38, I18n.getStringParams("gui.done")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 75, 38, "Select File"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 75, field_146295_m/2 - 20, "Play Sound"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 75, field_146295_m/2 + 20, "Upload"));
        fileChooser = new JFileChooser(Minecraft.getMinecraft().mcDataDir);
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        this.soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);
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
                case 1:
                    int fcReturn = fileChooser.showOpenDialog(null);
                    if (fcReturn == JFileChooser.APPROVE_OPTION)
                    {
                        selectedSound = new Sound(fileChooser.getSelectedFile());
                        selected = -1;
                    }
                    break;
                case 2:
                    if (selectedSound != null)
                    {
                        SoundPlayer.playSound(selectedSound.getSoundLocation(), (float)player.posX, (float)player.posY, (float)player.posZ);
                    }
                    break;
                case 3:
                    if (selectedSound != null)
                    {
                        Sound sound = SoundHandler.setupSound(selectedSound.getSoundLocation());
                        NetworkHelper.clientSoundUpload(sound);
                    }
                    break;
            }
        }
    }

    @Override
    public Minecraft getMinecraftInstance()
    {
        return field_146297_k;
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return field_146289_q;
    }

    @Override
    public void selectSoundIndex(int selected)
    {
        this.selected = selected;

        if (selected >= 0 && selected <= SoundHandler.getSounds().size())
        {
            this.selectedSound = SoundHandler.getSounds().get(selected);
        }
        else
        {
            this.selectedSound = null;
        }
    }

    @Override
    public boolean soundIndexSelected(int var1)
    {
        return var1 == selected;
    }

    @Override
    public int getWidth()
    {
        return field_146295_m;
    }

    @Override
    public int getHeight()
    {
        return field_146295_m;
    }

    @Override
    public void drawBackground()
    {
        func_146276_q_();
    }
}
