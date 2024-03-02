package com.loci.kmatch.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface MatchAPI {
        @GET("v4/teams/{teamId}/matches")
        suspend fun getPostTeam(
            @Header("X-Auth-Token") token: String,
            @Path("teamId") teamId: Int
        ): Response<Matches>
        @Headers("X-Auth-Token:eabaa69166254d99be52074facc3d4bb")
        @GET("v4/competitions/CL/matches")
        suspend fun getPostCl(): Response<Matches>

}