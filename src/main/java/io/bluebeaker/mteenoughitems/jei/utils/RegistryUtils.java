package io.bluebeaker.mteenoughitems.jei.utils;

import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;


public class RegistryUtils {

  public static void tryAddItemCatalyst(IModRegistry registry, ItemStack stack, String... uid) {
    try {
      registry.addRecipeCatalyst(stack, uid);
    } catch (Throwable e) {
      MTEEnoughItems.getLogger().error("Exception adding catalyst:",e);
    }
  }
}