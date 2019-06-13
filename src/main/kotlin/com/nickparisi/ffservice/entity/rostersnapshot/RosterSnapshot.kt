package com.nickparisi.ffservice.entity.rostersnapshot

import com.nickparisi.ffservice.entity.player.Player
import com.nickparisi.ffservice.entity.team.Team
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


//better data types for date/time objects
@Document
data class RosterSnapshot(
        @Id //Generated
        val rosterId: Long,

        val week: Int,
        val team: Team,
        val players: Collection<Player>
) {
        val totalScore: Double
                get() = players.stream()
                        .filter { it.slot != "BENCH" }
                        .map { it.score ?: 0.0 }
                        .reduce(0.0) { a, b -> a + b }
}