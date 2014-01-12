package com.dynious.soundscool.block;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.lib.Names;
import com.dynious.soundscool.lib.Reference;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSoundPlayer extends BlockContainer
{
    public BlockSoundPlayer()
    {
        super(Material.field_151576_e);
        this.func_149647_a(SoundsCool.tabSoundsCool);
        this.func_149663_c(Names.soundPlayer);
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

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    //onBlockAdded
    public void func_149726_b(World world, int x, int y, int z)
    {
        TileEntity tile = world.func_147438_o(x, y, z);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    //onBlockNeighbourChanged
    public void func_149695_a(World world, int x, int y, int z, Block block)
    {
        TileEntity tile = world.func_147438_o(x, y, z);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    private IIcon blockTop;
    private IIcon blockSide;

    //registerIcons()
    @Override
    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister)
    {
        blockTop = iconRegister.registerIcon(Reference.modid + ":" + String.format("%s_top", getUnwrappedUnlocalizedName(this.func_149739_a())));
        blockSide = iconRegister.registerIcon(Reference.modid + ":" + String.format("%s_side", getUnwrappedUnlocalizedName(this.func_149739_a())));
    }

    //getIcon()
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int metaData)
    {
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP || ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
        {
            return blockTop;
        }
        else
        {
            return blockSide;
        }
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
