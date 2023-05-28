package io.github.adainish.cobbledboostersforge.conf;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledboostersforge.CobbledBoostersForge;
import io.github.adainish.cobbledboostersforge.util.Adapters;

import java.io.*;

public class LanguageConfig
{
    public String prefix;
    public String splitter;
    public String expiredMessage;
    public String activatedMessage;

    public LanguageConfig()
    {
        this.prefix = "&6[&bBoosters&6]";
        this.splitter = " » ";
        this.expiredMessage = "&cThe %amount%x %type% has expired...";
        this.activatedMessage = "&aA %amount%x %type% Booster has started that lasts for %timer%";
    }

    public static void writeConfig()
    {
        File dir = CobbledBoostersForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        LanguageConfig config = new LanguageConfig();
        try {
            File file = new File(dir, "language.json");
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

    public static LanguageConfig getConfig()
    {
        File dir = CobbledBoostersForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "language.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledBoostersForge.log.error("Something went wrong attempting to read the Language Config");
            return null;
        }

        return gson.fromJson(reader, LanguageConfig.class);
    }
}
