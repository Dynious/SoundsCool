package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.helper.NetworkHelper;
import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.renderer.Tessellator;

import java.io.File;
import java.util.ArrayList;

public class GuiSoundsList extends GuiScrollingList
{
    private GuiSounds parent;
    private ArrayList<File> sounds;

    public GuiSoundsList(GuiSounds parent, ArrayList<File> sounds, int listWidth)
    {
        super(parent.getMinecraftInstance(), listWidth, parent.field_146295_m, 32, parent.field_146295_m - 32 + 4, 10, 35);
        this.parent = parent;
        this.sounds = sounds;
    }

    @Override
    protected int getSize()
    {
        return sounds.size();
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
        this.parent.func_146276_q_();
    }

    @Override
    protected int getContentHeight()
    {
        return (this.getSize()) * 35 + 1;
    }

    @Override
    protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5)
    {
        File file = sounds.get(listIndex);
        if (file != null)
        {
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(file.getName(), listWidth - 10), this.left + 3 , var3 + 2, 0xFFFFFF);
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(NetworkHelper.hasServerSound(file.getName())? "Uploaded": "Not uploaded", listWidth - 10), this.left + 3 , var3 + 12, 0xCCCCCC);
        }
    }
}
