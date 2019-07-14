package com.nickparisi.ffservice.entity.schedule

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository: MongoRepository<Schedule, String> {
    fun findByLeagueIdAndYear(leagueId: Int, year: Int): Schedule
}