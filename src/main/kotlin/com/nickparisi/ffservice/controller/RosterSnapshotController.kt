package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshot
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshotRepository
import com.nickparisi.ffservice.entity.team.TeamRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rostersnap")
//TODO: Our controllers should return proper HTTP responses
class RosterSnapshotController(val rosterSnapshotRepository: RosterSnapshotRepository,
                               val teamRepository: TeamRepository
) {

    @GetMapping("/{rosterId}")
    fun getById(@PathVariable rosterId: Long) = rosterSnapshotRepository.findById(rosterId)

    @GetMapping("/teamId/{teamId}")
    fun getByLeagueIdAndSeason(@PathVariable teamId: Long): List<RosterSnapshot> {
        val team = teamRepository.findById(teamId)
        return if (team.isPresent) rosterSnapshotRepository.findByTeam(team.get()) else emptyList()
    }
}