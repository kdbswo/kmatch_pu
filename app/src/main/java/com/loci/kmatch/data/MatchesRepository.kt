package com.loci.kmatch.data

import android.util.Log
import com.loci.kmatch.BuildConfig

class MatchesRepository {
    private val apiService = RetrofitInstance.api


    suspend fun getPostTeam(teamId: Int): List<Match>? {
        return try {
            val response = apiService.getPostTeam(BuildConfig.FOOTBALL_DATA_API_KEY, teamId)
            if (response.isSuccessful) {
                response.body()?.matches
            } else {
                Log.e("MainViewModel", "Error fetching data: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error fetching data: $e")
            null
        }
    }
}