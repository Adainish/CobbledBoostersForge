package io.github.adainish.cobbledboostersforge;

import io.github.adainish.cobbledboostersforge.cmd.Command;
import io.github.adainish.cobbledboostersforge.conf.LanguageConfig;
import io.github.adainish.cobbledboostersforge.listener.PlayerListener;
import io.github.adainish.cobbledboostersforge.scheduler.AsyncTask;
import io.github.adainish.cobbledboostersforge.storage.BoosterStorage;
import io.github.adainish.cobbledboostersforge.subscriptions.Subscriptions;
import io.github.adainish.cobbledboostersforge.tasks.UpdateBoostersTask;
import io.github.adainish.cobbledboostersforge.wrapper.PlayerWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CobbledBoostersForge.MODID)
public class CobbledBoostersForge {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobbledboostersforge";
    // Directly reference a slf4j logger
    public static final String MOD_NAME = "CobbledBoosters";
    public static final String VERSION = "1.0.0";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2023";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    public static BoosterStorage boosterStorage;
    private static MinecraftServer server;
    private static File configDir;
    private static File storageDir;
    private static File playerStorageDir;

    public List<AsyncTask> asyncTaskList = new ArrayList<>();
    public static Subscriptions subscriptions;

    public static PlayerWrapper playerWrapper;
    public static LanguageConfig languageConfig;
    public CobbledBoostersForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        initDirs();
    }


    //permission event
    @SubscribeEvent
    public void onPermissionRegistry(PermissionGatherEvent.Nodes event)
    {
        //register admin nodes

    }

    @SubscribeEvent
    public void onCommandRegistry(RegisterCommandsEvent event) {

        //register commands
        event.getDispatcher().register(Command.getCommand());

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        server = ServerLifecycleHooks.getCurrentServer();

        initConfig();
        initStorage();

        subscriptions = new Subscriptions();

        playerWrapper = new PlayerWrapper();

        AsyncTask.Builder builder = new AsyncTask.Builder();
        AsyncTask task = builder.withInfiniteIterations().withInterval(20).withRunnable(new UpdateBoostersTask()).build();
        task.start();
        asyncTaskList.add(task);

        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    @SubscribeEvent
    public void onServerShutDown(ServerStoppingEvent event)
    {
        if (!asyncTaskList.isEmpty())
        {
            log.warn("Shutting down tasks...");
            for (AsyncTask t:asyncTaskList) {
                t.stop();
            }
            asyncTaskList.clear();
        }
    }


    public void initDirs()
    {
        log.warn("Writing directories");
        setConfigDir(new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()) + "/CobbledBoosters/"));
        getConfigDir().mkdir();
        setStorageDir(new File(getConfigDir(), "/storage/"));
        getStorageDir().mkdirs();
        setPlayerStorageDir(new File(configDir, "/playerstorage/"));
        getPlayerStorageDir().mkdirs();
    }

    public void initConfig()
    {
        //nothing really happens here yet
        log.warn("Writing language config if not present yet...");
        LanguageConfig.writeConfig();
        log.warn("Loading language config");
        languageConfig = new LanguageConfig();

    }

    public void initStorage()
    {
        BoosterStorage.writeStorage();
        boosterStorage = BoosterStorage.getStorage();
    }
    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        CobbledBoostersForge.server = server;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        CobbledBoostersForge.configDir = configDir;
    }

    public static File getStorageDir() {
        return storageDir;
    }

    public static void setStorageDir(File storageDir) {
        CobbledBoostersForge.storageDir = storageDir;
    }

    public static File getPlayerStorageDir() {
        return playerStorageDir;
    }

    public static void setPlayerStorageDir(File playerStorageDir) {
        CobbledBoostersForge.playerStorageDir = playerStorageDir;
    }

}
