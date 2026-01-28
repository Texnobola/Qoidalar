package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityMountEvent;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class MajburiyMehnatProcedureProcedureProcedure { // KLASS NOMI TO'G'RILANDI
    @SubscribeEvent
    public static void onEntityMount(EntityMountEvent event) {
        if (event.getEntityBeingMounted() instanceof Boat || event.getEntityBeingMounted() instanceof AbstractMinecart) {
            if (event.getEntityMounting() instanceof Villager _villager) {
                Player _player = _villager.level().getNearestPlayer(_villager, 10);
                if (_player != null && !_villager.level().isClientSide()) {
                    if (_villager.level() instanceof ServerLevel _level) {
                        BlockPos pos = BlockPos.containing(_player.getX(), _player.getY(), _player.getZ());
                        RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.north(), MobSpawnType.MOB_SUMMONED);
                        if (_otam != null) {
                            _player.displayClientMessage(Component.literal("§6[Otam]: §fKonstitutsiyaning 37-moddasi buzildi. Majburiy mehnat taqiqlanadi!"), false);
                            event.getEntityBeingMounted().discard();
                            for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                                ItemStack itemstack = _player.getInventory().getItem(i);
                                if (itemstack.getItem() == Items.EMERALD) {
                                    itemstack.shrink(5);
                                    _player.displayClientMessage(Component.literal("§c[!] 5 ta zumrad davlat foydasiga jarima sifatida musodara qilindi."), false);
                                    break;
                                }
                            }
                            QoidalarMod.queueServerWork(100, () -> { if (_otam.isAlive()) _otam.discard(); });
                        }
                    }
                }
            }
        }
    }
}