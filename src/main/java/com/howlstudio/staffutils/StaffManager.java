package com.howlstudio.staffutils;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

public class StaffManager {

    private final Path dataDir;
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final Set<UUID> frozenPlayers = new HashSet<>();
    private final Set<UUID> spyingPlayers = new HashSet<>();
    private final Set<UUID> staffChatPlayers = new HashSet<>();
    private final Set<UUID> modModePlayers = new HashSet<>();
    private final Map<UUID, List<Warning>> warnings = new HashMap<>();
    private final Map<UUID, String> playerNames = new HashMap<>(); // uuid -> name cache

    public StaffManager(Path dataDir) {
        this.dataDir = dataDir;
        loadWarnings();
    }

    // --- Vanish ---
    public boolean toggleVanish(UUID uuid) {
        if (vanishedPlayers.contains(uuid)) { vanishedPlayers.remove(uuid); return false; }
        vanishedPlayers.add(uuid); return true;
    }
    public boolean isVanished(UUID uuid) { return vanishedPlayers.contains(uuid); }

    // --- Freeze ---
    public boolean toggleFreeze(UUID target) {
        if (frozenPlayers.contains(target)) { frozenPlayers.remove(target); return false; }
        frozenPlayers.add(target); return true;
    }
    public boolean isFrozen(UUID uuid) { return frozenPlayers.contains(uuid); }

    // --- Spy ---
    public boolean toggleSpy(UUID uuid) {
        if (spyingPlayers.contains(uuid)) { spyingPlayers.remove(uuid); return false; }
        spyingPlayers.add(uuid); return true;
    }
    public boolean isSpying(UUID uuid) { return spyingPlayers.contains(uuid); }
    public Set<UUID> getSpyingPlayers() { return Collections.unmodifiableSet(spyingPlayers); }

    // --- Staff Chat ---
    public boolean toggleStaffChat(UUID uuid) {
        if (staffChatPlayers.contains(uuid)) { staffChatPlayers.remove(uuid); return false; }
        staffChatPlayers.add(uuid); return true;
    }
    public boolean isInStaffChat(UUID uuid) { return staffChatPlayers.contains(uuid); }
    public Set<UUID> getStaffChatPlayers() { return Collections.unmodifiableSet(staffChatPlayers); }

    // --- Mod Mode ---
    public boolean toggleModMode(UUID uuid) {
        if (modModePlayers.contains(uuid)) {
            modModePlayers.remove(uuid);
            vanishedPlayers.remove(uuid);
            spyingPlayers.remove(uuid);
            staffChatPlayers.remove(uuid);
            return false;
        }
        modModePlayers.add(uuid);
        vanishedPlayers.add(uuid);
        spyingPlayers.add(uuid);
        staffChatPlayers.add(uuid);
        return true;
    }
    public boolean isInModMode(UUID uuid) { return modModePlayers.contains(uuid); }

    // --- Warnings ---
    public void addWarning(UUID target, String targetName, UUID staffUuid, String staffName, String reason) {
        warnings.computeIfAbsent(target, k -> new ArrayList<>())
            .add(new Warning(targetName, staffName, reason, Instant.now()));
        playerNames.put(target, targetName);
        save();
    }

    public List<Warning> getWarnings(UUID target) {
        return warnings.getOrDefault(target, Collections.emptyList());
    }

    public String getPlayerName(UUID uuid) {
        return playerNames.getOrDefault(uuid, uuid.toString().substring(0, 8));
    }

    public void registerPlayer(UUID uuid, String name) {
        playerNames.put(uuid, name);
    }

    public void onPlayerLeave(UUID uuid) {
        vanishedPlayers.remove(uuid);
        frozenPlayers.remove(uuid);
        spyingPlayers.remove(uuid);
        staffChatPlayers.remove(uuid);
        modModePlayers.remove(uuid);
    }

    private void loadWarnings() {
        Path file = dataDir.resolve("warnings.dat");
        if (!Files.exists(file)) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            @SuppressWarnings("unchecked")
            Map<UUID, List<Warning>> loaded = (Map<UUID, List<Warning>>) ois.readObject();
            warnings.putAll(loaded);
        } catch (Exception ignored) {}
    }

    public void save() {
        try {
            Files.createDirectories(dataDir);
            Path file = dataDir.resolve("warnings.dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
                oos.writeObject(warnings);
            }
        } catch (IOException ignored) {}
    }
}
