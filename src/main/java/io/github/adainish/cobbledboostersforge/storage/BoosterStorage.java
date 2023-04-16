package io.github.adainish.cobbledboostersforge.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.data.BoosterManager;
import io.github.adainish.cobbledboostersforge.util.Adapters;

import java.io.*;

public class BoosterStorage
{
    public BoosterManager boosterManager;

    public BoosterStorage()
    {
        this.boosterManager = new BoosterManager();
    }


    public void save()
    {
        File dir = CobbledBoostersForge.getStorageDir();
        dir.mkdirs();

        File file = new File(dir, "globalstorage.json");
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobbledBoostersForge.log.error("Something went wrong attempting to read the Booster Storage");
            return;
        }


        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStorage()
    {
        File dir = CobbledBoostersForge.getStorageDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        BoosterStorage config = new BoosterStorage();
        try {
            File file = new File(dir, "globalstorage.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            CobbledBoostersForge.log.warn(e);
        }
    }

    public static BoosterStorage getStorage()
    {
        File dir = CobbledBoostersForge.getStorageDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "globalstorage.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledBoostersForge.log.error("Something went wrong attempting to read the storage file");
            return null;
        }

        return gson.fromJson(reader, BoosterStorage.class);
    }
}
