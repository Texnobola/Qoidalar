package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.init.QoidalarModItems;

@Mod.EventBusSubscriber
public class OtamMuloqotProcedureProcedure { // Klass nomi fayl nomiga moslandi
    @SubscribeEvent
    public static void onEntityLeftClick(AttackEntityEvent event) {
        if (event.getTarget() instanceof RaisEntity _otam) {
            Player _player = event.getEntity();
            ItemStack _mainHandItem = _player.getMainHandItem();

            if (_mainHandItem.getItem() == QoidalarModItems.LITSENZIYA.get()) {
                if (!_player.level().isClientSide()) {
                    _player.displayClientMessage(Component.literal("§6[Otam]: §fBarakalla, bolam! Hujjatlaring joyida ekan."), false);
                    
                    if (_player.level() instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, _otam.getX(), _otam.getY() + 1, _otam.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    }

                    _otam.discard(); 
                    event.setCanceled(true);
                }
            }
        }
    }
}