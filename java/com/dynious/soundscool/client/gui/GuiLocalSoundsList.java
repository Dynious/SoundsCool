package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.sound.Sound;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraft.client.renderer.Tessellator;

public class GuiLocalSoundsList extends GuiScrollingList
{
    private IListGui parent;

    public GuiLocalSoundsList(IListGui parent, int listWidth)
    {
        super(parent.getMinecraftInstance(), listWidth, parent.getWidth(), 32, parent.getHeight() - 64, 10, 35);
        this.parent = parent;
    }

    @Override
    protected int getSize()
    {
        return SoundHandler.getLocalSounds().size();
    }

    @Override
    protected void elementClicked(int var1, boolean var2)
    {
        this.parent.selectSoundIndex(var1);
    }

    @Override
    protected boolean isSelected(int var1)
    {
        return this.parent.soundIndexSelected(var1);
    }

    @Override
    protected void drawBackground()
    {
        this.parent.drawBackground();
    }

    @Override
    protected int getContentHeight()
    {
        return (this.getSize()) * 35 + 1;
    }

    @Override
    protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5)
    {
        Sound sound = SoundHandler.getLocalSounds().get(listIndex);
        if (sound != null)
        {
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(sound.getSoundName(), listWidth - 10), this.left + 3 , var3 + 2, 0xFFFFFF);
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(sound.hasRemote()? "Uploaded": "Not uploaded", listWidth - 10), this.left + 3 , var3 + 12, 0xCCCCCC);
        }
    }
}
