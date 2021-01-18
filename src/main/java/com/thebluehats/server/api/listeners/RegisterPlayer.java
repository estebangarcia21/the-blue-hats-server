package com.thebluehats.server.api.listeners;

import com.thebluehats.server.core.modules.annotations.ServerAPI;
import kong.unirest.UnirestInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class RegisterPlayer extends APIListener {
    private final UnirestInstance serverAPI;

    @Inject
    public RegisterPlayer(@ServerAPI UnirestInstance serverAPI) {
        this.serverAPI = serverAPI;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
         standardRequest(serverAPI.post("/api/v1/players"))
            .header("Content-Type", "application/json")
            .body("{\n    \"uuid\": \"" + event.getPlayer().getUniqueId() + "\"\n}\n")
            .asJson();
    }
}
