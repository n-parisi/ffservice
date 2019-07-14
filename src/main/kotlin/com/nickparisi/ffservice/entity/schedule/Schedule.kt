package com.nickparisi.ffservice.entity.schedule

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Schedule(
        @Id
        val uuid: String? = null,

        val year: Int,
        val leagueId: Int,
        val matchups: Collection<Matchup>
)