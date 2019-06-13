package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.service.OnboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/onboard")
//TODO: Our controllers should return proper HTTP responses
class OnboardController(val service: OnboardService) {

    @PostMapping
    fun onboardCompletedSeason(@RequestBody onboardRequest: OnboardRequest): String {
        service.onboardCompletedSeason(onboardRequest.leagueId, onboardRequest.season)
        return "Success!"
    }

    class OnboardRequest(val leagueId: Int, val season: Int)
}