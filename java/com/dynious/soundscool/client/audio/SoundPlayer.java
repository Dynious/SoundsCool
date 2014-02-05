package com.dynious.soundscool.client.audio;

import com.dynious.soundscool.handler.SoundHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystem;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

@SideOnly(Side.CLIENT)
public class SoundPlayer
{
    private static SoundSystem soundSystem;

    private static void init()
    {
        SoundManager soundManager = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.audio.SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), "sndManager", "field_147694_f", "V");
        soundSystem = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, soundManager, "sndSystem", "field_148620_e", "e");
    }

    public static void playSound(File sound, String identifier, float x, float y, float z)
    {
        if (soundSystem == null)
        {
            init();
        }
        try
        {
            soundSystem.newSource(false, identifier, sound.toURI().toURL(), sound.getName(), false, x, y, z, 0, 16);
            soundSystem.play(identifier);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void stopSound(String identifier)
    {
        soundSystem.stop(identifier);
    }
}
