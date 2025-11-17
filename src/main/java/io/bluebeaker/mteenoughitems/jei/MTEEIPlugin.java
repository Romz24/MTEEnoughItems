package io.bluebeaker.mteenoughitems.jei;

import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import io.bluebeaker.mteenoughitems.jei.plugins.*;
import io.bluebeaker.mteenoughitems.utils.LogTimer;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


@JEIPlugin
public class MTEEIPlugin implements IModPlugin {

  private static IJeiRuntime jeiRuntime = null;
  public static IModRegistry modRegistry;

  public final List<IPlugin> plugins = new ArrayList<>();

  public enum Plugins{
    Forestry(ModChecker.Forestry,ForestryPlugin::new),
    Railcraft(ModChecker.Railcraft,RailcraftPlugin::new),
    BuildcraftSilicon(ModChecker.BuildcraftSilicon,BCSiliconPlugin::new),
    StorageDrawers(ModChecker.StorageDrawers,StorageDrawersPlugin::new),

    ThermalFoundation(ModChecker.ThermalFoundation,ThermalFoundationPlugin::new),
    ThermalExpansion(ModChecker.ThermalExpansion,ThermalExpansionPlugin::new);

    private final ModChecker modChecker;
    private final Supplier<IPlugin> supplier;
    Plugins(ModChecker modChecker, Supplier<IPlugin> supplier) {
        this.modChecker = modChecker;
        this.supplier = supplier;
    }
  }

  public MTEEIPlugin(){
    for (Plugins entry: Plugins.values()) {
      if(!entry.modChecker.isLoaded()) continue;
      try {
        plugins.add(entry.supplier.get());
      }catch (Exception e){
        MTEEnoughItems.getLogger().error("Error init plugin for mod {}:",entry.modChecker.name,e);
      }
    }
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    for (IPlugin plugin : plugins) {
      try {
        plugin.registerCategories(registry);
      } catch (Throwable e) {
        MTEEnoughItems.getLogger().error("Error registering categories for {}:",plugin.getName(),e);
      }
    }
  }

  @Override
  public void register(IModRegistry registry) {
    modRegistry=registry;

    LogTimer timer = new LogTimer();
    MTEEnoughItems.getLogger().info("Started loading recipes...");

    for (IPlugin plugin : plugins) {
      try {
        plugin.register(registry);
        MTEEnoughItems.getLogger().info("Loaded {} recipes in {}ms",plugin.getName(),timer.stagedTime());
      } catch (Throwable e) {
        MTEEnoughItems.getLogger().error("Error loading recipes for {}:",plugin.getName(),e);
        timer.stagedTime();
      }
    }
  }

  @Override
  public void registerSubtypes(ISubtypeRegistry subtypeRegistry) {
    for (IPlugin plugin : plugins) {
      try {
        plugin.registerSubtypes(subtypeRegistry);
      } catch (Throwable e) {
        MTEEnoughItems.getLogger().error("Error registering subtypes for {}:",plugin.getName(),e);
      }
    }
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntimeIn) {
    MTEEIPlugin.jeiRuntime = jeiRuntimeIn;
  }
}