package com.nickparisi.ffservice.entity.schedule

import com.nickparisi.ffservice.entity.team.Team

/**
 * Not a DB object. Belongs to Schedule.
 */
data class Matchup(
        val week: Int,
        val awayTeam: Team?,
        val homeTeam: Team?,
        val awayScore: Double?,
        val homeScore: Double?,
        val winner: String
)