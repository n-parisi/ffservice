package com.nickparisi.ffservice.entity.team

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * A Team document represents one team in a league for a specific year
 * TODO: add scoring format metadata..will need to be its own object
 */

@Document
data class Team(
        @Id
        val uuid: String? = null,

        val teamId: Int,
        val year: Int,
        val leagueId: Int,
        var teamName: String,
        var teamAbbrev: String
)