package com.dynious.soundscool.handler;

import com.dynious.soundscool.client.gui.GuiSounds;
import com.dynious.soundscool.inventory.ContainerSounds;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(ID)
        {
            case 0:
                System.out.println("Opening CON");
                return new ContainerSounds();
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(ID)
        {
            case 0:
                System.out.println("Opening GUI");
                return new GuiSounds(player);
            default:
                return null;
        }
    }
}
