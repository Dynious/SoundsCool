package com.dynious.soundscool.helper;

import com.dynious.soundscool.handler.SoundHandler;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class NetworkHelper
{
    public static byte[] convertFileToByteArr(File file)
    {
        if (file != null && file.exists())
        {
            try
            {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void createFileFromByteArr(byte[] byteFile, String category, String fileName)
    {
        if (byteFile != null && byteFile.length > 0 && !category.isEmpty() && !fileName.isEmpty())
        {
            File file = new File(SoundHandler.getSoundsFolder().getAbsolutePath() + File.separator + category + File.separator + fileName);
            try
            {
                FileUtils.writeByteArrayToFile(file, byteFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
