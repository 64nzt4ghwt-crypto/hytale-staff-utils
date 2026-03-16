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

public class SpyCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public SpyCommand(StaffManager manager) {
        super("spy", "[Staff] Toggle chat spy mode (see all private messages). Usage: /spy");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID uuid = playerRef.getUuid();
        if (uuid == null) return;

        boolean nowSpying = manager.toggleSpy(uuid);
        if (nowSpying) {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fSpy mode §aenabled§f. You can see all private messages."));
        } else {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fSpy mode §cdisabled§f."));
        }
    }
}
