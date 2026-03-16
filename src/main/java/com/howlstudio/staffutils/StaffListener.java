package com.howlstudio.staffutils;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.UUID;

public class StaffListener {

    private final StaffManager manager;

    public StaffListener(StaffManager manager) {
        this.manager = manager;
    }

    public void register() {
        var bus = HytaleServer.get().getEventBus();
        bus.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        bus.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        PlayerRef ref = player.getPlayerRef();
        if (ref == null) return;
        UUID uuid = ref.getUuid();
        if (uuid == null) return;
        String name = ref.getUsername() != null ? ref.getUsername() : uuid.toString().substring(0, 8);
        manager.registerPlayer(uuid, name);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef ref = event.getPlayerRef();
        if (ref == null) return;
        UUID uuid = ref.getUuid();
        if (uuid != null) manager.onPlayerLeave(uuid);
    }
}
