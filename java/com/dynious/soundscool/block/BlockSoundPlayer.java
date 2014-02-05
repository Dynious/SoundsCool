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
        super(Material.rock);
        this.setCreativeTab(SoundsCool.tabSoundsCool);
        this.setBlockName(Names.soundPlayer);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileSoundPlayer();
    }



    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                 EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.getTileEntity(x, y, z);
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

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity tile =  world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).stopCurrentSound();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    private IIcon blockTop;
    private IIcon blockSide;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockTop = iconRegister.registerIcon(Reference.modid + ":" + String.format("%s_top", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
        blockSide = iconRegister.registerIcon(Reference.modid + ":" + String.format("%s_side", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaData)
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
