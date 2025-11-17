package io.bluebeaker.mteenoughitems.jei.forestry;

import com.google.common.collect.HashMultimap;
import forestry.api.apiculture.FlowerManager;
import forestry.api.genetics.IFlowerRegistry;
import forestry.apiculture.flowers.Flower;
import forestry.apiculture.flowers.FlowerProvider;
import forestry.apiculture.flowers.FlowerRegistry;
import forestry.core.genetics.alleles.EnumAllele;
import forestry.core.utils.BlockStateSet;
import io.bluebeaker.mteenoughitems.Categories;
import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import io.bluebeaker.mteenoughitems.jei.forestry.accessors.FlowerAccessor;
import io.bluebeaker.mteenoughitems.jei.generic.GenericRecipeCategory;
import io.bluebeaker.mteenoughitems.jei.utils.BlockTooltipCallbacks;
import io.bluebeaker.mteenoughitems.utils.BlockDropChecker;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import io.bluebeaker.mteenoughitems.utils.RenderUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BeekeepingFlowersCategory extends GenericRecipeCategory<BeekeepingFlowersCategory.Wrapper> {
    public static final String UID = Categories.Forestry.BEEKEEPING_FLOWERS_UID;
    public static final int MARGIN_X = 4;

    public BeekeepingFlowersCategory(IGuiHelper guiHelper) {
        super(guiHelper, 170, 120);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.YELLOW_FLOWER));
    }

    @Override
    public String getModName() {
        return ModChecker.Forestry.name;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        BlockTooltipCallbacks callbacks = new BlockTooltipCallbacks();

        for (int i=0;i<wrapper.items.size();i++) {
            this.addItemSlot(guiItemStackGroup,i,MARGIN_X+18*(i%9),20+18*((i-1)/9));
            guiItemStackGroup.set(i,wrapper.items.get(i));

            callbacks.add(i,wrapper.getStates().get(i));
        }

        guiItemStackGroup.addTooltipCallback(new TooltipCallback(wrapper, callbacks.getItemCallback()));
    }

    @Override
    public String getTranslationKey() {
        return Categories.Forestry.BEEKEEPING_FLOWERS;
    }

    @Override
    public String getUid() {
        return UID;
    }

    public static List<Wrapper> getRecipes(IJeiHelpers jeiHelpers){
        List<Wrapper> recipes = new ArrayList<>();
        IFlowerRegistry flowerRegistry = FlowerManager.flowerRegistry;
        if(!(flowerRegistry instanceof FlowerRegistry)) return recipes;

        FlowerRegistry flowerRegistry1 = (FlowerRegistry) flowerRegistry;
        // Access registered flowers
        HashMultimap<String, Block> blocks = FlowerAccessor.acceptableBlocks.get(flowerRegistry1);
        Map<String, BlockStateSet> blockstates = FlowerAccessor.acceptableBlockStates.get(flowerRegistry1);
        HashMultimap<String, Flower> plantableFlowers = FlowerAccessor.plantableFlowers.get(flowerRegistry1);
        if(blocks==null || blockstates==null || plantableFlowers==null){
            MTEEnoughItems.getLogger().warn("Failed to access FlowerRegistry, not loading flowers");
            return recipes;
        }

        // Get all flower types
        Map<String, FlowerProvider> flowerTypes = new HashMap<>();
        for (EnumAllele.Flowers value : EnumAllele.Flowers.values()) {
            flowerTypes.put(value.getValue().getFlowerType(),value.getValue());
        }

        // Add dummy flower type for missing flower types (if added by other mods)
        blocks.keySet().forEach((s)->{if(!flowerTypes.containsKey(s)) flowerTypes.put(s,new FlowerProvider(s,s));});
        blockstates.keySet().forEach((s)->{if(!flowerTypes.containsKey(s)) flowerTypes.put(s,new FlowerProvider(s,s));});
        plantableFlowers.keySet().forEach((s)->{if(!flowerTypes.containsKey(s)) flowerTypes.put(s,new FlowerProvider(s,s));});

        // Get blocks for every flower type
        for (String flowerType : flowerTypes.keySet()) {
            Set<IBlockState> states = new HashSet<>();

            BlockStateSet iBlockStates = blockstates.get(flowerType);
            if(iBlockStates!=null)
                states.addAll(iBlockStates);

            for (Block block : blocks.get(flowerType)) {
                states.add(block.getDefaultState());
            }
            List<IBlockState> states1 = new ArrayList<>(states);
            states1.sort(Comparator.comparing(Object::toString));

            boolean[] plantable = new boolean[states.size()];
            for (int i = 0; i < states1.size(); i++) {
                IBlockState state = states1.get(i);
                for (Flower flower : plantableFlowers.get(flowerType)) {
                    if(flower.getBlockState()==state){
                        plantable[i]=true;
                        break;
                    }
                }
            }

            recipes.add(new Wrapper(flowerTypes.get(flowerType),states1,plantable));
        }

        return recipes;
    }

    public static class TooltipCallback implements ITooltipCallback<ItemStack> {
        private final boolean[] plantable;
        private final ITooltipCallback<ItemStack> parent;
        public TooltipCallback(Wrapper wrapper,ITooltipCallback<ItemStack> parent) {
            plantable=wrapper.getPlantable().clone();
            this.parent=parent;
        }

        @Override
        public void onTooltip(int i, boolean b, ItemStack stack, List<String> list) {
            this.parent.onTooltip(i, b, stack, list);

            if(i>=plantable.length) return;
            if(plantable[i]){
                list.add(I18n.format("category.mteenoughitems.forestry.beekeeping_flowers.grow"));
            }
        }
    }

    public static class Wrapper implements IRecipeWrapper {
        public final FlowerProvider provider;
        public final int size;
        private final List<IBlockState> states;
        private final List<ItemStack> items;
        private final boolean[] plantable;

        public Wrapper(FlowerProvider provider, List<IBlockState> states, boolean[] plantable) {
            super();
            this.provider=provider;
            this.states = Collections.unmodifiableList(states);
            this.size=states.size();

            this.plantable = plantable;
            ArrayList<ItemStack> items = new ArrayList<>(size);
            for (int i = 0; i < states.size(); i++) {
                items.add(BlockDropChecker.getDrop(states.get(i)));
            }

            this.items=Collections.unmodifiableList(items);
        }

        @Override
        public void getIngredients(IIngredients iIngredients) {
            iIngredients.setInputs(VanillaTypes.ITEM,items);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int xPos = 16;
            int yPos = 4;
            RenderUtils.drawTextAlignedMiddle(provider.getDescription(),recipeWidth/2,yPos, Color.gray.getRGB());
        }

        public List<IBlockState> getStates() {
            return states;
        }

        public boolean[] getPlantable() {
            return plantable;
        }
    }
}
