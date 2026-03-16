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

import java.util.Arrays;
import java.util.UUID;

public class FreezeCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public FreezeCommand(StaffManager manager) {
        super("freeze", "[Staff] Freeze a player. Usage: /freeze <player>");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            playerRef.sendMessage(Message.raw("§cUsage: /freeze <player>"));
            return;
        }

        String targetName = parts[1];
        PlayerRef targetRef = Universe.get().getPlayerByUsername(targetName, NameMatching.EXACT);
        if (targetRef == null) {
            playerRef.sendMessage(Message.raw("§c[StaffUtils] Player not found: §f" + targetName));
            return;
        }

        UUID targetUuid = targetRef.getUuid();
        if (targetUuid == null) return;

        boolean nowFrozen = manager.toggleFreeze(targetUuid);
        if (nowFrozen) {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §f" + targetName + " §ahas been frozen."));
            targetRef.sendMessage(Message.raw("§c[StaffUtils] §fYou have been §cfrozen §fby a staff member."));
        } else {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §f" + targetName + " §ahas been unfrozen."));
            targetRef.sendMessage(Message.raw("§a[StaffUtils] §fYou have been §aunfrozen."));
        }
    }
}
