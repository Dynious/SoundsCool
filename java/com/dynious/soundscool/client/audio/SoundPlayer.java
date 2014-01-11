package com.dynious.soundscool.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

@SideOnly(Side.CLIENT)
public class SoundPlayer
{
    private static SoundSystem soundSystem;

    private static void init()
    {
        try
        {
            Field soundManagerField = Minecraft.getMinecraft().func_147118_V().getClass().getDeclaredField("field_147694_f");
            soundManagerField.setAccessible(true);
            SoundManager soundManager = (SoundManager)soundManagerField.get(Minecraft.getMinecraft().func_147118_V());

            Field soundSystemField = soundManager.getClass().getDeclaredField("field_148620_e");
            soundSystemField.setAccessible(true);
            soundSystem = (SoundSystem)soundSystemField.get(soundManager);

        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static void playSound(File sound, float x, float y, float z)
    {
        if (soundSystem == null)
        {
            init();
        }
        try
        {
            soundSystem.quickPlay(false, sound.toURI().toURL(), sound.getName(), false, x, y, z, 0, 16);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }
}
