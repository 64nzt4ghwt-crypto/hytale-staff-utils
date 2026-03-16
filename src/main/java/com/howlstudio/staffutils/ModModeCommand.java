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

public class ModModeCommand extends AbstractPlayerCommand {
    private final StaffManager manager;

    public ModModeCommand(StaffManager manager) {
        super("modmode", "[Staff] Toggle full moderation mode (vanish + spy + staff chat). Usage: /modmode");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID uuid = playerRef.getUuid();
        if (uuid == null) return;

        boolean inModMode = manager.toggleModMode(uuid);
        if (inModMode) {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §6§lMod Mode ENABLED§f!"));
            playerRef.sendMessage(Message.raw("§7  ✔ Vanish §a(on)"));
            playerRef.sendMessage(Message.raw("§7  ✔ Chat spy §a(on)"));
            playerRef.sendMessage(Message.raw("§7  ✔ Staff chat §a(on)"));
            playerRef.sendMessage(Message.raw("§7Use §e/modmode §7again to disable all."));
        } else {
            playerRef.sendMessage(Message.raw("§a[StaffUtils] §c§lMod Mode DISABLED§f."));
            playerRef.sendMessage(Message.raw("§7  ✗ Vanish §c(off) §7| ✗ Spy §c(off) §7| ✗ Staff chat §c(off)"));
        }
    }
}
