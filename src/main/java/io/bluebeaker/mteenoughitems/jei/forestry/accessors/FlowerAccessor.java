package io.bluebeaker.mteenoughitems.jei.forestry.accessors;

import com.google.common.collect.HashMultimap;
import forestry.apiculture.flowers.Flower;
import forestry.apiculture.flowers.FlowerRegistry;
import forestry.core.utils.BlockStateSet;
import io.bluebeaker.mteenoughitems.utils.FieldAccessor;
import net.minecraft.block.Block;

import java.util.Map;

public class FlowerAccessor {
    public static FieldAccessor<FlowerRegistry, HashMultimap<String, Block>> acceptableBlocks = new FieldAccessor<>(FlowerRegistry.class, "acceptableBlocks");
    public static FieldAccessor<FlowerRegistry, Map<String, BlockStateSet>> acceptableBlockStates = new FieldAccessor<>(FlowerRegistry.class, "acceptableBlockStates");
    public static FieldAccessor<FlowerRegistry, HashMultimap<String, Flower>> plantableFlowers = new FieldAccessor<>(FlowerRegistry.class, "plantableFlowers");
}
