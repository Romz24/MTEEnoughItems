package io.bluebeaker.mteenoughitems.jei.plugins;

import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.thermal.FluidConversionCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

public class ThermalFoundationPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Thermal Foundation";
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        if(MTEEnoughItemsConfig.thermal.fluid_conversion){
        registry.addRecipeCategories(new FluidConversionCategory(guiHelper));}

    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        if(MTEEnoughItemsConfig.thermal.fluid_conversion){
            registry.addRecipes(FluidConversionCategory.getRecipes(jeiHelpers), FluidConversionCategory.UID);
        }
    }
}
