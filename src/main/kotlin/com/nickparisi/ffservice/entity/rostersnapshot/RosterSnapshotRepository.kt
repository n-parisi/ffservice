package com.nickparisi.ffservice.entity.rostersnapshot

import com.nickparisi.ffservice.entity.team.Team
import org.springframework.data.mongodb.repository.MongoRepository

interface RosterSnapshotRepository: MongoRepository<RosterSnapshot, Long> {
    fun findByTeam(team: Team): List<RosterSnapshot>
}