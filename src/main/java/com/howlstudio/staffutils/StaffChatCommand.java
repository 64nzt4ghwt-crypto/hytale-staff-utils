package com.howlstudio.staffutils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

public class StaffChatCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public StaffChatCommand(StaffManager manager) {
        super("staffchat", "[Staff] Toggle staff-only chat. Usage: /staffchat [message]");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID uuid = playerRef.getUuid();
        if (uuid == null) return;

        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+", 2);

        if (parts.length >= 2) {
            // Direct staff message without toggling
            String msg = parts[1];
            String name = manager.getPlayerName(uuid);
            broadcastToStaff(manager, "§d[Staff] §f" + name + "§7: §f" + msg);
            return;
        }

        boolean inSC = manager.toggleStaffChat(uuid);
        if (inSC) {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fStaff chat §aenabled§f. Your messages go to staff only."));
        } else {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fStaff chat §cdisabled§f."));
        }
    }

    static void broadcastToStaff(StaffManager manager, String message) {
        for (UUID staffUuid : manager.getStaffChatPlayers()) {
            try {
                var ref = com.hypixel.hytale.server.core.universe.Universe.get().getPlayer(staffUuid);
                if (ref != null) ref.sendMessage(Message.raw(message));
            } catch (Exception ignored) {}
        }
    }
}
