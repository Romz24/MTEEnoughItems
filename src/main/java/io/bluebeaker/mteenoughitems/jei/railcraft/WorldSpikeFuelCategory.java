package io.bluebeaker.mteenoughitems.jei.railcraft;

import io.bluebeaker.mteenoughitems.Categories;
import io.bluebeaker.mteenoughitems.jei.generic.FuelRecipeWrapper;
import io.bluebeaker.mteenoughitems.jei.generic.GenericRecipeCategory;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import io.bluebeaker.mteenoughitems.utils.RenderUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.machine.worldspike.WorldspikeVariant;
import mods.railcraft.common.core.RailcraftConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WorldSpikeFuelCategory extends GenericRecipeCategory<WorldSpikeFuelCategory.Wrapper> {
    public static final String UID = Categories.Railcraft.WORLDSPIKE_FUEL_UID;

    public WorldSpikeFuelCategory(IGuiHelper guiHelper) {
        super(guiHelper,116,32);

        ItemBlock item = RailcraftBlocks.WORLDSPIKE.item();
        this.icon = item==null?null:guiHelper.createDrawableIngredient(new ItemStack(item,1,1));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        this.addItemSlot(guiItemStackGroup,0,8,GUI_HEIGHT/2-9);
        this.addItemSlot(guiItemStackGroup,1,GUI_WIDTH-26,GUI_HEIGHT/2-9);
        guiItemStackGroup.set(iIngredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
    }

    @Override
    public String getTranslationKey() {
        return Categories.Railcraft.WORLDSPIKE_FUEL;
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getModName() {
        return ModChecker.Railcraft.name;
    }

    public static List<Wrapper> getRecipes(IJeiHelpers jeiHelpers){
        List<Wrapper> recipes = new ArrayList<>();
        for (int i = 1; i < WorldspikeVariant.values().length; i++) {
            Map<Ingredient, Float> fuelList = WorldspikeVariant.fromId(i).getFuelList();
            for (Ingredient ingredient : fuelList.keySet()) {
                recipes.add(new Wrapper(ingredient,fuelList.get(ingredient),i));
            }
        }
        return recipes;
    }

    public static class Wrapper extends FuelRecipeWrapper {
        float duration;
        final Ingredient ingredient;
        final int variant;

        public Wrapper(Ingredient input, float duration, int variant) {
            super(ItemStack.EMPTY, 0, (int) duration);
            this.duration=duration;
            this.ingredient=input;
            this.variant=variant;
        }

        @Override
        public void getIngredients(IIngredients iIngredients) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
            ItemBlock worldspikeItem = RailcraftBlocks.WORLDSPIKE.item();
            if(worldspikeItem!=null)
                inputs.add(Collections.singletonList(new ItemStack(worldspikeItem,1,this.variant)));
            iIngredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int xPos = recipeWidth / 2;
            int yPos = recipeHeight / 2 - minecraft.fontRenderer.FONT_HEIGHT;

            RenderUtils.drawTextAlignedMiddle(String.format("%.2fh",this.duration), xPos, yPos, Color.gray.getRGB());

            yPos=yPos+minecraft.fontRenderer.FONT_HEIGHT+2;

            RenderUtils.drawTextAlignedMiddle(WorldspikeVariant.VALUES[this.variant].getBaseTag(), xPos, yPos, Color.gray.getRGB());
        }
    }
}
