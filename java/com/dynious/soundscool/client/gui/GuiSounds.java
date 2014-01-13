package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
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

public class GuiSounds extends GuiScreen implements IListGui
{
    private GuiLocalSoundsList soundsList;
    private int selected = -1;
    private Sound selectedSound;
    private JFileChooser fileChooser;
    private EntityPlayer player;
    private GuiButton uploadButton;
    private GuiButton playButton;

    public GuiSounds(EntityPlayer player)
    {
        this.player = player;
        fileChooser = new JFileChooser(Minecraft.getMinecraft().mcDataDir);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Sound Files (.ogg, .wav, .mp3)", "ogg", "wav", "mp3"));
        SoundsCool.proxy.getChannel().writeOutbound(new GetUploadedSoundsPacket(player));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiLocalSoundsList(this, 150);
        this.field_146292_n.add(new GuiButton(0, getWidth() / 2, getHeight() - 42, I18n.getStringParams("gui.done")));
        this.field_146292_n.add(new GuiButton(1, 10, getHeight() - 42, 150, 20, "Select File"));
        this.field_146292_n.add(playButton = new GuiButton(2, getWidth() / 2, getHeight() - 102, "Play Sound"));
        playButton.field_146124_l = false;
        this.field_146292_n.add(uploadButton = new GuiButton(3, getWidth() / 2, getHeight() - 72, "Upload"));
        uploadButton.field_146124_l = false;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        try
        {
            this.soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        }
        catch(Exception exception)
        {
        }
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);

        if (selectedSound != null)
        {
            this.getFontRenderer().drawString(selectedSound.getSoundName(), getWidth()/2 + 100 - (this.getFontRenderer().getStringWidth(selectedSound.getSoundName())/2), 30, 0xFFFFFF);

            boolean hasSound = NetworkHandler.hasServerSound(selectedSound.getSoundName());
            String uploaded = hasSound? "Uploaded": "Not uploaded";
            this.getFontRenderer().drawString(uploaded, getWidth()/2 + + 100 - (this.getFontRenderer().getStringWidth(uploaded)/2), 60, hasSound? 0x00FF00: 0xFF0000);

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
                        selectSoundIndex(-1);
                        selectedSound = new Sound(fileChooser.getSelectedFile());
                        onSelectedSoundChanged();
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
                        if (!NetworkHandler.hasServerSound(selectedSound.getSoundName()))
                        {
                            Sound sound = SoundHandler.setupSound(selectedSound.getSoundLocation());
                            NetworkHelper.clientSoundUpload(sound);
                            selectSoundIndex(-1);
                        }
                        else
                        {
                            SoundsCool.proxy.getChannel().writeOutbound(new RemoveSoundPacket(selectedSound.getSoundName()));
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
            if (NetworkHandler.hasServerSound(selectedSound.getSoundName()))
            {
                if (selected == -1)
                {
                    selectSoundIndex(NetworkHandler.uploadedSounds.indexOf(NetworkHandler.getServerSound(selectedSound.getSoundName())));
                }
                uploadButton.field_146126_j = "Remove";
                uploadButton.field_146124_l = NetworkHandler.uploadedSounds.get(selected).getCategory().equals(player.getDisplayName());
            }
            else
            {
                uploadButton.field_146126_j = "Upload";
                uploadButton.field_146124_l = true;
            }
            playButton.field_146124_l = true;
        }
        else
        {
            uploadButton.field_146126_j = "Upload";
            uploadButton.field_146124_l = false;
            playButton.field_146124_l = false;
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

        if (selected >= 0 && selected < SoundHandler.getSounds().size())
        {
            this.selectedSound = SoundHandler.getSounds().get(selected);
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
