package net.smileycorp.magiadaemonica.common.recipes;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PlantHybridizationRegistry {

    private static final PlantHybridizationRegistry INSTANCE = new PlantHybridizationRegistry();

    private final List<HybridizationRecipe> RECIPES = Lists.newArrayList();

    public void addRecipe(BlockCrops parent1, BlockCrops parent2, BlockCrops hybrid, float chance) {
        RECIPES.add(new HybridizationRecipe(parent1, parent2, hybrid, chance));
    }

    public HybridizationRecipe getRecipe(IBlockState state1, IBlockState state2) {
        if (!(state1.getBlock() instanceof BlockCrops) |! (state2.getBlock() instanceof BlockCrops)) return null;
        return RECIPES.stream().filter(recipe -> recipe.matches(state1.getBlock(), state2.getBlock())).findAny().orElse(null);
    }

    public Collection<HybridizationRecipe> getRecipes() {
        return Lists.newArrayList(RECIPES);
    }

    public static PlantHybridizationRegistry getInstance() {
        return INSTANCE;
    }

    public static class HybridizationRecipe {

        private final BlockCrops parent1, parent2, hybrid;
        private final float chance;

        private HybridizationRecipe(BlockCrops parent1, BlockCrops parent2, BlockCrops hybrid, float chance) {
            this.parent1 = parent1;
            this.parent2 = parent2;
            this.hybrid = hybrid;
            this.chance = chance;
        }

        public boolean matches(Block block1, Block block2) {
            return (block1 == parent1 && block2 == parent2) || (block2 == parent1 && block1 == parent2);
        }

        public boolean canGrow(Random rand) {
            return rand.nextFloat() <= chance;
        }

        public IBlockState getState(int age) {
            return hybrid.withAge(Math.min(age, hybrid.getMaxAge()));
        }

        public BlockCrops getParent1() {
            return parent1;
        }

        public BlockCrops getParent2() {
            return parent2;
        }

        public BlockCrops getHybrid() {
            return hybrid;
        }

        public float getChance() {
            return chance;
        }

    }

}
