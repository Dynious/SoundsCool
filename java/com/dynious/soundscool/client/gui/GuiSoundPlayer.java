package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerPlayPacket;
import com.dynious.soundscool.sound.Sound;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.apache.commons.io.FileUtils;

@SideOnly(Side.CLIENT)
public class GuiSoundPlayer extends GuiScreen implements IListGui
{
    private GuiRemoteSoundsList soundsList;
    private TileSoundPlayer tile;
    private GuiButton playButton;

    public GuiSoundPlayer(TileSoundPlayer tile)
    {
        this.tile = tile;
        SoundsCool.proxy.getChannel().writeOutbound(new GetUploadedSoundsPacket(Minecraft.getMinecraft().thePlayer));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiRemoteSoundsList(this, 150);
        this.field_146292_n.add(new GuiButton(0, getWidth() / 2, getHeight() - 42, I18n.getStringParams("gui.done")));
        this.field_146292_n.add(playButton = new GuiButton(1, getWidth() / 2, getHeight() - 72, "Play"));
        onSelectedSoundChanged();
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        this.soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);

        Sound sound = tile.getSelectedSound();
        if (sound != null)
        {
            this.getFontRenderer().drawString(sound.getSoundName(), getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(sound.getSoundName())/2), 30, 0xFFFFFF);

            String uploaded = sound.hasLocal()? "Downloaded": "Not downloaded";
            this.getFontRenderer().drawString(uploaded, getWidth()/2 + + 100 - (this.getFontRenderer().getStringWidth(uploaded)/2), 60, sound.hasLocal()? 0x00FF00: 0xFF0000);

            String category = sound.getRemoteCategory();
            this.getFontRenderer().drawString(category, getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(category)/2), 90, 0xFFFFFF);

            if (sound.getSoundLocation() != null)
            {
                String space = FileUtils.byteCountToDisplaySize(sound.getSoundLocation().length());
                this.getFontRenderer().drawString(space, getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(space)/2), 120, 0xFFFFFF);
            }
        }
    }

    @Override
    protected void func_146284_a(GuiButton button)
    {
        if (button.field_146124_l)
            switch (button.field_146127_k)
            {
                case 0:
                    this.field_146297_k.func_147108_a(null);
                    this.field_146297_k.setIngameFocus();
                    break;
                case 1:
                    if (tile.getSelectedSound() != null)
                    {
                        SoundsCool.proxy.getChannel().writeOutbound(new SoundPlayerPlayPacket(tile));
                    }
                    break;
            }
    }

    public void onSelectedSoundChanged()
    {
        playButton.field_146124_l = tile.getSelectedSound() != null;
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
        if (selected >= 0 && selected < SoundHandler.getRemoteSounds().size())
        {
            tile.selectSound(SoundHandler.getRemoteSounds().get(selected).getSoundName());
            onSelectedSoundChanged();
        }
    }

    @Override
    public boolean soundIndexSelected(int var1)
    {
        Sound sound = tile.getSelectedSound();
        return sound != null && SoundHandler.getRemoteSounds().indexOf(sound) == var1;
    }

    @Override
    public int getWidth()
    {
        return field_146294_l;
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

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
