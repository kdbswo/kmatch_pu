package com.loci.kmatch.data


data class Matches(
    val count: Int,
    val matches: List<Match>
)

data class Match(
    val id: Long,
//    val season: Season,
    val utcDate: String, // 날짜 및 시간
    val status: String,
//    val stage: String?,
//    val group: String?,
//    val lastUpdated: String, // 마지막 업데이트 날짜 및 시간
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score?,
    // 필요에 따라 다른 필드 추가
)

//data class Season(
//    val id: Long,
//    val startDate: String, // 시즌 시작 날짜
//    val endDate: String, // 시즌 종료 날짜
//    val currentMatchday: Int?,
//    val winner: Team?, // Winner 정보
//)

data class Team(
    val id: Long,
    val name: String,
    val crest: String?, // 팀 로고 URL
)

data class Score(
    val winner: String?, // 승자 팀
    val duration: String?, // 경기 시간
    val fullTime: Time?,
//    val halfTime: Time?,
)

data class Time(
    val home: Int?,
    val away: Int?
)