package io.bluebeaker.mteenoughitems.jei.railcraft;

import io.bluebeaker.mteenoughitems.Categories;
import io.bluebeaker.mteenoughitems.jei.generic.FluidHeatConversionCategory;
import io.bluebeaker.mteenoughitems.jei.generic.FluidHeatConversionRecipe;
import io.bluebeaker.mteenoughitems.utils.ModChecker;
import io.bluebeaker.mteenoughitems.utils.RenderUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.fluids.Fluids;
import mods.railcraft.common.util.steam.SteamConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoilerCategory extends FluidHeatConversionCategory<BoilerCategory.BoilerRecipe> {
    protected final IDrawableStatic bgFire;
    protected final IDrawableAnimated fire;

    public static final String UID = Categories.Railcraft.BOILER_UID;
    public static final ResourceLocation GUI_PATH = new ResourceLocation("railcraft","textures/gui/gui_boiler_liquid.png");

    public BoilerCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.bgFire = guiHelper.createDrawable(GUI_PATH, 62, 38, 14, 14);
        this.fire = guiHelper.drawableBuilder(GUI_PATH, 176, 47, 14, 14).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);

        ItemBlock item = RailcraftBlocks.BOILER_TANK_PRESSURE_LOW.item();
        this.icon = item==null?null:guiHelper.createDrawableIngredient(new ItemStack(item));
    }

    @Override
    public String getModName() {
        return ModChecker.Railcraft.name;
    }

    @Override
    public String getTranslationKey() {
        return Categories.Railcraft.BOILER;
    }

    @Override
    public String getUid() {
        return UID;
    }

    public static List<BoilerRecipe> getRecipes(IJeiHelpers jeiHelpers){
        List<BoilerRecipe> recipes = new ArrayList<>();

        Fluids.STEAM.get().ifPresent(steam ->
                recipes.add(new BoilerRecipe(
                        jeiHelpers,
                        new FluidStack(FluidRegistry.WATER, 1),
                        new FluidStack(steam, SteamConstants.STEAM_PER_UNIT_WATER)
                ))
        );

        return recipes;
    }

    public static class BoilerRecipe extends FluidHeatConversionRecipe {
        public BoilerRecipe(IJeiHelpers jeiHelpers, FluidStack input, FluidStack output) {
            super(jeiHelpers, input, output, 1);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int xPos = recipeWidth/2;
            int yPos = recipeHeight/2 - minecraft.fontRenderer.FONT_HEIGHT;
            RenderUtils.drawTextAlignedMiddle(">=100°C", xPos, yPos, Color.gray.getRGB());
        }

        @Override
        public String getPowerUnit() {
            return "";
        }
    }
}
