package com.nickparisi.ffservice.entity.team

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository: MongoRepository<Team, String> {
    fun findByLeagueIdAndYear(leagueId: Int, year: Int): List<Team>
}