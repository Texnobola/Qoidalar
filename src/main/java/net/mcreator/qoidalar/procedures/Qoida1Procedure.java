package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Qoida1Procedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		// Xatolik mana shu yerda edi: double ni int ga aylantirish (cast) kerak
		BlockPos pos = BlockPos.containing(x, y, z); 

		if ((world.getBlockState(pos)).getBlock() == Blocks.CHEST || (world.getBlockState(pos)).getBlock() == Blocks.TRAPPED_CHEST) {
			if (entity instanceof Player _player && !_player.getAbilities().instabuild) {
				
				if (event != null && event.isCancelable()) {
					event.setCanceled(true);
				}

				if (!world.isClientSide()) {
					_player.displayClientMessage(Component.literal("§cO'g'rilik qilmang! Bu birovning mulki."), true);
					
					if (world instanceof ServerLevel _level) {
						LightningBolt _ent = EntityType.LIGHTNING_BOLT.create(_level);
						_ent.moveTo(x, y, z, 0, 0); // Chaqmoq uchun double koordinatalar ketaveradi
						_level.addFreshEntity(_ent);
					}
				}
			}
		}
	}
}