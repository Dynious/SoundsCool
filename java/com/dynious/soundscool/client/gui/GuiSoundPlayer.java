package com.dynious.soundscool.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.FileUtils;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.helper.SoundHelper;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerPlayPacket;
import com.dynious.soundscool.sound.Sound;
import com.dynious.soundscool.tileentity.TileSoundPlayer;

@SideOnly(Side.CLIENT)
public class GuiSoundPlayer extends GuiScreen implements IListGui
{
    private GuiRemoteSoundsList soundsList;
    private TileSoundPlayer tile;
    private GuiButton playButton;

    public GuiSoundPlayer(TileSoundPlayer tile)
    {
        this.tile = tile;
        NetworkHelper.syncPlayerSounds(Minecraft.getMinecraft().thePlayer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiRemoteSoundsList(this, 150);
        this.buttonList.add(new GuiButton(0, getWidth() / 2, getHeight() - 42, I18n.format("gui.done")));
        this.buttonList.add(playButton = new GuiButton(1, getWidth() / 2, getHeight() - 72, "Play/Stop"));
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
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
            switch (button.id)
            {
                case 0:
                    this.mc.displayGuiScreen(null);
                    this.mc.setIngameFocus();
                    break;
                case 1:
                    if (tile.getSelectedSound() != null)
                    {
                    	NetworkHelper.syncPlayerSounds(Minecraft.getMinecraft().thePlayer);
                    	SoundsCool.network.sendToServer(new SoundPlayerPlayPacket(tile));
                    }
                    break;
            }
    }

    public void onSelectedSoundChanged()
    {
        playButton.enabled = tile.getSelectedSound() != null;
    }

    @Override
    public Minecraft getMinecraftInstance()
    {
        return mc;
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return fontRendererObj;
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
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public void drawBackground()
    {
        drawDefaultBackground();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
