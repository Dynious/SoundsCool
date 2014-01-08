package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.tileentity.TileSoundPlayer;
import net.minecraft.client.gui.GuiScreen;

public class GuiSoundPlayer extends GuiScreen
{
    private TileSoundPlayer tile;

    public GuiSoundPlayer(TileSoundPlayer tile)
    {
        this.tile = tile;
    }
}
