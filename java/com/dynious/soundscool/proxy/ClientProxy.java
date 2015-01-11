package com.dynious.soundscool.proxy;

import com.dynious.soundscool.handler.event.SoundEventHandler;
import com.dynious.soundscool.lib.Names;
import com.dynious.soundscool.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraftforge.common.MinecraftForge;

import javax.swing.*;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
    }

    @Override
    public void soundSetup()
    {
        super.soundSetup();

        MinecraftForge.EVENT_BUS.register(new SoundEventHandler());
    }

    @Override
    public void UISetup()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void registerBlocks()
    {
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(net.minecraft.item.Item.getByNameOrId(Reference.modid+":"+Names.soundPlayer), 0, new ModelResourceLocation(Reference.modid+":"+Names.soundPlayer, "inventory"));
    }
}
