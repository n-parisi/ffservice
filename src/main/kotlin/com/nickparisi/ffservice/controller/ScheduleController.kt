package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.entity.schedule.ScheduleRepository
import com.nickparisi.ffservice.entity.team.TeamRepository
import com.nickparisi.ffservice.service.OnboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedule")
class ScheduleController(val repository: ScheduleRepository) {

    @GetMapping("/{year}/{leagueId}")
    fun getByLeagueIdAndSeason(
            @PathVariable leagueId: Int,
            @PathVariable year: Int
    ) = repository.findByLeagueIdAndYear(leagueId, year)
}