package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.entity.team.TeamRepository
import com.nickparisi.ffservice.service.OnboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/team")
class TeamController(val repository: TeamRepository) {

    @GetMapping("/{teamId}")
    fun getById(@PathVariable teamId: String) = repository.findById(teamId)

    @GetMapping("/{year}/{leagueId}")
    fun getByLeagueIdAndSeason(
            @PathVariable leagueId: Int,
            @PathVariable year: Int
    ) = repository.findByLeagueIdAndYear(leagueId, year)
}