package com.dynious.soundscool.helper;

import com.dynious.soundscool.SoundsCool;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class GuiHelper
{
    public static void openGui(int id)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.openGui(SoundsCool.instance, id, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
    }
}
