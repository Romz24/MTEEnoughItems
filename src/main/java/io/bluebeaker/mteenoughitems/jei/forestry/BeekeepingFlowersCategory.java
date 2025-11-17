package io.bluebeaker.mteenoughitems.jei.forestry;

import com.google.common.collect.HashMultimap;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.FlowerManager;
import forestry.api.apiculture.IBee;
import forestry.api.genetics.IFlowerRegistry;
import forestry.apiculture.ModuleApiculture;
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
import net.minecraft.nbt.NBTTagCompound;

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
        this.addItemSlot(guiItemStackGroup,45,4,4);
        guiItemStackGroup.set(45,wrapper.bees);

        for (int i=0;i<wrapper.items.size();i++) {
            this.addItemSlot(guiItemStackGroup,i,MARGIN_X+18*(i%9),26+18*(i/9));
            guiItemStackGroup.set(i,wrapper.items.get(i));

            callbacks.add(i,wrapper.getFlowerDefs().get(i).blockState);
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

        // Preprocess bees
        Map<String,List<ItemStack>> flowersToBees = new HashMap<>();
        addFlowersToBees(flowersToBees);


        // Get blocks for every flower type
        for (String flowerType : flowerTypes.keySet()) {
            Set<IBlockState> states = new HashSet<>();
            // Add defined blockstates
            BlockStateSet iBlockStates = blockstates.get(flowerType);
            if(iBlockStates!=null)
                states.addAll(iBlockStates);
            // Add default state for defined blocks
            for (Block block : blocks.get(flowerType)) {
                states.add(block.getDefaultState());
            }
            // Check plantable
            Set<IBlockState> plantable = new HashSet<>();
            for (Flower flower : plantableFlowers.get(flowerType)) {
                plantable.add(flower.getBlockState());
            }

            List<FlowerDef> flowerDefs = new ArrayList<>();
            for (IBlockState state : states) {
                flowerDefs.add(new FlowerDef(state, plantable.contains(state)));
            }
            flowerDefs.sort(Comparator.comparing((f)->f.blockState.toString()));

            for (int i = 0; i < flowerDefs.size(); i=i+45) {
                List<FlowerDef> subList = flowerDefs.subList(i,Math.min(i+45,flowerDefs.size()));
                recipes.add(new Wrapper(flowerTypes.get(flowerType),subList, flowersToBees.get(flowerType)));
            }
        }

        return recipes;
    }

    private static void addFlowersToBees(Map<String, List<ItemStack>> flowersToBees) {
        if (BeeManager.beeRoot == null) return;
        List<IBee> individualTemplates = BeeManager.beeRoot.getIndividualTemplates();

        for (IBee individual : individualTemplates) {
            individual.analyze();
            String flowerType = individual.getGenome().getFlowerProvider().getFlowerType();
            flowersToBees.computeIfAbsent(flowerType,(type)->new ArrayList<>());

            NBTTagCompound nbt = new NBTTagCompound();
            individual.writeToNBT(nbt);

            ItemStack princess = new ItemStack(ModuleApiculture.getItems().beePrincessGE);
            ItemStack drone = new ItemStack(ModuleApiculture.getItems().beeDroneGE);
            princess.setTagCompound(nbt);
            drone.setTagCompound(nbt);

            individual.mate(individual);
            ItemStack queen = new ItemStack(ModuleApiculture.getItems().beeQueenGE);
            nbt = new NBTTagCompound();
            individual.writeToNBT(nbt);
            queen.setTagCompound(nbt);

            flowersToBees.get(flowerType).add(queen);
            flowersToBees.get(flowerType).add(princess);
            flowersToBees.get(flowerType).add(drone);
        }
    }

    public static class FlowerDef{
        public final boolean plantable;
        public final IBlockState blockState;
        public FlowerDef(IBlockState blockState,boolean plantable) {
            this.plantable = plantable;
            this.blockState = blockState;
        }
    }

    public static class TooltipCallback implements ITooltipCallback<ItemStack> {
        private final Wrapper wrapper;
        private final ITooltipCallback<ItemStack> parent;
        public TooltipCallback(Wrapper wrapper,ITooltipCallback<ItemStack> parent) {
            this.wrapper=wrapper;
            this.parent=parent;
        }

        @Override
        public void onTooltip(int i, boolean b, ItemStack stack, List<String> list) {
            this.parent.onTooltip(i, b, stack, list);

            if(i>=wrapper.size) return;
            if(wrapper.getFlowerDefs().get(i).plantable){
                list.add(I18n.format("category.mteenoughitems.forestry.beekeeping_flowers.grow"));
            }
        }
    }

    public static class Wrapper implements IRecipeWrapper {
        public final FlowerProvider provider;
        public final int size;
        public final List<FlowerDef> flowerDefs;
        public final List<ItemStack> items;
        public final List<ItemStack> bees;


        public Wrapper(FlowerProvider provider, List<FlowerDef> flowerDefs, List<ItemStack> bees) {
            super();
            this.provider=provider;
            this.flowerDefs = Collections.unmodifiableList(flowerDefs);
            this.size=flowerDefs.size();
            this.bees = bees;

            ArrayList<ItemStack> items = new ArrayList<>(size);
            for (FlowerDef flowerDef : flowerDefs) {
                items.add(BlockDropChecker.getDrop(flowerDef.blockState));
            }

            this.items=Collections.unmodifiableList(items);
        }

        @Override
        public void getIngredients(IIngredients iIngredients) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(bees);
            for (ItemStack item : items) {
                inputs.add(Collections.singletonList(item));
            }
            iIngredients.setInputLists(VanillaTypes.ITEM,inputs);
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if(flowerDefs.get(i).plantable){
                    outputs.add(items.get(i));
                }
            }
            iIngredients.setOutputs(VanillaTypes.ITEM,outputs);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int xPos = 16;
            int yPos = 4;
            RenderUtils.drawTextAlignedMiddle(provider.getDescription(),recipeWidth/2,yPos, Color.gray.getRGB());
        }

        public List<FlowerDef> getFlowerDefs() {
            return flowerDefs;
        }
    }
}
