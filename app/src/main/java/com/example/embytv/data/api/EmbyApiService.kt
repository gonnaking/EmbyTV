package com.example.embytv.data.api

import com.example.embytv.data.model.*
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Retrofit interface for Emby Server REST API.
 * All endpoints require X-Emby-Token auth unless noted.
 */
interface EmbyApiService {

    @GET("System/Info/Public")
    suspend fun getSystemPublicInfo(): SystemInfo

    @POST("Users/AuthenticateByName")
    @FormUrlEncoded
    suspend fun authenticate(
        @Field("UserName") username: String,
        @Field("Pw") password: String,
        @Field("DeviceId") deviceId: String = "embytv_android",
        @Field("DeviceName") deviceName: String = "EmbyTV"
    ): AuthenticationResult

    /**
     * GET /Users/{userId}/Items - List library items.
     * Token passed via OkHttpClient interceptor; this parameter is optional here.
     */
    @GET("Users/{userId}/Items")
    suspend fun getUserItems(
        @Path("userId") userId: String,
        @Query("Fields") fields: String = "PrimaryImage,MediaSources,Chapters",
        @Query("IncludeItemTypes") includeTypes: String = "Movie,Series",
        @Query("SortBy") sortBy: String = "SortName",
        @Query("Limit") limit: Int = 30,
        @Query("StartIndex") startIndex: Int = 0
    ): ResultPage<MediaItem>

    @GET("Users/{userId}/Items/{itemId}")
    suspend fun getItemDetails(
        @Path("userId") userId: String,
        @Path("itemId") itemId: String,
        @Query("Fields") fields: String = "PrimaryImage,MediaSources,Chapters,People,Genres,Taglines,Overview,ParentId,Studios"
    ): MediaItem

    /** Returns raw ResponseBody bytes — image loaded by Coil. */
    @GET("Users/{userId}/Items/{itemId}/Images/Primary?quality={quality}")
    suspend fun getItemPoster(
        @Path("userId") userId: String,
        @Path("itemId") itemId: String,
        @Path("quality") quality: Int = 90
    ): ResponseBody

    @POST("Videos/{itemId}/PlaybackInfo")
    suspend fun getPlaybackInfo(
        @Path("itemId") itemId: String,
        @Body request: PlaybackRequest
    ): PlaybackResponse

    @GET("Users/{userId}/Items/Latest")
    suspend fun getLatestItems(
        @Path("userId") userId: String,
        @Query("Limit") limit: Int = 12,
        @Query("MediaTypes") mediaTypes: String = "Video"
    ): List<MediaItem>

    @GET("Shows/{seriesId}/Episodes")
    suspend fun getSeasonEpisodes(
        @Path("seriesId") seriesId: String,
        @Path("userId") userId: String,
        @Query("SeasonId") seasonId: String? = null,
        @Query("SortBy") sortBy: String = "SortName",
        @Query("Limit") limit: Int = 30
    ): ResultPage<MediaItem>
}
