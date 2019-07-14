package com.nickparisi.ffservice.entity.player

import com.nickparisi.ffservice.entity.enums.Position
import com.nickparisi.ffservice.entity.enums.RosterSlot

/**
 * Not a DB Object. Belongs to RosterSnapshot - represents a player performance for a week.
 *
 * TODO: Ultimately we will want to be able to provide information like ownership history for a given player in a
 *       league, week to week scores unique to league for this player, etc. But may also want to get this info across
 *       many leagues. This object could get complicated very quickly and it should be carefully thought out how to
 *       save players in the DB. Probably make each Player unique to a league.
 *       For now, prioritize the application to serve information about teams/rosters.
 */
data class Player(
        val name: String,
        val slot: RosterSlot,
        val position: Position,
        val score: Double?
)