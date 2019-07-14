package com.nickparisi.ffservice.entity.rostersnapshot

import com.nickparisi.ffservice.entity.enums.RosterSlot
import com.nickparisi.ffservice.entity.player.Player
import com.nickparisi.ffservice.entity.team.Team
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
//opposing roster snap
data class RosterSnapshot(
        @Id
        val uuid: String? = null,

        val week: Int,
        val team: Team,
        val players: Collection<Player>
) {
        val totalScore: Double
                get() = players.stream()
                                .filter { it.slot != RosterSlot.BENCH }
                                .map { it.score ?: 0.0 }
                                .reduce(0.0) { a, b -> a + b }
}