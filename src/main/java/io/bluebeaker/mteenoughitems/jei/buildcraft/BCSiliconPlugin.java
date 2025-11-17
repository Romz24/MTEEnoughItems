package io.bluebeaker.mteenoughitems.jei.buildcraft;

import buildcraft.compat.module.jei.silicon.CategoryAssemblyTable;
import buildcraft.silicon.container.ContainerAssemblyTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.LogTimer;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class BCSiliconPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry){
        if(ModChecker.BuildcraftSilicon.isLoaded() && MTEEnoughItemsConfig.buildcraft.facade_assembly){
            LogTimer timer = new LogTimer();
            registry.addRecipes(FacadeAssemblyCategory.getRecipes(),FacadeAssemblyCategory.UID);
            RegistryUtils.tryAddItemCatalyst(registry,ItemUtils.getItemstack(ModChecker.BuildcraftSilicon.name,"assembly_table"),FacadeAssemblyCategory.UID);

            registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerAssemblyTable.class, FacadeAssemblyCategory.UID, 36, 12, 0, 36);
            registry.addRecipeClickArea(GuiAssemblyTable.class,86,36,4,70,FacadeAssemblyCategory.UID, CategoryAssemblyTable.UID);

            MTEEnoughItems.getLogger().info("Loaded BuildCraft recipes in {}ms",timer.stagedTime());
        }
    }

    @Override
    public void registerSubtypes(ISubtypeRegistry subtypeRegistry) {
        if(ModChecker.BuildcraftSilicon.isLoaded() && MTEEnoughItemsConfig.buildcraft.gatesubtypes) {
            new GateSubTypeInterpreter().register(subtypeRegistry);
            new FacadeSubTypeInterpreter().register(subtypeRegistry);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(ModChecker.BuildcraftSilicon.isLoaded() && MTEEnoughItemsConfig.buildcraft.facade_assembly){
            registry.addRecipeCategories(new FacadeAssemblyCategory(registry.getJeiHelpers().getGuiHelper()));
        }
    }
}
