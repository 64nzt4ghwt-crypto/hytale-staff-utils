package com.howlstudio.staffutils;

import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

/**
 * StaffUtils — Essential tools for server staff and moderators.
 *
 * Commands:
 *   /vanish          — Become invisible to regular players
 *   /staffchat       — Toggle staff-only chat channel
 *   /freeze <player> — Prevent a player from moving (anti-cheat assist)
 *   /spy             — Toggle chat spy (see all private messages)
 *   /stafflist       — List all online staff members
 *   /warn <player>   — Issue a formal warning to a player
 *   /warns <player>  — View a player's warning history
 *   /modmode         — Toggle full moderation mode (vanish + spy + staff chat)
 */
public final class StaffUtilsPlugin extends JavaPlugin {

    private StaffManager staffManager;

    public StaffUtilsPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        System.out.println("[StaffUtils] Loading...");

        staffManager = new StaffManager(getDataDirectory());

        CommandManager cmd = CommandManager.get();
        cmd.register(new VanishCommand(staffManager));
        cmd.register(new StaffChatCommand(staffManager));
        cmd.register(new FreezeCommand(staffManager));
        cmd.register(new SpyCommand(staffManager));
        cmd.register(new StaffListCommand(staffManager));
        cmd.register(new WarnCommand(staffManager));
        cmd.register(new WarnsCommand(staffManager));
        cmd.register(new ModModeCommand(staffManager));

        new StaffListener(staffManager).register();

        System.out.println("[StaffUtils] Ready! 8 commands loaded.");
    }

    @Override
    protected void shutdown() {
        if (staffManager != null) {
            staffManager.save();
            System.out.println("[StaffUtils] Data saved.");
        }
    }
}
