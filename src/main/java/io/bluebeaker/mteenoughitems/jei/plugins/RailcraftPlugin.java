package io.bluebeaker.mteenoughitems.jei.plugins;

import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.railcraft.BlastFurnaceFuelCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.BoilerCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.FluidFireboxCategory;
import io.bluebeaker.mteenoughitems.jei.railcraft.WorldSpikeFuelCategory;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mods.railcraft.client.gui.GuiBlastFurnace;
import mods.railcraft.client.gui.GuiBoiler;
import mods.railcraft.client.gui.GuiWorldspike;

public class RailcraftPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Railcraft";
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
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

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeClickArea(GuiBlastFurnace.class,56,36,14,14,BlastFurnaceFuelCategory.UID);
        registry.addRecipeClickArea(GuiWorldspike.class,90,23,32,18,WorldSpikeFuelCategory.UID);

        registry.addRecipeClickArea(GuiBoiler.class,62,38,14,14,FluidFireboxCategory.UID,BoilerCategory.UID);
        registry.addRecipeClickArea(GuiBoiler.class,62,22,14,14,BoilerCategory.UID);

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
    }
}
