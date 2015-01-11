package com.dynious.soundscool.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.lib.Names;
import com.dynious.soundscool.lib.Reference;
import com.dynious.soundscool.tileentity.TileSoundPlayer;

public class BlockSoundPlayer extends Block implements ITileEntityProvider
{
    public BlockSoundPlayer()
    {
        super(Material.rock);
        this.setCreativeTab(SoundsCool.tabSoundsCool);
        this.setHardness(2F);
        this.setResistance(10F);
        this.setStepSound(Block.soundTypeStone);
        this.setUnlocalizedName(Reference.modid+":"+Names.soundPlayer);
    }

    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileSoundPlayer();
    }
     


    @Override
    public boolean onBlockActivated(World world, BlockPos pos,
    		IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                player.openGui(SoundsCool.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos p, EnumFacing side)
    {
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
        	if(world.isBlockIndirectlyGettingPowered(pos) > 0)
        		((TileSoundPlayer)tile).setPowered(true);
        	else
            ((TileSoundPlayer)tile).setPowered(false);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
        	if(world.isBlockIndirectlyGettingPowered(pos) > 0)
        		((TileSoundPlayer)tile).setPowered(true);
        	else
        		((TileSoundPlayer)tile).setPowered(false);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile =  world.getTileEntity(pos);
        if (tile != null && tile instanceof TileSoundPlayer)
        {
            ((TileSoundPlayer)tile).stopCurrentSound();
        }
        super.breakBlock(world, pos, state);
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
