package net.smileycorp.magiadaemonica.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ChunkCache;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.recipes.PlantHybridizationRegistry;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class PlantHybridizationCategory implements IRecipeCategory<PlantHybridizationCategory.Wrapper> {

    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/jei/hybridization.png");
    public static final String ID = Constants.locStr("plant_hybridizations");

    private static final int ARROW_X = 39;
    private static final int ARROW_Y = 4;

    private final IDrawable background;
    protected IDrawableAnimated progress;

    public PlantHybridizationCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 85, 38);
        IDrawableStatic progressDrawable = guiHelper.createDrawable(TEXTURE, 0, 38, 24, 16);
        progress = guiHelper.createAnimatedDrawable(progressDrawable, 300, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public String getUid() {
        return ID;
    }

    @Override
    public String getTitle() {
        return new TextComponentTranslation("jei.category.magiadaemonica.plant_hybridizations").getFormattedText();
    }

    @Override
    public String getModName() {
        return Constants.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        if (progress != null) progress.draw(minecraft, ARROW_X, ARROW_Y);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, true, 1, 5);
        items.init(1, true, 19, 5);
        items.init(2, false, 66, 5);
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        items.set(0, inputs.get(0));
        items.set(1, inputs.get(1));
        items.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    public static class Wrapper implements IRecipeWrapper {

        private final ItemStack parent1;
        private final ItemStack parent2;
        private final ItemStack hybrid;
        private final float chance;

        public Wrapper(PlantHybridizationRegistry.HybridizationRecipe recipe) {
            parent1 = getStack(recipe.getParent1());
            parent2 = getStack(recipe.getParent2());
            hybrid = getStack(recipe.getHybrid());
            chance = recipe.getChance() * 100f;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(parent1, parent2));
            ingredients.setOutput(VanillaTypes.ITEM, hybrid);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            String text = String.format("%.2f", chance) + "%";
            minecraft.fontRenderer.drawString(text, 52 - minecraft.fontRenderer.getStringWidth(text) / 2, 26, 0x404040);
        }

        private ItemStack getStack(BlockCrops block) {
            Minecraft mc = Minecraft.getMinecraft();
            IBlockState state = block.withAge(block.getMaxAge());
            NonNullList<ItemStack> list = NonNullList.create();
            block.getDrops(list, mc.world, BlockPos.ORIGIN, state, 0);
            if (!list.isEmpty()) {
                ItemStack stack = list.get(0);
                stack.setCount(1);
                return stack;
            }
            return block.getPickBlock(state, new RayTraceResult(new Vec3d(0, 0, 0), EnumFacing.UP),
                    mc.world, BlockPos.ORIGIN, mc.player);
        }

        private ChunkCache createWorldCache(BlockCrops block, IBlockState state) {
            WorldClient world = Minecraft.getMinecraft().world;
            return new ChunkCache(world, BlockPos.ORIGIN, BlockPos.ORIGIN.add(16, 0, 16), 0) {
                @Override
                public IBlockState getBlockState(BlockPos pos) {
                    return BlockPos.ORIGIN.equals(pos) ? state : Blocks.AIR.getDefaultState();
                }

                @Nullable
                @Override
                public TileEntity getTileEntity(BlockPos pos) {
                    return BlockPos.ORIGIN.equals(pos) ? block.createTileEntity(world, state) : null;
                }
            };
        }

    }

}
