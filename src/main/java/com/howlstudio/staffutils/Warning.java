package com.howlstudio.staffutils;

import java.io.Serializable;
import java.time.Instant;

public class Warning implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String playerName;
    private final String staffName;
    private final String reason;
    private final Instant timestamp;

    public Warning(String playerName, String staffName, String reason, Instant timestamp) {
        this.playerName = playerName;
        this.staffName = staffName;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public String getPlayerName() { return playerName; }
    public String getStaffName() { return staffName; }
    public String getReason() { return reason; }
    public Instant getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "§c" + reason + " §7(by §f" + staffName + "§7)";
    }
}
