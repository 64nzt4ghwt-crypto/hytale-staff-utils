package com.howlstudio.staffutils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class WarnsCommand extends AbstractPlayerCommand {
    private final StaffManager manager;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MM/dd HH:mm").withZone(ZoneId.systemDefault());

    public WarnsCommand(StaffManager manager) {
        super("warns", "View a player's warnings. Usage: /warns <player>");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+");

        String targetName;
        UUID targetUuid;

        if (parts.length < 2) {
            // Show own warns
            targetUuid = playerRef.getUuid();
            if (targetUuid == null) return;
            targetName = manager.getPlayerName(targetUuid);
        } else {
            targetName = parts[1];
            targetUuid = UUID.nameUUIDFromBytes(targetName.getBytes());
        }

        List<Warning> warns = manager.getWarnings(targetUuid);
        playerRef.sendMessage(Message.raw("§6§l--- Warnings for " + targetName + " ---"));
        if (warns.isEmpty()) {
            playerRef.sendMessage(Message.raw("§7No warnings on record."));
        } else {
            for (int i = 0; i < warns.size(); i++) {
                Warning w = warns.get(i);
                playerRef.sendMessage(Message.raw("§c#" + (i + 1) + " §f" + w.getReason()
                    + " §7(by §f" + w.getStaffName() + "§7, " + FMT.format(w.getTimestamp()) + ")"));
            }
            playerRef.sendMessage(Message.raw("§7Total: §c" + warns.size() + " §7warning(s)."));
        }
    }
}
