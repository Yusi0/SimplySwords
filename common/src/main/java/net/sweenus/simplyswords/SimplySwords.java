package net.sweenus.simplyswords;

import com.google.gson.JsonObject;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sweenus.simplyswords.client.renderer.BattleStandardRenderer;
import net.sweenus.simplyswords.client.renderer.BattleStandardDarkRenderer;
import net.sweenus.simplyswords.client.renderer.model.BattleStandardDarkModel;
import net.sweenus.simplyswords.client.renderer.model.BattleStandardModel;
import net.sweenus.simplyswords.config.Config;
import net.sweenus.simplyswords.config.SimplySwordsConfig;
import net.sweenus.simplyswords.entity.BattleStandardDarkEntity;
import net.sweenus.simplyswords.entity.BattleStandardEntity;
import net.sweenus.simplyswords.registry.EffectRegistry;
import net.sweenus.simplyswords.registry.EntityRegistry;
import net.sweenus.simplyswords.registry.ItemsRegistry;
import net.sweenus.simplyswords.registry.SoundRegistry;
import net.sweenus.simplyswords.util.ModLootTableModifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class SimplySwords {
    public static final String MOD_ID = "simplyswords";
    // Registering a new creative tab
    public static final ItemGroup SIMPLYSWORDS = CreativeTabRegistry.create(new Identifier(MOD_ID, "simplyswords"), () ->
            new ItemStack(ItemsRegistry.RUNIC_TABLET.get()));

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static boolean isConfigOutdated;

    public static void init() {

        //CONFIG

        SimplySwordsConfig.init();

        String version = SimplySwordsExpectPlatform.getVersion();
        String defaultConfig = String.format("""
                {
                  "regen_simplyswords_config_file": false,
                  "config_version": %s
                }""", version.substring(0, 4));

        File configFile = Config.createFile("config/simplyswords/backupconfig.json", defaultConfig, false);
        JsonObject json = Config.getJsonObject(Config.readFile(configFile));
        if (json.has("config_version")) {
            if (version.startsWith(json.get("config_version").getAsString())) {
                isConfigOutdated = false;
            }
            else {
                isConfigOutdated = true;
                System.out.println("SimplySwords: It looks like you've updated from a previous version. Please regenerate the Simply Swords configs to get the latest features.");
                //System.out.println(version.substring(0, 4));
            }
        }
        else {
            isConfigOutdated = true;
            System.out.println("SimplySwords: It looks like you've updated from a previous version. Please regenerate the Simply Swords configs to get the latest features.");
            //System.out.println(version.substring(0, 4));
        }

        SimplySwordsConfig.generateConfigs(json == null || !json.has("regen_simplyswords_config_file") || json.get("regen_simplyswords_config_file").getAsBoolean());
        SimplySwordsConfig.loadConfig();


        ItemsRegistry.ITEM.register();
        SoundRegistry.SOUND.register();
        EffectRegistry.EFFECT.register();
        EntityRegistry.ENTITIES.register();
        EntityAttributeRegistry.register(EntityRegistry.BATTLESTANDARD, BattleStandardEntity::createBattleStandardAttributes);
        EntityAttributeRegistry.register(EntityRegistry.BATTLESTANDARDDARK, BattleStandardDarkEntity::createBattleStandardDarkAttributes);
        ModLootTableModifiers.init();

        //Don't announce via in-game chat because that's kinda annoying
        //ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new EventGameStart());
        
        System.out.println(SimplySwordsExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
        EnvExecutor.runInEnv(Env.CLIENT, () -> SimplySwords.Client::initializeClient);

    }

    @Environment(EnvType.CLIENT)
    public static class Client {
        public static final EntityModelLayer BATTLESTANDARD_MODEL = new EntityModelLayer(new Identifier("battlestandard", "cube"), "main");
        public static final EntityModelLayer BATTLESTANDARD_DARK_MODEL = new EntityModelLayer(new Identifier("battlestandarddark", "cube"), "main");
        @Environment(EnvType.CLIENT)
        public static void initializeClient() {
            EntityRendererRegistry.register(EntityRegistry.BATTLESTANDARD, BattleStandardRenderer::new);
            EntityModelLayerRegistry.register(BATTLESTANDARD_MODEL, BattleStandardModel::getTexturedModelData);
            EntityRendererRegistry.register(EntityRegistry.BATTLESTANDARDDARK, BattleStandardDarkRenderer::new);
            EntityModelLayerRegistry.register(BATTLESTANDARD_DARK_MODEL, BattleStandardDarkModel::getTexturedModelData);
        }
    }

}
