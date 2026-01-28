package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class VillagerHimoyasiProcedureProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        if (event.getEntity() instanceof Villager || event.getEntity() instanceof IronGolem) {
            if (event.getSource().getEntity() instanceof Player _player) {
                LevelAccessor world = _player.level();
                BlockPos pos = BlockPos.containing(_player.getX(), _player.getY(), _player.getZ());

                if (!world.isClientSide() && world instanceof ServerLevel _level) {
                    RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.offset(3, 0, 0), MobSpawnType.MOB_SUMMONED);
                    
                    if (_otam != null) {
                        _player.displayClientMessage(Component.literal("§6[Otam]: §fSiz qonunni buzdingiz! 30 soniya zindonda o'tirasiz!"), false);
                        _level.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.elder_guardian.curse")), SoundSource.NEUTRAL, 1.0F, 0.8F);

                        // Qamoqxona qurish (5x4x5)
                        for (int sx = -2; sx <= 2; sx++) {
                            for (int sy = -1; sy <= 3; sy++) {
                                for (int sz = -2; sz <= 2; sz++) {
                                    BlockPos bp = pos.offset(sx, sy, sz);
                                    
                                    // Pol va Shift (Obsidian)
                                    if (sy == -1 || sy == 3) {
                                        world.setBlock(bp, Blocks.OBSIDIAN.defaultBlockState(), 3);
                                    } 
                                    // Devorlar
                                    else if (Math.abs(sx) == 2 || Math.abs(sz) == 2) {
                                        // Burchaklarda va o'rtada panjaralar bo'lsin
                                        if ((sx + sz) % 2 == 0) {
                                            world.setBlock(bp, Blocks.IRON_BARS.defaultBlockState(), 3);
                                        } else {
                                            world.setBlock(bp, Blocks.OBSIDIAN.defaultBlockState(), 3);
                                        }
                                    } 
                                    // Ichki bo'shliq
                                    else {
                                        world.setBlock(bp, Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
                            }
                        }

                        _player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3));

                        // 30 soniyadan keyin tozalash
                        QoidalarMod.queueServerWork(600, () -> {
                            for (int sx = -2; sx <= 2; sx++) {
                                for (int sy = -1; sy <= 3; sy++) {
                                    for (int sz = -2; sz <= 2; sz++) {
                                        BlockPos bp = pos.offset(sx, sy, sz);
                                        if (world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN || world.getBlockState(bp).getBlock() == Blocks.IRON_BARS) {
                                            world.setBlock(bp, Blocks.AIR.defaultBlockState(), 3);
                                        }
                                    }
                                }
                            }
                            _player.displayClientMessage(Component.literal("§a[Otam]: §fTavba qildingiz degan umiddaman. Ozodsiz!"), false);
                            if (_otam.isAlive()) _otam.discard();
                        });
                    }
                }
            }
        }
    }
}