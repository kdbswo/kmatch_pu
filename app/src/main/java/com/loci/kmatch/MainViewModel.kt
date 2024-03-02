package com.loci.kmatch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loci.kmatch.data.Match
import com.loci.kmatch.data.MatchesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val repository = MatchesRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _matchesList = MutableStateFlow<List<Match>>(emptyList())
    val matchesList: StateFlow<List<Match>> = _matchesList

    init {
        viewModelScope.launch {
            fetchAllMatches()
        }
    }

    private suspend fun fetchMatches(teamId: Int): List<Match> {
        return try {
            val matches = repository.getPostTeam(teamId) ?: emptyList()
            val currentMatchesList = _matchesList.value

            val combinedList = currentMatchesList.toMutableList().apply {
                addAll(matches)
            }

            _matchesList.value = combinedList

////            Log.d("team", matches.toString())

            val gson = Gson()

            // Convert each JSON string in 'matches' to List of Match objects
            val matchListType = object : TypeToken<List<Match>>() {}.type
            val matchList: List<Match> = gson.fromJson(gson.toJsonTree(combinedList), matchListType)

            for (match in matchList) {
                Log.d("team", "Status: ${match.status}")
            }

            matches
        } catch (e: Exception) {
            Log.d("error", "$e")
            emptyList()
        }
    }

    fun fetchAllMatches() {
        viewModelScope.launch {
            _isLoading.value = true
            val matchesList = listOf(
                async { fetchMatches(73) },
//                async { fetchMatches(76) },
//                async { fetchMatches(524) },
//                async { fetchMatches(5) },
//                async { fetchMatches(15) },
//                async { fetchMatches(10) }
            )

            val fetchedMatches = matchesList.awaitAll()

            val allMatches = fetchedMatches.flatten()
            val sortedMatches = sortMatches(allMatches)

            _matchesList.value = sortedMatches

            loadingSwich()
        }
    }

    private fun sortMatches(mergedList: List<Match>?): List<Match> {
        val nonNullList = mergedList.orEmpty()
        return nonNullList
            .distinctBy { it.id }
            .sortedBy {
                LocalDateTime.parse(
                    it.utcDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                )
            }
    }

    private fun loadingSwich() {
        _isLoading.value = !isLoading.value
    }


    fun findTimedFirstMatch(matches: List<Match>): Int {
        val completedMatch = matches.firstOrNull { it.status == "IN_PLAY" || it.status == "TIMED" }
        return matches.indexOf(completedMatch)
    }

    fun convertUtcToKoreanTime(utcDate: String): String {

        val instant: Instant = Instant.parse(utcDate)

        val koreanDateTime: LocalDateTime =
            instant.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime()

        return koreanDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    fun convertToKoreanTeamName(originalName: String): String {
        return when (originalName) {
            "Everton FC" -> "에버턴"
            "Brighton & Hove Albion FC" -> "브라이튼"
            "Wolverhampton Wanderers FC" -> "울버햄튼"
            "Manchester United FC" -> "맨체스터 유나이티드"
            "Chelsea FC" -> "첼시"
            "Brentford FC" -> "브렌트포드"
            "Tottenham Hotspur FC" -> "토트넘"
            "FC Bayern München" -> "바이에른 뮌헨"
            "TSG 1899 Hoffenheim" -> "호펜하임"
            "1. FSV Mainz 05" -> "마인츠"
            "VfL Wolfsburg" -> "볼프스부르크"
            "1. FC Union Berlin" -> "유니온 베를린"
            "SV Werder Bremen" -> "베르더 브레멘"
            "AFC Bournemouth" -> "본머스"
            "Borussia Mönchengladbach" -> "뮌헨글라트바흐"
            "VfB Stuttgart" -> "슈투트가르트"
            "Racing Club de Lens" -> "RC 랑스"
            "Paris Saint-Germain FC" -> "파리 생제르맹"
            "Borussia Dortmund" -> "도르트문트"
            "FC Heidenheim 1846" -> "하이덴하임 1846"
            "SV Darmstadt 98" -> "다름슈타트"
            "Crystal Palace FC" -> "크리스탈 팰리스"
            "VfL Bochum 1848" -> "보훔 1848"
            "AC Milan" -> "AC밀란"
            "Galatasaray SK" -> "갈라타사라이"
            "Liverpool FC" -> "리버풀"
            "FC Lorient" -> "로리앙"
            "Toulouse FC" -> "툴루즈"
            "RB Leipzig" -> "라이프치히"
            "Eintracht Frankfurt" -> "프랑크푸르트"
            "FC Augsburg" -> "아우크스부르크"
            "SC Freiburg" -> "프라이부르크"
            "Burnley FC" -> "번리"
            "Olympique Lyonnais" -> "올림피크 리옹"
            "Bayer 04 Leverkusen" -> "레버쿠젠"
            "OGC Nice" -> "니스"
            "Sheffield United FC" -> "셰필드 유나이티드"
            "Luton Town FC" -> "루턴 타운"
            "Arsenal FC" -> "아스날"
            "Olympique de Marseille" -> "마르세유"
            "1. FC Köln" -> "쾰른"
            "Manchester City FC" -> "맨체스터 시티"
            "Clermont Foot 63" -> "클레르몽"
            "FC København" -> "코펜하겐"
            "Newcastle United FC" -> "뉴캐슬 유나이티드"
            "RC Strasbourg Alsace" -> "스트라스부르"
            "Fulham FC" -> "풀럼"
            "Stade Brestois 29" -> "브레스트"
            "Montpellier HSC" -> "몽펠리에"
            "1. FC Heidenheim 1846" -> "하이덴하임"
            "Stade de Reims" -> "스타드 랭스"
            "Lille OSC" -> "릴 OSC"
            "SS Lazio" -> "라치오"
            "Real Sociedad de Fútbol" -> "레알 소시에다드"
            "FC Nantes" -> "낭트"
            "Stade Rennais FC 1901" -> "렌"
            "AS Monaco FC" -> "모나코"
            "Aston Villa FC" -> "아스톤 빌라"
            "West Ham United FC" -> "웨스트햄"
            "Nottingham Forest FC" -> "노팅엄 포레스트"
            "Le Havre AC" -> "르아브르"
            else -> originalName
        }
    }

}


