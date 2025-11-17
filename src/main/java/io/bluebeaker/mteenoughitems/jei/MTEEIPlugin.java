package io.bluebeaker.mteenoughitems.jei;

import cofh.thermalexpansion.gui.client.device.GuiTapper;
import forestry.energy.gui.GuiEngineBiogas;
import forestry.energy.gui.GuiEnginePeat;
import forestry.energy.gui.GuiGenerator;
import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.forestry.BeekeepingFlowersCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.BioGeneratorCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.BiogasEngineCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.PeatEngineCategory;
import io.bluebeaker.mteenoughitems.jei.immersiveengineering.DieselGeneratorCategory;
import io.bluebeaker.mteenoughitems.jei.immersiveengineering.MineralDepositCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.BlastFurnaceFuelCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.BoilerCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.FluidFireboxCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.WorldSpikeFuelCategory;
import io.bluebeaker.mteenoughitems.jei.thermal.FluidConversionCategory;
import io.bluebeaker.mteenoughitems.jei.thermal.TreeFluidCategory;
import io.bluebeaker.mteenoughitems.jei.thermal.TreeFluidFuelCategory;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.LogTimer;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mods.railcraft.client.gui.GuiBlastFurnace;
import mods.railcraft.client.gui.GuiBoilerFluid;
import mods.railcraft.client.gui.GuiBoilerSolid;
import mods.railcraft.client.gui.GuiWorldspike;
import net.minecraft.item.ItemStack;


@JEIPlugin
public class MTEEIPlugin implements IModPlugin {

  private static IJeiRuntime jeiRuntime = null;
  public static IModRegistry modRegistry;

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
    if(ModChecker.Forestry.isLoaded()){
      if(MTEEnoughItemsConfig.forestry.peat_engine) {
        registry.addRecipeCategories(new PeatEngineCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.forestry.biogas_engine) {
        registry.addRecipeCategories(new BiogasEngineCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.forestry.bio_generator && ModChecker.IC2.isLoaded()) {
        registry.addRecipeCategories(new BioGeneratorCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.forestry.beekeeping_flowers) {
        registry.addRecipeCategories(new BeekeepingFlowersCategory(guiHelper));
      }

    }
    if(ModChecker.Railcraft.isLoaded()){
      if(MTEEnoughItemsConfig.railcraft.fluid_firebox) {
        registry.addRecipeCategories(new FluidFireboxCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.railcraft.boiler) {
        registry.addRecipeCategories(new BoilerCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.railcraft.blast_furnace_fuel) {
        registry.addRecipeCategories(new BlastFurnaceFuelCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.railcraft.worldspike_fuel) {
        registry.addRecipeCategories(new WorldSpikeFuelCategory(guiHelper));
      }
    }
    if(ModChecker.ThermalFoundation.isLoaded() && MTEEnoughItemsConfig.thermal.fluid_conversion){
      registry.addRecipeCategories(new FluidConversionCategory(guiHelper));
    }
    if(ModChecker.ThermalExpansion.isLoaded() && MTEEnoughItemsConfig.thermal.tree_extractor){
      registry.addRecipeCategories(new TreeFluidCategory(guiHelper));
      registry.addRecipeCategories(new TreeFluidFuelCategory(guiHelper));
    }
    if(ModChecker.ImmersiveEngineering.isLoaded()){
      if(MTEEnoughItemsConfig.immersiveEngineering.diesel_generator){
        registry.addRecipeCategories(new DieselGeneratorCategory(guiHelper));
      }
      if(MTEEnoughItemsConfig.immersiveEngineering.mineral_deposit){
        registry.addRecipeCategories(new MineralDepositCategory(guiHelper));
      }
    }
  }

  @Override
  public void register(IModRegistry registry) {
    modRegistry=registry;
    IJeiHelpers jeiHelpers = registry.getJeiHelpers();

    LogTimer timer = new LogTimer();
    MTEEnoughItems.getLogger().info("Started loading recipes...");

    if(ModChecker.Forestry.isLoaded()){
      registry.addRecipeClickArea(GuiEngineBiogas.class,52,27,36,14,BiogasEngineCategory.UID);
      registry.addRecipeClickArea(GuiGenerator.class,68,38,40,18,BioGeneratorCategory.UID);
      registry.addRecipeClickArea(GuiEnginePeat.class,45,27,14,14, PeatEngineCategory.UID);

      if(MTEEnoughItemsConfig.forestry.peat_engine){
        registry.addRecipes(PeatEngineCategory.getRecipes(jeiHelpers),PeatEngineCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Forestry.name, "engine_peat"),PeatEngineCategory.UID);
      }
      if(MTEEnoughItemsConfig.forestry.biogas_engine){
        registry.addRecipes(BiogasEngineCategory.getRecipes(jeiHelpers),BiogasEngineCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Forestry.name, "engine_biogas"),BiogasEngineCategory.UID);
      }

      if(MTEEnoughItemsConfig.forestry.bio_generator && ModChecker.IC2.isLoaded()){
        registry.addRecipes(BioGeneratorCategory.getRecipes(jeiHelpers),BioGeneratorCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Forestry.name, "engine_generator"),BioGeneratorCategory.UID);
      }

      if(MTEEnoughItemsConfig.forestry.beekeeping_flowers) {
        registry.addRecipes(BeekeepingFlowersCategory.getRecipes(jeiHelpers),BeekeepingFlowersCategory.UID);

        for(String name: new String[]{"apiary", "bee_house", "alveary.plain", "cart.beehouse"}){
          ItemStack itemstack = ItemUtils.getItemstack(ModChecker.Forestry.name, name);
          if(!itemstack.isEmpty())
            RegistryUtils.tryAddItemCatalyst(registry, itemstack,BeekeepingFlowersCategory.UID);
        };
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Forestry.name, "cart.beehouse",1),BeekeepingFlowersCategory.UID);
      }
    }

    MTEEnoughItems.getLogger().info("Loaded Forestry recipes in {}ms",timer.stagedTime());

    if(ModChecker.Railcraft.isLoaded()){
      registry.addRecipeClickArea(GuiBlastFurnace.class,56,36,14,14,BlastFurnaceFuelCategory.UID);
      registry.addRecipeClickArea(GuiWorldspike.class,90,23,32,18,WorldSpikeFuelCategory.UID);

      registry.addRecipeClickArea(GuiBoilerFluid.class,62,38,14,14,FluidFireboxCategory.UID,BoilerCategory.UID);
      registry.addRecipeClickArea(GuiBoilerSolid.class,62,22,14,14,BoilerCategory.UID);

      for (int i = 1; i <=3; i++) {
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "worldspike",i), WorldSpikeFuelCategory.UID);
      }

      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "blast_furnace"), BlastFurnaceFuelCategory.UID);

      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "boiler_firebox_solid"), BoilerCategory.UID);
      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "locomotive_steam_solid"), BoilerCategory.UID);

      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "boiler_firebox_fluid"),FluidFireboxCategory.UID, BoilerCategory.UID);
      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "boiler_tank_pressure_high"),FluidFireboxCategory.UID, BoilerCategory.UID);
      RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.Railcraft.name, "boiler_tank_pressure_low"),FluidFireboxCategory.UID, BoilerCategory.UID);

      if(MTEEnoughItemsConfig.railcraft.fluid_firebox){
        registry.addRecipes(FluidFireboxCategory.getRecipes(jeiHelpers), FluidFireboxCategory.UID);
      }
      if(MTEEnoughItemsConfig.railcraft.boiler) {
        registry.addRecipes(BoilerCategory.getRecipes(jeiHelpers), BoilerCategory.UID);
      }
      if(MTEEnoughItemsConfig.railcraft.blast_furnace_fuel) {
        registry.addRecipes(BlastFurnaceFuelCategory.getRecipes(jeiHelpers), BlastFurnaceFuelCategory.UID);
      }
      if(MTEEnoughItemsConfig.railcraft.worldspike_fuel) {
        registry.addRecipes(WorldSpikeFuelCategory.getRecipes(jeiHelpers), WorldSpikeFuelCategory.UID);
      }
      MTEEnoughItems.getLogger().info("Loaded Railcraft recipes in {}ms",timer.stagedTime());
    }


    if(ModChecker.ThermalFoundation.isLoaded() && MTEEnoughItemsConfig.thermal.fluid_conversion){
      registry.addRecipes(FluidConversionCategory.getRecipes(jeiHelpers), FluidConversionCategory.UID);
      MTEEnoughItems.getLogger().info("Loaded Thermal Foundation recipes in {}ms",timer.stagedTime());
    }
    if(ModChecker.ThermalExpansion.isLoaded() && MTEEnoughItemsConfig.thermal.tree_extractor){
      registry.addRecipes(TreeFluidCategory.getRecipes(jeiHelpers), TreeFluidCategory.UID);
      registry.addRecipes(TreeFluidFuelCategory.getRecipes(jeiHelpers), TreeFluidFuelCategory.UID);

      RegistryUtils.tryAddItemCatalyst(registry,ItemUtils.getItemstack(ModChecker.ThermalExpansion.name, "device",3),TreeFluidCategory.UID,TreeFluidFuelCategory.UID);
      registry.addRecipeClickArea(GuiTapper.class,62,35,16,16,TreeFluidCategory.UID,TreeFluidFuelCategory.UID);

      MTEEnoughItems.getLogger().info("Loaded Thermal Expansion recipes in {}ms",timer.stagedTime());
    }

    if(ModChecker.ImmersiveEngineering.isLoaded()){
      if (MTEEnoughItemsConfig.immersiveEngineering.diesel_generator){
        registry.addRecipes(DieselGeneratorCategory.getRecipes(jeiHelpers),DieselGeneratorCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.ImmersiveEngineering.name,"metal_multiblock",10),DieselGeneratorCategory.UID);
      }

      if (MTEEnoughItemsConfig.immersiveEngineering.mineral_deposit){
        registry.addRecipes(MineralDepositCategory.getRecipes(jeiHelpers),MineralDepositCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.ImmersiveEngineering.name,"metal_device1",7),MineralDepositCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.ImmersiveEngineering.name,"metal_multiblock",11),MineralDepositCategory.UID);
        RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.ImmersiveEngineering.name,"metal_multiblock",12),MineralDepositCategory.UID);
      }
      MTEEnoughItems.getLogger().info("Loaded Immersive Engineering recipes in {}ms",timer.stagedTime());
    }
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntimeIn) {
    MTEEIPlugin.jeiRuntime = jeiRuntimeIn;
  }
}