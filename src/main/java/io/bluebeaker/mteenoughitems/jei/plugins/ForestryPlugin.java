package io.bluebeaker.mteenoughitems.jei.plugins;

import forestry.energy.gui.GuiEngineBiogas;
import forestry.energy.gui.GuiEnginePeat;
import forestry.energy.gui.GuiGenerator;
import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.forestry.BeekeepingFlowersCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.BioGeneratorCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.BiogasEngineCategory;
import io.bluebeaker.mteenoughitems.jei.forestry.PeatEngineCategory;
import io.bluebeaker.mteenoughitems.jei.utils.RegistryUtils;
import io.bluebeaker.mteenoughitems.utils.ItemUtils;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

public class ForestryPlugin implements IPlugin {

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
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

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

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
}
