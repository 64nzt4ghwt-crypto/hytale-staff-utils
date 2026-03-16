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

public class VanishCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public VanishCommand(StaffManager manager) {
        super("vanish", "[Staff] Toggle invisibility to regular players. Usage: /vanish");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID uuid = playerRef.getUuid();
        if (uuid == null) return;

        boolean nowVanished = manager.toggleVanish(uuid);
        if (nowVanished) {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fYou are now §avanished§f. Regular players cannot see you."));
        } else {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §fYou are now §cvisible§f to all players."));
        }
    }
}
