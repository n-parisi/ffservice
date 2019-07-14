package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshot
import com.nickparisi.ffservice.entity.rostersnapshot.RosterSnapshotRepository
import com.nickparisi.ffservice.entity.team.TeamRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rostersnap")
class RosterSnapshotController(val rosterSnapshotRepository: RosterSnapshotRepository,
                               val teamRepository: TeamRepository
) {

    @GetMapping("/{rosterId}")
    fun getById(@PathVariable rosterId: String) = rosterSnapshotRepository.findById(rosterId)

    @GetMapping("/teamId/{uuid}")
    fun getByLeagueIdAndSeason(@PathVariable uuid: String): List<RosterSnapshot> {
        val team = teamRepository.findById(uuid)
        return if (team.isPresent) rosterSnapshotRepository.findByTeam(team.get()) else emptyList()
    }
}