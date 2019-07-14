package com.nickparisi.ffservice.controller

import com.nickparisi.ffservice.service.OnboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/onboard")
class OnboardController(val service: OnboardService) {

    @PostMapping
    fun onboardCompletedSeason(@RequestBody onboardRequest: OnboardRequest): OnboardResponse {
        service.onboardCompletedSeason(onboardRequest.leagueId, onboardRequest.season)
        return OnboardResponse("Success!", true)
    }

    class OnboardRequest(val leagueId: Int, val season: Int)
    class OnboardResponse(val message: String, val onboarded: Boolean)
}