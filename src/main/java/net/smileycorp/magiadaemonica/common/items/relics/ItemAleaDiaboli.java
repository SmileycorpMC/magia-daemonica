package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.client.DaemonicaItemProperties;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.DaemonicaSoundEvents;
import net.smileycorp.magiadaemonica.common.EnumParticle;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.network.ChooseCurseBoonMessage;
import net.smileycorp.magiadaemonica.common.network.ChooseRelicMessage;
import net.smileycorp.magiadaemonica.config.ItemsConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ItemAleaDiaboli extends ItemRelic {

    public ItemAleaDiaboli() {
        super("alea_diaboli");
        addPropertyOverride(Constants.loc("number"), DaemonicaItemProperties::aleaDiabolaNumber);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        Random rand = player.getRNG();
        setNumber(getRoll(player), stack);
        world.playSound(null, player.posX, player.posY, player.posZ, DaemonicaSoundEvents.ALEA_DIABOLI_ROLL, SoundCategory.PLAYERS, 1.5f, 1.1f - (rand.nextFloat() * 0.2f));
        player.getCooldownTracker().setCooldown(this, ItemsConfig.aleaDiaboliCooldown);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote) return;
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null |! (entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        if (!nbt.hasKey("roll_ticks")) return;
        if (!nbt.hasKey("number")) nbt.setByte("number", (byte) getRoll(player));
        byte rollTicks = nbt.getByte("roll_ticks");
        if (rollTicks <= 0) {
            nbt.removeTag("roll_ticks");
            applyRoll(stack, nbt.getByte("number"), player);
            return;
        }
        nbt.setByte("roll_ticks", (byte) (rollTicks - 1));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
        return !(stack.hasTagCompound() && stack.getTagCompound().hasKey("roll_ticks"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.alea_diaboli.tooltip.disclaimer").getFormattedText());
    }

    public static int getRoll(EntityPlayer player) {
        Random rand = player.getRNG();
        int mode = ItemsConfig.aleaDiaboliLuckMode;
        if (mode == 0) return rand.nextInt(20) + 1;
        double luck = player.getLuck();
        boolean advantage = luck > 0;
        luck = Math.abs(luck);
        int rolls = (int) luck;
        if (mode % 2 == 0 && rand.nextFloat() <= luck - rolls) rolls++;
        if (mode < 3) return rand.nextInt(20) + 1 + (advantage ? rolls : -rolls);
        int roll = -1;
        for (int i = 0; i <= rolls; i++) {
            int r = rand.nextInt(20) + 1;
            if (i == 0 || (advantage ? r > roll : r < roll)) roll = r;
        }
        return roll;
    }

    public static void setNumber(int number, ItemStack stack) {
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        nbt.setByte("number", (byte) MathHelper.clamp(number, 1, 20));
        nbt.setByte("roll_ticks", (byte) 40);
        stack.setTagCompound(nbt);
    }

    public static void applyRoll(ItemStack stack, int number, EntityPlayer player) {
        Random rand = player.getRNG();
        WorldServer world = (WorldServer) player.world;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        switch (number) {
            case 1:
                world.newExplosion(null, x, y, z, 3, true, true);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                stack.shrink(1);
                return;
            case 2:
                player.motionY = 2.5;
                player.velocityChanged = true;
                EntityFireworkRocket firework = new EntityFireworkRocket(world, new ItemStack(Items.FIREWORKS), player);
                firework.setInvisible(true);
                world.spawnEntity(firework);
                for (int i = 0; i < 8; i++) world.spawnParticle(EnumParticleTypes.CLOUD,
                        x + rand.nextFloat() - rand.nextFloat(), y + 0.5f, z + rand.nextFloat() - rand.nextFloat(), 0, 0d, 0d, 0d, 0);
                return;
            case 3:
                world.spawnEntity(new EntityLightningBolt(world, x, y, z, false));
                return;
            case 4:
                player.inventory.dropAllItems();
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.75f, 1f);
                return;
            case 5:
                for (int i = 0; i < 12; i ++) {
                    EntitySilverfish entity = new EntitySilverfish(world);
                    float angle = MathHelper.wrapDegrees(rand.nextFloat() * 360f);
                    Vec3d vec = DirectionUtils.getDirectionVecXZDegrees(angle);
                    entity.setPositionAndRotation(x + vec.x, y + player.height * 0.5f, z + vec.z, 360 - angle, 0);
                    entity.motionX = vec.x * 0.6;
                    entity.motionY = 0.6f;
                    entity.motionZ = vec.z * 0.6;
                    entity.velocityChanged = true;
                    world.spawnEntity(entity);
                }
                for (int i = 0; i < 18; i++) EnumParticle.PIXEL.send(player.dimension, x + rand.nextFloat() - rand.nextFloat(),
                        y + player.height * rand.nextFloat(), z + rand.nextFloat() - rand.nextFloat(), (double) 0xFF818181, 20d, 0d, 0.1, 0d, 1d);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 1f, 1f);
                return;
            case 6:
                ChooseCurseBoonMessage.send((EntityPlayerMP) player, true, CursesRegistry.getRandomCurses(player, 3));
                return;
            case 7:
                Vec3d vec = DirectionUtils.getDirectionVecXZDegrees(MathHelper.wrapDegrees(rand.nextFloat() * 360f));
                float distance = (1 + rand.nextFloat() * 2f) * 500f;
                BlockPos pos = new BlockPos(player.posX + vec.x * distance, 0, player.posZ + vec.z * distance);
                world.getChunkFromBlockCoords(pos);
                player.dismountRidingEntity();
                player.setPositionAndUpdate(pos.getX() + 0.5f, world.getHeight(pos.getX(), pos.getZ()), pos.getZ() + 0.5f);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
                for (int i = 0; i < 8; i++) {
                    world.spawnParticle(EnumParticleTypes.DRAGON_BREATH,
                            x + rand.nextFloat() - rand.nextFloat(), y + rand.nextFloat() * player.height, z + rand.nextFloat() - rand.nextFloat(), 0, 0d, 0.1, 0d, 0);
                    world.spawnParticle(EnumParticleTypes.DRAGON_BREATH,
                            player.posX + rand.nextFloat() - rand.nextFloat(), player.posY + rand.nextFloat() * player.height, player.posZ + rand.nextFloat() - rand.nextFloat(), 0, 0d, 1, 0d, 0.1);
                }
                return;
            case 8:
                List<Potion> potions = ForgeRegistries.POTIONS.getValuesCollection().stream()
                        .filter(potion -> potion.isBadEffect() &! potion.isInstant()).collect(Collectors.toList());
                player.addPotionEffect(new PotionEffect(potions.get(rand.nextInt(potions.size())), 3600));
                world.playSound(null, x, y, z, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                return;
            case 9:
                world.setBlockState(new BlockPos(x, y, z), Blocks.WEB.getDefaultState());
                world.playSound(null, x, y, z, SoundEvents.ENTITY_SPIDER_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!player.isAirBorne) player.setPosition(x, y + 0.3f, z);
                return;
            case 10:
                EntityBoat boat = new EntityBoat(world);
                boat.setPosition(x, y, z);
                player.startRiding(boat, true);
                world.spawnEntity(boat);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1f, 1f);
                return;
            case 11:
                world.playSound(null, x, y, z, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.PLAYERS, 1f, 1f);;
                EntityItem item = player.dropItem(new ItemStack(Blocks.DIRT, 64), false);
                if (item == null) return;
                item.setNoPickupDelay();
                item.setOwner(player.getName());
                return;
            case 12:
                List<Potion> potions1 = ForgeRegistries.POTIONS.getValuesCollection().stream()
                        .filter(potion -> !potion.isBadEffect() &! potion.isInstant()).collect(Collectors.toList());
                player.addPotionEffect(new PotionEffect(potions1.get(rand.nextInt(potions1.size())), 3600));
                world.playSound(null, x, y, z, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                return;
            case 13:
                player.setHealth(player.getMaxHealth());
                player.clearActivePotions();
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 4));
                player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 200, 1));
                player.world.setEntityState(player, (byte)35);
                player.getFoodStats().setFoodLevel(20);
                player.getCooldownTracker().setCooldown(stack.getItem(), 0);
                return;
            case 14:
                player.addExperienceLevel(10);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
                return;
            case 15:
                world.playSound(null, x, y, z, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1f, 1f);
                for (ItemStack stack1 : player.inventory.mainInventory) {
                    if (!stack1.isItemStackDamageable() |! stack1.isItemDamaged()) continue;
                    stack1.setItemDamage(0);
                }
                for (ItemStack stack1 : player.inventory.armorInventory) {
                    if (!stack1.isItemStackDamageable() |! stack1.isItemDamaged()) continue;
                    stack1.setItemDamage(0);
                }
                for (ItemStack stack1 : player.inventory.offHandInventory) {
                    if (!stack1.isItemStackDamageable() |! stack1.isItemDamaged()) continue;
                    stack1.setItemDamage(0);
                }
                return;
            case 16:
                world.playSound(null, x, y, z, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1f, 1f);
                ItemStack stack1 = EnchantmentHelper.addRandomEnchantment(rand, new ItemStack(Items.BOOK), 30, true);
                EntityItem item1 = player.dropItem(stack1, false);
                if (item1 == null) return;
                item1.setNoPickupDelay();
                item1.setOwner(player.getName());
                return;
            case 17:
                world.playSound(null, x, y, z, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1f, 1f);;
                EntityItem item2 = player.dropItem(new ItemStack(DaemonicaItems.MATERIAL, 1, 666), false);
                if (item2 == null) return;
                item2.setNoPickupDelay();
                item2.setOwner(player.getName());
                return;
            case 18:
                ItemStack stack2 = new ItemStack(Items.PAPER);
                stack2.setStackDisplayName("§6IOU");
                NBTTagList tooltips = new NBTTagList();
                tooltips.appendTag(new NBTTagString("§4§oInfernal Relic - Placeholder"));
                tooltips.appendTag(new NBTTagString("this is supposed to be a grimoire entry or invocation or something"));
                tooltips.appendTag(new NBTTagString("I don't know fully how that system is going to work yet and it's NYI"));
                tooltips.appendTag(new NBTTagString("hit me with a rock or a brick or something, whatever your blunt object of choice is when it gets implemented"));
                stack2.getOrCreateSubCompound("display").setTag("Lore", tooltips);
                EntityItem item3 = player.dropItem(stack2, false);
                if (item3 == null) return;
                item3.setNoPickupDelay();
                item3.setOwner(player.getName());
                return;
            case 19:
                ChooseCurseBoonMessage.send((EntityPlayerMP) player, false, BoonRegistry.getRandomBoons(player, 3));
                return;
            case 20:
                ChooseRelicMessage.send((EntityPlayerMP) player, ContractsUtils.getRelics().size());
                stack.shrink(1);
        }
    }

}
