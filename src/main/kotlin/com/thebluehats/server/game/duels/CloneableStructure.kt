package com.thebluehats.server.game.duels

import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Position

interface CloneableStructure {
    val minBound: Position
    val maxBound: Position
}