package com.thebluehats.server.api.listeners;

import com.thebluehats.server.core.modules.annotations.ServerApi;
import kong.unirest.UnirestInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class RegisterPlayer extends APIListener {
    private final UnirestInstance serverApi;

    @Inject
    public RegisterPlayer(@ServerApi UnirestInstance serverApi) {
        this.serverApi = serverApi;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
         standardRequest(serverApi.post("/api/v1/players"))
            .header("Content-Type", "application/json")
            .body("{\n    \"uuid\": \"" + event.getPlayer().getUniqueId() + "\"\n}\n")
            .asJson();
    }
}
