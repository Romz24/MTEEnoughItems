package io.bluebeaker.mteenoughitems.jei.plugins;

import cofh.thermalexpansion.gui.client.device.GuiTapper;
import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.thermal.TreeFluidCategory;
import io.bluebeaker.mteenoughitems.jei.thermal.TreeFluidFuelCategory;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

public class ThermalExpansionPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Thermal Expansion";
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        if(MTEEnoughItemsConfig.thermal.tree_extractor){
            registry.addRecipeCategories(new TreeFluidCategory(guiHelper));
            registry.addRecipeCategories(new TreeFluidFuelCategory(guiHelper));
        }
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        if(MTEEnoughItemsConfig.thermal.tree_extractor){
            registry.addRecipes(TreeFluidCategory.getRecipes(jeiHelpers), TreeFluidCategory.UID);
            registry.addRecipes(TreeFluidFuelCategory.getRecipes(jeiHelpers), TreeFluidFuelCategory.UID);

            RegistryUtils.tryAddItemCatalyst(registry, ItemUtils.getItemstack(ModChecker.ThermalExpansion.name, "device",3),TreeFluidCategory.UID,TreeFluidFuelCategory.UID);
            registry.addRecipeClickArea(GuiTapper.class,62,35,16,16,TreeFluidCategory.UID,TreeFluidFuelCategory.UID);
        }
    }
}
