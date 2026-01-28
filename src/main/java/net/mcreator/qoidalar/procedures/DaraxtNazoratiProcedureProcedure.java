package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.mcreator.qoidalar.init.QoidalarModItems;
import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class DaraxtNazoratiProcedureProcedure {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Player _player = event.getPlayer();

        if (world.getBlockState(pos).is(BlockTags.LOGS)) {
            // 1. Birinchi navbatda Litsenziya tekshiruvi (Tezkor jazo)
            boolean hasLicense = false;
            for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                if (_player.getInventory().getItem(i).getItem() == QoidalarModItems.LITSENZIYA.get()) {
                    hasLicense = true;
                    break;
                }
            }

            if (!hasLicense) {
                shoshilinchJazo(world, pos, _player, "Litsenziyasiz daraxt kessang, jarima to'laysan!");
            } else {
                // 2. Agar litsenziya bo'lsa, ko'chat ekishni 15 soniya kutamiz
                QoidalarMod.queueServerWork(300, () -> {
                    boolean saplingFound = false;
                    for (int sx = -3; sx <= 3; sx++) {
                        for (int sy = -2; sy <= 2; sy++) {
                            for (int sz = -3; sz <= 3; sz++) {
                                if (world.getBlockState(pos.offset(sx, sy, sz)).is(BlockTags.SAPLINGS)) {
                                    saplingFound = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!saplingFound && !world.isClientSide()) {
                        shoshilinchJazo(world, pos, _player, "Litsenziyang boru, lekin ko'chat ekmading! Piyoda yurasan!");
                        // Sprintni cheklash (Jadvalingizdagi jazo)
                        _player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 6000, 2));
                        _player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1));
                    }
                });
            }
        }
    }

    private static void shoshilinchJazo(LevelAccessor world, BlockPos pos, Player _player, String message) {
        if (world instanceof ServerLevel _level) {
            _level.getServer().getCommands().performPrefixedCommand(_player.createCommandSourceStack(), "title @p title {\"text\":\"JINOYAT!\",\"color\":\"dark_red\"}");
            _level.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.elder_guardian.curse")), SoundSource.NEUTRAL, 1.0F, 0.8F);
            
            RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.west(), MobSpawnType.MOB_SUMMONED);
            if (_otam != null) {
                _player.displayClientMessage(Component.literal("§6[Otam]: §f" + message), false);
                _otam.setTarget(_player);
                QoidalarMod.queueServerWork(200, () -> { if (_otam.isAlive()) _otam.discard(); });
            }
        }
    }
}