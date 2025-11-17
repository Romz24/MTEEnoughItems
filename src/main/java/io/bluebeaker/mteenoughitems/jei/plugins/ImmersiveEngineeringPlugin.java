package io.bluebeaker.mteenoughitems.jei.plugins;

import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.immersiveengineering.DieselGeneratorCategory;
import io.bluebeaker.mteenoughitems.jei.immersiveengineering.MineralDepositCategory;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

public class ImmersiveEngineeringPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Immersive Engineering";
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        if(MTEEnoughItemsConfig.immersiveEngineering.diesel_generator){
            registry.addRecipeCategories(new DieselGeneratorCategory(guiHelper));
        }
        if(MTEEnoughItemsConfig.immersiveEngineering.mineral_deposit){
            registry.addRecipeCategories(new MineralDepositCategory(guiHelper));
        }
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
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
    }
}
