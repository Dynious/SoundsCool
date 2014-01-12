package com.dynious.soundscool.block;

import com.dynious.soundscool.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModBlocks
{
    public static BlockSoundPlayer soundPlayer;

    public static void init()
    {
        soundPlayer = new BlockSoundPlayer();

        GameRegistry.registerBlock(soundPlayer, Names.soundPlayer);

        GameRegistry.addShapedRecipe(new ItemStack(soundPlayer), "IRI", "RJR", "IRI", 'I', Items.iron_ingot, 'R', Items.redstone, 'J', Blocks.jukebox);
    }
}
