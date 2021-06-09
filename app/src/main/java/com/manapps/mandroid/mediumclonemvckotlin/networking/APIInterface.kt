package com.manapps.mandroid.mediumclonemvckotlin.networking

import com.manapps.mandroid.mediumclonemvckotlin.models.request.LoginRequest
import com.manapps.mandroid.mediumclonemvckotlin.models.request.SignupRequest
import com.manapps.mandroid.mediumclonemvckotlin.models.response.ArticleResponse
import com.manapps.mandroid.mediumclonemvckotlin.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvckotlin.models.response.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIInterface {

    @POST("users")
    fun sendCreateAccountRequest(
        @Body userCreds: SignupRequest
    ): Call<UserResponse>

    @POST("users/login")
    fun sendlLoginRequest(
        @Body userCreds: LoginRequest
    ): Call<UserResponse>


    @GET("articles")
    fun getAuthorsArticles(
        @Query("author") author: String? = null,
        @Query("favourited") favourited: String? = null,
        @Query("tag") tag: String? = null
    ): Call<ArticlesResponse>

    @GET("articles/feed")
    fun getFeedArticles(): Call<ArticlesResponse>

    @GET("articles/{slug}")
     fun getArticleBySlug(
        @Path("slug") slug: String
    ): Call<ArticleResponse>
}