package net.mcreator.qoidalar.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.util.List;

public class LitsenziyaItem extends Item {
    public LitsenziyaItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        if (!world.isClientSide()) {
            // Ishlatganda chatda tasdiqlash
            entity.displayClientMessage(Component.literal("§a[Hujjat]: §fDaraxt kesish huquqi tasdiqlangan!"), true);
        }
        return ar;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        // Item ustiga sichqonchani olib borganda chiqadigan yozuv
        list.add(Component.literal("Ushbu hujjat daraxtlarni qonuniy").withStyle(ChatFormatting.GRAY));
        list.add(Component.literal("kesish imkonini beradi.").withStyle(ChatFormatting.GRAY));
        list.add(Component.literal("Egasi: §b" + "Davlat Fuqarosi").withStyle(ChatFormatting.GOLD));
    }
}