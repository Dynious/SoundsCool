package com.dynious.soundscool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.dynious.soundscool.block.ModBlocks;
import com.dynious.soundscool.command.CommandSoundsCool;
import com.dynious.soundscool.creativetab.CreativeTabSoundsCool;
import com.dynious.soundscool.handler.GuiHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.lib.Reference;
import com.dynious.soundscool.network.packet.SoundChunkPacket;
import com.dynious.soundscool.network.packet.SoundUploadedPacket;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.RemoveSoundPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerPlayPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.network.packet.server.OpenGUIPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import com.dynious.soundscool.network.packet.server.SoundNotFoundPacket;
import com.dynious.soundscool.network.packet.server.SoundReceivedPacket;
import com.dynious.soundscool.network.packet.server.SoundRemovedPacket;
import com.dynious.soundscool.network.packet.server.StopSoundPacket;
import com.dynious.soundscool.network.packet.server.UploadedSoundsPacket;
import com.dynious.soundscool.proxy.CommonProxy;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class SoundsCool
{
    @Instance(Reference.modid)
    public static SoundsCool instance;

    @SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
    public static CommonProxy proxy;

    public static CreativeTabs tabSoundsCool = new CreativeTabSoundsCool(CreativeTabs.getNextID(), Reference.modid);
    
    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("soundscool");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.registerMessages();
    	
    	proxy.soundSetup();

        proxy.UISetup();

        SoundHandler.findSounds();

        ModBlocks.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerBlocks();

        proxy.initTileEntities();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSoundsCool());
    }
}
