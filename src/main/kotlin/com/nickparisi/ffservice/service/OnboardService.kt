package com.nickparisi.ffservice.service

import com.fasterxml.jackson.databind.JsonNode
import com.nickparisi.ffservice.util.UrlGenerator
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.nickparisi.ffservice.entity.enums.Position
import com.nickparisi.ffservice.entity.player.Player
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshot
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshotRepository
import com.nickparisi.ffservice.entity.team.Team
import com.nickparisi.ffservice.entity.team.TeamRepository
import com.nickparisi.ffservice.entity.enums.RosterSlot
import com.nickparisi.ffservice.entity.schedule.Matchup
import com.nickparisi.ffservice.entity.schedule.Schedule
import com.nickparisi.ffservice.entity.schedule.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@Service
class OnboardService(@Autowired
                     val restTemplateBuilder: RestTemplateBuilder,

                     @Autowired
                     val teamRepository: TeamRepository,

                     @Autowired
                     val rosterSnapshotRepository: RosterSnapshotRepository,

                     @Autowired
                     val scheduleRepository: ScheduleRepository

) {

    private val restTemplate = restTemplateBuilder.build()
    private val mapper = ObjectMapper()

    fun onboardCompletedSeason(leagueId: Int, year: Int) {
        //TODO: Error handling, success/fail states
        //TODO: do something better than throw exceptions

        //cache team entities as we create them to avoid looking up on each iteration
        val teamsSaved = mutableMapOf<Int, Team>()
        for (week in 1..16) {
            val response: ResponseEntity<String> = restTemplate.getForEntity(UrlGenerator.generateEspnUrl(leagueId, year, week))
            val root = mapper.readTree(response.body)

            val teams = root.path("teams")
            for (team in teams) {
                val teamId = team.path("id").intValue()

                //Save team entities on first week
                val teamEntity: Team
                if (week == 1) {
                    val teamName = "${team.path("location").textValue()} ${team.path("nickname").textValue()}"
                    val teamAbbrev = team.path("abbrev").textValue()

                    val teamObject = Team(teamId = teamId, year = year, leagueId = leagueId, teamName = teamName, teamAbbrev = teamAbbrev)
                    teamEntity = teamRepository.save(teamObject)
                    teamsSaved[teamId] = teamEntity
                } else {
                    teamEntity = teamsSaved[teamId] ?: throw Exception("New ID found in week $week. Should not happen.")
                }

                val startingPlayers = team.path("roster").path("entries")
                val rosterPlayerEntities = getPlayersFromJson(startingPlayers, week)

                rosterSnapshotRepository.save(RosterSnapshot(week = week, team = teamEntity, players = rosterPlayerEntities))
            }

            //Save schedule on week 1
            if (week == 1) {
                val schedule = root.path("schedule")

                val matchups = mutableListOf<Matchup>()
                for (matchup in schedule) {
                    val awayNode = matchup.path("away")
                    val homeNode = matchup.path("home")

                    val awayTeam: Team?
                    val awayScore: Double?
                    if (awayNode != null) {
                        val awayTeamId = awayNode.path("teamId").intValue()
                        awayTeam = teamsSaved[awayTeamId]
                        awayScore = awayNode.path("totalPoints").doubleValue()
                    } else {
                        awayTeam = null
                        awayScore = null
                    }

                    val homeTeam: Team?
                    val homeScore: Double?
                    if (homeNode != null) {
                        val homeTeamId = homeNode.path("teamId").intValue()
                        homeTeam = teamsSaved[homeTeamId]
                        homeScore = homeNode.path("totalPoints").doubleValue()
                    } else {
                        homeTeam = null
                        homeScore = null
                    }

                    val matchupPeriodId = matchup.path("matchupPeriodId").intValue()
                    val winner = matchup.path("winner").toString().removeSurrounding("\"", "\"")

                    val matchupEntity = Matchup(matchupPeriodId, awayTeam, homeTeam, awayScore, homeScore, winner)
                    matchups.add(matchupEntity)
                }

                val scheduleEntity = Schedule(year = year, leagueId =  leagueId, matchups =  matchups)
                scheduleRepository.save(scheduleEntity)
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
        0 -> RosterSlot.QB
        2 -> RosterSlot.RB
        4 -> RosterSlot.WR
        6 -> RosterSlot.TE
        16 -> RosterSlot.DST
        17 -> RosterSlot.K
        20 -> RosterSlot.BENCH
        23 -> RosterSlot.FLEX
        else -> RosterSlot.UNKNOWN
    }

    private fun getPosition(positionId: Int) = when (positionId) {
        1 -> Position.QB
        2 -> Position.RB
        3 -> Position.WR
        4 -> Position.TE
        16 -> Position.DST
        5 -> Position.K
        else -> Position.UNKNOWN
    }
}