package com.totemswap.totemautoequip;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class TotemAutoEquipClient implements ClientModInitializer {

    public static final String MOD_ID = "totemautoequip";

    public static boolean enabled = true;

    private static final int INVENTORY_SLOT_START = 9;
    private static final int INVENTORY_SLOT_END = 44;
    private static final int OFFHAND_BUTTON = 40;

    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "general")
        );

        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.totemautoequip.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                if (client.player != null) {
                    Component msg = enabled
                            ? Component.literal("§a[TotemAutoEquip] เปิดใช้งานแล้ว")
                            : Component.literal("§c[TotemAutoEquip] ปิดใช้งานแล้ว");
                    client.player.sendSystemMessage(msg);
                }
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!enabled) return;
            if (screen instanceof InventoryScreen) {
                tryEquipTotem(client);
            }
        });
    }

    private static void tryEquipTotem(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || client.gameMode == null) return;

        ItemStack offhandStack = player.getOffhandItem();
        if (offhandStack.is(Items.TOTEM_OF_UNDYING)) {
            return;
        }

        AbstractContainerMenu handler = player.containerMenu;
        if (handler == null) return;

        List<Slot> slots = handler.slots;
        for (int i = 0; i < slots.size(); i++) {
            if (i < INVENTORY_SLOT_START || i > INVENTORY_SLOT_END) {
                continue;
            }
            Slot slot = slots.get(i);
            if (slot.getItem().is(Items.TOTEM_OF_UNDYING)) {
                client.gameMode.handleInventoryMouseClick(
                        handler.containerId,
                        i,
                        OFFHAND_BUTTON,
                        ContainerInput.SWAP,
                        player
                );
                break;
            }
        }
    }
}