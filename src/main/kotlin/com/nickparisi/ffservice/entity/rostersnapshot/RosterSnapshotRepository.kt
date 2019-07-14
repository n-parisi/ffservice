package com.nickparisi.ffservice.entity.rostersnapshot

import com.nickparisi.ffservice.entity.team.Team
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RosterSnapshotRepository: MongoRepository<RosterSnapshot, String> {
    fun findByTeam(team: Team): List<RosterSnapshot>
}