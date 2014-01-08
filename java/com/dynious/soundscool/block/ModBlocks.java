package com.dynious.soundscool.block;

import com.dynious.soundscool.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static BlockSoundPlayer soundPlayer;

    public static void init()
    {
        soundPlayer = new BlockSoundPlayer();

        GameRegistry.registerBlock(soundPlayer, Names.soundPlayer);
    }
}
