package io.bluebeaker.mteenoughitems.jei.plugins;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 * A wrapper class for control over whether and when to load the plugin, depends on the existence of the respective mod
 */
public interface IPlugin {

    String getName();

    default void registerCategories(IRecipeCategoryRegistration registry) {}
    default void register(IModRegistry registry) {}
    default void registerSubtypes(ISubtypeRegistry subtypeRegistry) {}
}
