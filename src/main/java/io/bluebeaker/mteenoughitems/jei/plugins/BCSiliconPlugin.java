package io.bluebeaker.mteenoughitems.jei.plugins;

import buildcraft.compat.module.jei.silicon.CategoryAssemblyTable;
import buildcraft.silicon.container.ContainerAssemblyTable;
import buildcraft.silicon.gui.GuiAssemblyTable;
import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.buildcraft.FacadeAssemblyCategory;
import io.bluebeaker.mteenoughitems.jei.buildcraft.FacadeSubTypeInterpreter;
import io.bluebeaker.mteenoughitems.jei.buildcraft.GateSubTypeInterpreter;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

public class BCSiliconPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Buildcraft Silicon";
    }

    @Override
    public void register(IModRegistry registry){
        if(MTEEnoughItemsConfig.buildcraft.facade_assembly){
            registry.addRecipes(FacadeAssemblyCategory.getRecipes(),FacadeAssemblyCategory.UID);
            RegistryUtils.tryAddItemCatalyst(registry,ItemUtils.getItemstack(ModChecker.BuildcraftSilicon.name,"assembly_table"),FacadeAssemblyCategory.UID);

            registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerAssemblyTable.class, FacadeAssemblyCategory.UID, 36, 12, 0, 36);
            registry.addRecipeClickArea(GuiAssemblyTable.class,86,36,4,70,FacadeAssemblyCategory.UID, CategoryAssemblyTable.UID);
        }
    }

    @Override
    public void registerSubtypes(ISubtypeRegistry subtypeRegistry) {
        if(MTEEnoughItemsConfig.buildcraft.gatesubtypes) {
            new GateSubTypeInterpreter().register(subtypeRegistry);
            new FacadeSubTypeInterpreter().register(subtypeRegistry);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(MTEEnoughItemsConfig.buildcraft.facade_assembly){
            registry.addRecipeCategories(new FacadeAssemblyCategory(registry.getJeiHelpers().getGuiHelper()));
        }
    }
}
