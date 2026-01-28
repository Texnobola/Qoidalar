package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class MerosniMuhofazaQilishProcedureProcedureProcedure { // KLASS NOMI TO'G'RILANDI
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Player _player = event.getPlayer();
        String blockName = world.getBlockState(pos).getBlock().getDescriptionId();
        
        if (blockName.contains("chiseled_sandstone") || blockName.contains("blue_terracotta") || blockName.contains("chiseled_stone_bricks")) {
            if (!world.isClientSide() && world instanceof ServerLevel _level) {
                RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.above(), MobSpawnType.MOB_SUMMONED);
                if (_otam != null) {
                    _player.displayClientMessage(Component.literal("§6[Otam]: §fKonstitutsiyaning 49-moddasi: Madaniy merosni asrash har bir fuqaroning burchidir!"), false);
                    _player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    _player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    _player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                    _player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                    _player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2400, 1));
                    QoidalarMod.queueServerWork(100, () -> { if (_otam.isAlive()) _otam.discard(); });
                }
            }
        }
    }
}