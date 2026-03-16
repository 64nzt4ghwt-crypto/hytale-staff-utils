package com.howlstudio.staffutils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class StaffListCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public StaffListCommand(StaffManager manager) {
        super("stafflist", "View online staff members. Usage: /stafflist");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        Set<UUID> staffOnline = manager.getStaffChatPlayers();
        Set<UUID> vanished = new java.util.HashSet<>();
        // Staff in staffchat = online staff (approximation)

        playerRef.sendMessage(Message.raw("§6§l--- Staff Online ---"));
        if (staffOnline.isEmpty()) {
            playerRef.sendMessage(Message.raw("§7No staff currently in staff chat. Use §e/staffchat §7to toggle."));
        } else {
            for (UUID staffUuid : staffOnline) {
                String name = manager.getPlayerName(staffUuid);
                String status = manager.isInModMode(staffUuid) ? " §c[MOD MODE]" : "";
                String vis = manager.isVanished(staffUuid) ? " §7[vanished]" : "";
                playerRef.sendMessage(Message.raw("§a✔ §f" + name + status + vis));
            }
        }
        playerRef.sendMessage(Message.raw("§7Total: §e" + staffOnline.size() + " §7staff in staff chat."));
    }
}
