package io.bluebeaker.mteenoughitems;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = MTEEnoughItems.MODID,type = Type.INSTANCE,category = "general")
public class MTEEnoughItemsConfig {
    @LangKey("config.mteenoughitems.forestry.name")
    public static Forestry forestry = new Forestry();
    public static class Forestry{
        @Config.RequiresMcRestart
        @LangKey(Categories.Forestry.PEAT_ENGINE)
        public boolean peat_engine = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Forestry.BIOGAS_ENGINE)
        public boolean biogas_engine = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Forestry.BIO_GENERATOR)
        public boolean bio_generator = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Forestry.BEEKEEPING_FLOWERS)
        public boolean beekeeping_flowers = true;
    }
    @LangKey("config.mteenoughitems.railcraft.name")
    public static Railcraft railcraft = new Railcraft();
    public static class Railcraft{
        @Config.RequiresMcRestart
        @LangKey(Categories.Railcraft.FLUID_FIREBOX)
        public boolean fluid_firebox = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Railcraft.BOILER)
        public boolean boiler = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Railcraft.BLAST_FURNACE_FUEL)
        public boolean blast_furnace_fuel = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Railcraft.WORLDSPIKE_FUEL)
        public boolean worldspike_fuel = true;
    }
    @LangKey("config.mteenoughitems.thermal.name")
    public static Thermal thermal = new Thermal();
    public static class Thermal{
        @Config.RequiresMcRestart
        @LangKey(Categories.Thermal.FLUID_CONVERSION)
        public boolean fluid_conversion = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.Thermal.TREE_EXTRACTOR)
        public boolean tree_extractor = true;
    }
    @LangKey("config.mteenoughitems.immersiveEngineering.name")
    public static ImmersiveEngineering immersiveEngineering = new ImmersiveEngineering();
    public static class ImmersiveEngineering{
        @Config.RequiresMcRestart
        @LangKey(Categories.ImmersiveEngineering.DIESEL_GENERATOR)
        public boolean diesel_generator = true;
        @Config.RequiresMcRestart
        @LangKey(Categories.ImmersiveEngineering.MINERAL_DEPOSIT)
        public boolean mineral_deposit = true;
    }
    @LangKey("config.mteenoughitems.buildcraft.name")
    public static Buildcraft buildcraft = new Buildcraft();
    public static class Buildcraft{
        @Config.RequiresMcRestart
        @Config.Comment("Register ingredient subtypes for BC gate and Facades in JEI.")
        @LangKey("config.mteenoughitems.buildcraft.gatesubtypes")
        public boolean gatesubtypes = true;

        @Config.RequiresMcRestart
        @LangKey(Categories.BuildCraft.FACADE_ASSEMBLY)
        public boolean facade_assembly = true;
    }
    @LangKey("config.mteenoughitems.storageDrawers.name")
    public static StorageDrawers storageDrawers = new StorageDrawers();
    public static class StorageDrawers{
        @Config.RequiresMcRestart
        @Config.Comment("Register ingredient subtypes for Storage Drawers in JEI.")
        @LangKey("config.mteenoughitems.storagedrawers.drawer_subtypes")
        public boolean drawer_subtypes = true;
    }
}