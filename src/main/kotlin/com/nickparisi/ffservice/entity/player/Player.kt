package com.nickparisi.ffservice.entity.player

/**
 * Not a DB Object. Belongs to RosterSnapshot.
 */
//enumify these positions
data class Player(
        val name: String,
        val slot: String,
        val position: String,
        val score: Double?
)