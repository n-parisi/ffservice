package com.nickparisi.ffservice.util

object UrlGenerator {
    //append with scordingPeriodId
    fun generateUrl(leagueId: Int, year: Int, week: Int) =
            "http://fantasy.espn.com/apis/v3/games/ffl/seasons/$year/segments/0/leagues/$leagueId?view=mBoxscore" +
            "&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mStatus&view=mTeam&view=mRoster&view=modular" +
            "&view=mNav&view=mDraftDetail&view=mLiveScoring&view=mPeriodMatchups&scoringPeriodId=$week"
}