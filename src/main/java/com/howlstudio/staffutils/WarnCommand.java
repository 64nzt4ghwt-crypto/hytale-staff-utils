package com.howlstudio.staffutils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

public class WarnCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public WarnCommand(StaffManager manager) {
        super("warn", "[Staff] Warn a player. Usage: /warn <player> <reason>");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID staffUuid = playerRef.getUuid();
        if (staffUuid == null) return;

        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+", 3);

        if (parts.length < 3) {
            playerRef.sendMessage(Message.raw("§cUsage: /warn <player> <reason>"));
            return;
        }

        String targetName = parts[1];
        String reason = parts[2];

        PlayerRef targetRef = Universe.get().getPlayerByUsername(targetName, NameMatching.EXACT);
        UUID targetUuid = targetRef != null ? targetRef.getUuid() : UUID.nameUUIDFromBytes(targetName.getBytes());
        String staffName = manager.getPlayerName(staffUuid);

        manager.addWarning(targetUuid, targetName, staffUuid, staffName, reason);

        int warnCount = manager.getWarnings(targetUuid).size();
        playerRef.sendMessage(Message.raw("§a[StaffUtils] §fWarned §e" + targetName + "§f: §c" + reason
            + " §7(warning #" + warnCount + ")"));

        if (targetRef != null) {
            targetRef.sendMessage(Message.raw("§c[Warning] §fYou have been warned by staff: §c" + reason));
            targetRef.sendMessage(Message.raw("§7This is your warning #" + warnCount + ". Further violations may result in a ban."));
        }

        StaffChatCommand.broadcastToStaff(manager, "§d[Staff] §f" + staffName + " §7warned §f" + targetName
            + "§7: §c" + reason + " §7(#" + warnCount + ")");
    }
}
