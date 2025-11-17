package io.bluebeaker.mteenoughitems.jei.immersiveengineering;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import io.bluebeaker.mteenoughitems.Categories;
import io.bluebeaker.mteenoughitems.jei.generic.GenericRecipeCategory;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import io.bluebeaker.mteenoughitems.utils.RenderUtils;
import io.bluebeaker.mteenoughitems.utils.StringUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MineralDepositCategory extends GenericRecipeCategory<MineralDepositCategory.Wrapper> {
    public static final String UID = Categories.ImmersiveEngineering.MINERAL_DEPOSIT_UID;
    public static final int MARGIN_X = 4;
    public MineralDepositCategory(IGuiHelper guiHelper) {
        super(guiHelper, 170, 50);

        this.icon = guiHelper.createDrawableIngredient(new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.SAMPLE_DRILL.getMeta()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        for (int i=0;i<wrapper.mineralMix.oreOutput.size();i++) {
            this.addItemSlot(guiItemStackGroup,i,MARGIN_X+18*(i%9),24+18*(i/9));
            guiItemStackGroup.set(i,wrapper.mineralMix.oreOutput.get(i));
        }
        guiItemStackGroup.addTooltipCallback(new TooltipCallback(wrapper));
    }

    @Override
    public String getTranslationKey() {
        return Categories.ImmersiveEngineering.MINERAL_DEPOSIT;
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getModName() {
        return ModChecker.ImmersiveEngineering.name;
    }

    public static List<Wrapper> getRecipes(IJeiHelpers jeiHelpers){
        List<Wrapper> recipes = new ArrayList<>();
        for (Map.Entry<MineralMix, Integer> entry : ExcavatorHandler.mineralList.entrySet()) {
            MineralMix mix = entry.getKey();
            int weight = entry.getValue();
            recipes.add(new Wrapper(mix,weight));
        }

        return recipes;
    }
    public static String getOreName(String name){
        String formatted = I18n.format("desc.immersiveengineering.info.mineral."+name);
        if(formatted.equals("desc.immersiveengineering.info.mineral."+name)) return name;
        return formatted;
    }
    public static class Wrapper implements IRecipeWrapper {
        public final MineralMix mineralMix;
        public final int weight;

        public Wrapper(MineralMix mineralMix, int weight){
            this.mineralMix=mineralMix;
            this.weight=weight;
        }

        @Override
        public void getIngredients(IIngredients iIngredients) {
            iIngredients.setOutputs(VanillaTypes.ITEM,mineralMix.oreOutput);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int y=2;
            RenderUtils.drawTextAlignedLeft(getOreName(mineralMix.name),MARGIN_X,y, Color.gray.getRGB());
            RenderUtils.drawTextAlignedRight(I18n.format("category.mteenoughitems.immersiveengineering.mineral_deposit.weight",weight),recipeWidth-MARGIN_X,y, Color.gray.getRGB());
            y=y+minecraft.fontRenderer.FONT_HEIGHT+1;
            RenderUtils.drawTextAlignedLeft(getDimListStr(),MARGIN_X,y, Color.gray.getRGB());
        }
        public String getDimListStr(){
            String str = "";
            if(mineralMix.dimensionWhitelist.length>0){
                str=I18n.format("category.mteenoughitems.immersiveengineering.mineral_deposit.whitelist", StringUtils.join(Arrays.stream(mineralMix.dimensionWhitelist).boxed(),","));
            }else if (mineralMix.dimensionBlacklist.length>0){
                str=I18n.format("category.mteenoughitems.immersiveengineering.mineral_deposit.blacklist", StringUtils.join(Arrays.stream(mineralMix.dimensionBlacklist).boxed(),","));;
            }
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            if(fontRenderer.getStringWidth(str)>170-MARGIN_X*2){
                str= fontRenderer.trimStringToWidth(str,170-MARGIN_X*2- fontRenderer.getStringWidth("..."))+"...";
            };
            return str;
        }

        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            // Show mineral name
            if(mouseY>=2 && mouseY<=11 && mouseX>=MARGIN_X && mouseX<170-MARGIN_X && mouseX<=MARGIN_X+Minecraft.getMinecraft().fontRenderer.getStringWidth(getOreName(mineralMix.name))) return Collections.singletonList(mineralMix.name);
            // Show dimension list
            if (mouseY >= 12 && mouseY <= 21 && mouseX >= MARGIN_X && mouseX <= 170 - MARGIN_X) {
                int[] dimList;
                if (mineralMix.dimensionWhitelist.length > 0) dimList = mineralMix.dimensionWhitelist;
                else if (mineralMix.dimensionBlacklist.length > 0) dimList = mineralMix.dimensionBlacklist;
                else return Collections.emptyList();

                if (mouseX > MARGIN_X + Minecraft.getMinecraft().fontRenderer.getStringWidth(getDimListStr()))
                    return Collections.emptyList();

                List<String> lines = new ArrayList<>();
                for (int i : dimList) {
                    lines.add(StringUtils.getDimensionName(i));
                }
                return lines;
            }
            return Collections.emptyList();
        }
    }
    public static class TooltipCallback implements ITooltipCallback<ItemStack>{
        public final Wrapper wrapper;

        public TooltipCallback(Wrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onTooltip(int i, boolean b, ItemStack stack, List<String> list) {
            float[] chances = wrapper.mineralMix.recalculatedChances;
            if(i>=chances.length) return;
            list.add(String.format("%.2f%%",chances[i]*100));
            list.add(I18n.format("category.mteenoughitems.immersiveengineering.mineral_deposit.count",String.format("%.1f",chances[i]*ExcavatorHandler.mineralVeinCapacity)));
        }
    }
}
