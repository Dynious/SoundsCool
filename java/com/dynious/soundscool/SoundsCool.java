package com.dynious.soundscool;

import com.dynious.soundscool.block.ModBlocks;
import com.dynious.soundscool.command.CommandSoundsCool;
import com.dynious.soundscool.creativetab.CreativeTabSoundsCool;
import com.dynious.soundscool.handler.GuiHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.lib.Reference;
import com.dynious.soundscool.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class SoundsCool
{
    @Instance(Reference.modid)
    public static SoundsCool instance;

    @SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
    public static CommonProxy proxy;

    public static CreativeTabs tabSoundsCool = new CreativeTabSoundsCool(CreativeTabs.getNextID(), Reference.modid);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.soundSetup();

        proxy.UISetup();

        SoundHandler.findSounds();

        ModBlocks.init();

        proxy.initNetworking();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

        proxy.initTileEntities();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSoundsCool());
    }
}
