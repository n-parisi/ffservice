package com.nickparisi.ffservice.entity.team

import org.springframework.data.mongodb.repository.MongoRepository

interface TeamRepository: MongoRepository<Team, Long> {
    fun findByLeagueIdAndYear(leagueId: Int, year: Int): List<Team>
}