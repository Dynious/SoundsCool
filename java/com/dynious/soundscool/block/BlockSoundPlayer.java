package com.dynious.soundscool.block;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSoundPlayer extends BlockContainer
{
    public BlockSoundPlayer()
    {
        super(Material.field_151576_e);
        this.func_149647_a(CreativeTabs.tabBlock);
    }

    @Override
    public TileEntity func_149915_a(World var1, int var2)
    {
        return new TileSoundPlayer();
    }

    @Override
    public boolean func_149727_a(World world, int x, int y, int z,
                                 EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.func_147438_o(x, y, z);
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                player.openGui(SoundsCool.instance, 1, world, x, y, z);
            }
        }
        return true;
    }

}
