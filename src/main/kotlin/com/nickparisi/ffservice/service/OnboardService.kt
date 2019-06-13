package com.nickparisi.ffservice.service

import com.fasterxml.jackson.databind.JsonNode
import com.nickparisi.ffservice.util.UrlGenerator
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.nickparisi.ffservice.entity.player.Player
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshot
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshotRepository
import com.nickparisi.ffservice.entity.team.Team
import com.nickparisi.ffservice.entity.team.TeamRepository
import org.springframework.beans.factory.annotation.Autowired

@Service
class OnboardService(@Autowired
                     restTemplateBuilder: RestTemplateBuilder,

                     @Autowired
                     val teamRepository: TeamRepository,

                     @Autowired
                     val rosterSnapshotRepository: RosterSnapshotRepository

) {

    private val restTemplate = restTemplateBuilder.build()
    private val mapper = ObjectMapper()

    fun onboardCompletedSeason(leagueId: Int, year: Int) {
        //cache team entities as we create them to avoid looking up on each iteration
        val teamsSaved = mutableMapOf<Long, Team>()
        for (week in 1..16) {
            val response: ResponseEntity<String> = restTemplate.getForEntity(UrlGenerator.generateUrl(leagueId, year, week))
            val root = mapper.readTree(response.body)

            val teams = root.path("teams")
            for (team in teams) {
                val teamId = team.path("id").longValue()

                //Save team entities on first week
                val teamEntity: Team
                if (week == 1) {
                    val teamName = "${team.path("location").textValue()} ${team.path("nickname").textValue()}"
                    val teamAbbrev = team.path("abbrev").textValue()

                    teamEntity = Team(IdGenerator.id, teamId, year, leagueId, teamName, teamAbbrev)
                    teamsSaved[teamId] = teamEntity

                    teamRepository.save(teamEntity)
                } else {
                    teamEntity = teamsSaved[teamId] ?: throw Exception("New ID found in week $week. Should not happen.")
                }

                val startingPlayers = team.path("roster").path("entries")
                val rosterPlayerEntities = getPlayersFromJson(startingPlayers, week)

                rosterSnapshotRepository.save(RosterSnapshot(IdGenerator.id, week, teamEntity, rosterPlayerEntities))
            }

            println("Finished adding week $week for league $leagueId")

        }
    }

    private fun getPlayersFromJson(startingPlayers: JsonNode, week: Int): List<Player> {
        val rosterPlayerEntities = mutableListOf<Player>()
        for (startingPlayer in startingPlayers) {
            val slot = getRosterSlot(startingPlayer.path("lineupSlotId").intValue())

            val playerData = startingPlayer.path("playerPoolEntry").path("player")
            val name = playerData.path("fullName").textValue()
            val position = getPosition(playerData.path("defaultPositionId").intValue())

            var points: Double? = null
            for (playerDataStat in playerData["stats"]) {
                if (playerDataStat["scoringPeriodId"].intValue() == week &&
                        playerDataStat["statSourceId"].intValue() == 0) {
                    points = playerDataStat["appliedTotal"].doubleValue()
                    break
                }
            }
            rosterPlayerEntities.add(Player(name, slot, position, points))
        }

        return rosterPlayerEntities
    }

    private fun getRosterSlot(slotId: Int) = when (slotId) {
        0 -> "QB"
        2 -> "RB"
        4 -> "WR"
        6 -> "TE"
        16 -> "D/ST"
        17 -> "K"
        20 -> "BENCH"
        23 -> "FLEX"
        else -> "UNKNOWN"
    }

    private fun getPosition(positionId: Int) = when (positionId) {
        1 -> "QB"
        2 -> "RB"
        3 -> "WR"
        4 -> "TE"
        16 -> "D/ST"
        5 -> "K"
        else -> "UNKNOWN"
    }

    //TODO: Better id gen
    private object IdGenerator {
        var id = 0L
            get() = field++
            private set
    }
}