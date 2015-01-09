package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.helper.SoundHelper;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.RemoveSoundPacket;
import com.dynious.soundscool.sound.Sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.util.UUID;

public class GuiSounds extends GuiScreen implements IListGui
{
    private GuiLocalSoundsList soundsList;
    private int selected = -1;
    private Sound selectedSound;
    private JFileChooser fileChooser;
    private EntityPlayer player;
    private GuiButton uploadButton;
    private GuiButton playButton;
    private UUID currentlyPlayerSoundId;
    private long timeSoundFinishedPlaying;

    public GuiSounds(EntityPlayer player)
    {
        this.player = player;
        fileChooser = new JFileChooser(Minecraft.getMinecraft().mcDataDir) {
            @Override
            protected JDialog createDialog(Component parent)
                    throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setLocationByPlatform(true);
                dialog.setAlwaysOnTop(true);
                return dialog;
            }
        };
        fileChooser.setFileFilter(new FileNameExtensionFilter("Sound Files (.ogg, .wav, .mp3)", "ogg", "wav", "mp3"));
        NetworkHelper.syncPlayerSounds(player);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiLocalSoundsList(this, 150);
        this.buttonList.add(new GuiButton(0, getWidth() / 2, getHeight() - 42, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(1, 10, getHeight() - 42, 150, 20, "Select File"));
        this.buttonList.add(playButton = new GuiButton(2, getWidth() / 2, getHeight() - 102, "Play Sound"));
        playButton.enabled = false;
        this.buttonList.add(uploadButton = new GuiButton(3, getWidth() / 2, getHeight() - 72, "Upload"));
        uploadButton.enabled = false;
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        try
        {
            this.soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        }
        catch(Exception ignored)
        {
        }
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);

        if (selectedSound != null)
        {
            this.getFontRenderer().drawString(selectedSound.getSoundName(), getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(selectedSound.getSoundName())/2), 30, 0xFFFFFF);

            String uploaded = selectedSound.hasRemote()? "Uploaded": "Not uploaded";
            this.getFontRenderer().drawString(uploaded, getWidth()/2 + + 100 - (this.getFontRenderer().getStringWidth(uploaded)/2), 60, selectedSound.hasRemote()? 0x00FF00: 0xFF0000);

            if (selectedSound.getCategory() != null)
            {
                this.getFontRenderer().drawString(selectedSound.getCategory(), getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(selectedSound.getCategory())/2), 90, 0xFFFFFF);
            }

            if (selectedSound.getSoundLocation() != null)
            {
                String space = FileUtils.byteCountToDisplaySize(selectedSound.getSoundLocation().length());
                this.getFontRenderer().drawString(space, getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(space)/2), 120, 0xFFFFFF);
            }
        }
        if (playButton != null && playButton.displayString.equalsIgnoreCase("Stop Sound") && System.currentTimeMillis() > timeSoundFinishedPlaying)
        {
            playButton.displayString = "Play Sound";
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 0:
                    this.mc.displayGuiScreen(null);
                    this.mc.setIngameFocus();
                    break;
                case 1:
                    if (Minecraft.getMinecraft().isFullScreen())
                    {
                        Minecraft.getMinecraft().toggleFullscreen();
                    }
                    int fcReturn = fileChooser.showOpenDialog(null);
                    if (Minecraft.getMinecraft().gameSettings.fullScreen != Minecraft.getMinecraft().isFullScreen())
                    {
                        Minecraft.getMinecraft().toggleFullscreen();
                    }
                    if (fcReturn == JFileChooser.APPROVE_OPTION)
                    {
                        selectSoundIndex(-1);
                        selectedSound = new Sound(fileChooser.getSelectedFile());
                        onSelectedSoundChanged();
                    }
                    break;
                case 2:
                    if (selectedSound != null)
                    {
                        if (System.currentTimeMillis() > timeSoundFinishedPlaying)
                        {
                            currentlyPlayerSoundId = UUID.randomUUID();
                            timeSoundFinishedPlaying = (long)(SoundHelper.getSoundLength(selectedSound.getSoundLocation())*1000) + System.currentTimeMillis();
                            SoundPlayer.playSound(selectedSound.getSoundLocation(), currentlyPlayerSoundId.toString(), (float)player.posX, (float)player.posY, (float)player.posZ, false);
                            playButton.displayString = "Stop Sound";
                        }
                        else
                        {
                            timeSoundFinishedPlaying = 0;
                            playButton.displayString = "Play Sound";
                            SoundPlayer.stopSound(currentlyPlayerSoundId.toString());
                        }
                    }
                    break;
                case 3:
                    if (selectedSound != null)
                    {
                        if (selectedSound.getState() == Sound.SoundState.LOCAL_ONLY)
                        {
                            Sound sound = SoundHandler.setupSound(selectedSound.getSoundLocation());
                            NetworkHelper.clientSoundUpload(sound);
                            selectSoundIndex(-1);
                        }
                        else
                        {
                            SoundsCool.network.sendToServer(new RemoveSoundPacket(selectedSound.getSoundName()));
                            SoundHandler.removeSound(selectedSound);
                            selectSoundIndex(-1);
                        }
                    }
                    break;
            }
        }
    }

    public void onSelectedSoundChanged()
    {
        if (selectedSound != null)
        {
            if (selectedSound.hasRemote())
            {
                uploadButton.displayString = "Remove";
                uploadButton.enabled = selectedSound.getRemoteCategory().equals(player.getDisplayName());
            }
            else
            {
                uploadButton.displayString = "Upload";
                uploadButton.enabled = true;
            }
            playButton.enabled = true;
        }
        else
        {
            uploadButton.displayString = "Upload";
            uploadButton.enabled = false;
            playButton.enabled = false;
        }
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
        this.selected = selected;

        if (selected >= 0 && selected < SoundHandler.getLocalSounds().size())
        {
            this.selectedSound = SoundHandler.getLocalSounds().get(selected);
        }
        else
        {
            this.selectedSound = null;
        }
        onSelectedSoundChanged();
    }

    @Override
    public boolean soundIndexSelected(int var1)
    {
        return var1 == selected;
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

    @Override
    public void onGuiClosed()
    {
        if (System.currentTimeMillis() < timeSoundFinishedPlaying)
        {
            SoundPlayer.stopSound(currentlyPlayerSoundId.toString());
        }
    }
}
