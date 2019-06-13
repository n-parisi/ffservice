package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.entity.team.TeamRepository
import com.nickparisi.ffservice.service.OnboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/team")
//TODO: Our controllers should return proper HTTP responses
class TeamController(val repository: TeamRepository) {

    @GetMapping("/{teamId}")
    fun getById(@PathVariable teamId: Long) = repository.findById(teamId)

    @GetMapping("/{leagueId}/{year}")
    fun getByLeagueIdAndSeason(
            @PathVariable leagueId: Int,
            @PathVariable year: Int
    ) = repository.findByLeagueIdAndYear(leagueId, year)
}