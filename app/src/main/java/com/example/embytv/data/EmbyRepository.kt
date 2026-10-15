package com.example.embytv.data

import com.example.embytv.data.api.*
import com.example.embytv.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class EmbyRepository @Inject constructor(
    private val apiService: EmbyApiService
) {

    var serverUrl: String? = null
        private set

    suspend fun connect(url: String): SystemInfo? = withContext(Dispatchers.IO) {
        try {
            serverUrl = url.removeSuffix("/") + "/"
            apiService.getSystemPublicInfo()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun login(username: String, password: String): AuthenticationResult? = withContext(Dispatchers.IO) {
        try {
            val result = apiService.authenticate(username, password)
            if (result.accessToken != null) {
                _accessToken = result.accessToken
                _userId = result.user?.id
            }
            result
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserLibrary(userId: String): ResultPage<MediaItem> = withContext(Dispatchers.IO) {
        apiService.getUserItems(userId)
    }

    suspend fun getContinueWatching(userId: String): ResultPage<MediaItem> = withContext(Dispatchers.IO) {
        try {
            apiService.getUserItems(
                userId = userId,
                includeTypes = "Movie,Series",
                sortBy = "DatePlayed"
            )
        } catch (e: Exception) {
            ResultPage.empty()
        }
    }

    suspend fun searchItems(userId: String, searchTerm: String): ResultPage<MediaItem> = withContext(Dispatchers.IO) {
        try {
            // Emby full-text search requires /Search/Term endpoint
            ResultPage(emptyList(), 0, 0)
        } catch (e: Exception) {
            ResultPage.empty()
        }
    }

    suspend fun getItemDetails(itemId: String): MediaItem? = withContext(Dispatchers.IO) {
        try {
            val userId = _userId ?: return@withContext null
            apiService.getItemDetails(userId, itemId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPlaybackInfo(itemId: String, positionTicks: Long = 0): PlaybackResponse? = withContext(Dispatchers.IO) {
        try {
            val userId = _userId ?: return@withContext null
            val request = PlaybackRequest(
                userId = userId,
                mediaSourceId = null,
                audioStreamIndex = null,
                subtitleStreamIndex = -1,
                startTimeTicks = positionTicks,
                maxStreamingBitrate = 150000000,
                deviceProfile = SessionProfile()
            )
            apiService.getPlaybackInfo(itemId, request)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getLatestItems(userId: String, limit: Int = 12): List<MediaItem> = withContext(Dispatchers.IO) {
        try {
            apiService.getLatestItems(userId, limit = limit)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Get episode list for a season. */
    suspend fun getSeasonEpisodes(seriesId: String, userId: String, seasonId: String? = null): ResultPage<MediaItem> = withContext(Dispatchers.IO) {
        try {
            apiService.getSeasonEpisodes(seriesId, userId, seasonId = seasonId)
        } catch (e: Exception) {
            ResultPage.empty()
        }
    }

    /** Build the public-image URL for a poster/backdrop. */
    companion object {
        fun buildImageUrl(serverBaseUrl: String, itemId: String, imageType: String = "Primary", quality: Int = 90): String {
            return "${serverBaseUrl}Users/Items/$itemId/Images/$imageType?quality=$quality&tag=${UUID.randomUUID().toString().take(8)}"
        }
    }

    private var _accessToken: String? = null
    private var _userId: String? = null

    val accessToken: String? get() = _accessToken
    val userId: String? get() = _userId
}
