package io.bluebeaker.mteenoughitems.jei.plugins;

import io.bluebeaker.mteenoughitems.MTEEnoughItemsConfig;
import io.bluebeaker.mteenoughitems.jei.storagedrawers.DrawerSubtypeInterpreter;
import mezz.jei.api.ISubtypeRegistry;

public class StorageDrawersPlugin implements IPlugin {
    @Override
    public void registerSubtypes(ISubtypeRegistry subtypeRegistry) {
        if(MTEEnoughItemsConfig.storageDrawers.drawer_subtypes)
            new DrawerSubtypeInterpreter().register(subtypeRegistry);
    }

    @Override
    public String getName() {
        return "Storage Drawers";
    }
}
