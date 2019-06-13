package com.nickparisi.ffservice.entity.team

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * A Team document represents one team in a league for a specific year
 * TODO: Enums for positions, smarter obj for year
 */

@Document
data class Team(@Id //Generated unique ID
                val teamId: Long,

                //teamIdLocal is ID of team for that league
                val teamIdLocal: Long,
                val year: Int,
                val leagueId: Int,
                var teamName: String,
                var teamAbbrev: String
)