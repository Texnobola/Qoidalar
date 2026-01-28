package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.qoidalar.init.QoidalarModItems;
import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class IjtimoiySoliqMajburiyatiProcedure {
    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Villager _villager && _villager.getVillagerData().getProfession() == net.minecraft.world.entity.npc.VillagerProfession.NONE) {
            Player _player = event.getEntity();
            
            // 1. Birinchi navbatda o'yinchida LITSENZIYA bor-yo'qligini tekshiramiz
            boolean hasLicense = false;
            ItemStack licenseStack = ItemStack.EMPTY;
            for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                if (_player.getInventory().getItem(i).getItem() == QoidalarModItems.LITSENZIYA.get()) {
                    hasLicense = true;
                    licenseStack = _player.getInventory().getItem(i);
                    break;
                }
            }

            // Agar o'yinchi shunchaki sayyoh bo'lsa (litsenziyasi yo'q bo'lsa), unga tegmaymiz
            if (!hasLicense) return;

            // 2. Agar litsenziyasi bo'lsa va soliq (Zumrad) bermoqchi bo'lsa
            ItemStack itemInHand = _player.getItemInHand(event.getHand());
            if (itemInHand.getItem() == Items.EMERALD && itemInHand.getCount() >= 10) {
                itemInHand.shrink(10);
                _player.displayClientMessage(Component.literal("§a[Davlat]: §fIjtimoiy soliq qabul qilindi. Litsenziyangiz amal qilish muddati uzaytirildi."), true);
                event.setCanceled(true);
                return;
            }

            // 3. Agar litsenziyasi bo'lsa-yu, lekin soliq to'lashdan bosh tortsa
            if (!_player.level().isClientSide() && _player.level() instanceof ServerLevel _level) {
                BlockPos pos = BlockPos.containing(_player.getX(), _player.getY(), _player.getZ());
                RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.north(), MobSpawnType.MOB_SUMMONED);

                if (_otam != null) {
                    _player.displayClientMessage(Component.literal("§6[Otam]: §fLitsenziyangiz bor, lekin ijtimoiy majburiyatni bajarmadingiz!"), false);
                    _player.displayClientMessage(Component.literal("§c[!] Tadbirkorlik litsenziyangiz bekor qilindi!"), false);

                    // JAZO: Litsenziyani yo'q qilish
                    licenseStack.setCount(0);

                    QoidalarMod.queueServerWork(100, () -> {
                        if (_otam.isAlive()) _otam.discard();
                    });
                }
            }
        }
    }
}