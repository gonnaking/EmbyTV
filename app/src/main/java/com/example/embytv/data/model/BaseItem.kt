package com.example.embytv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Unified media item representation mapping to Emby's BaseItem structure.
 */
@Serializable
data class MediaItem(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Type") val type: String, // Movie | Series | Episode | Season
    @SerialName("PrimaryImageTag") val primaryImageTag: String? = null,
    @SerialName("IndexNumber") val indexNumber: Int? = null,
    @SerialName("ParentIndexNumber") val parentIndexNumber: Int? = null,
    @SerialName("PremiereDate") val premiereDate: String? = null,
    @SerialName("Overview") val overview: String? = null,
    @SerialName("People") val people: List<BasePerson>? = null,
    @SerialName("Genres") val genres: List<String>? = null,
    @SerialName("ProductionYear") val productionYear: Int? = null,
    @SerialName("CommunityRating") val communityRating: Double? = null,
    @SerialName("RunTimeTicks") val runTimeTicks: Long? = null,
    @SerialName("LocationType") val locationType: String? = null,
    @SerialName("Path") val path: String? = null,
    @SerialName("Container") val container: String? = null,
    @SerialName("SeriesName") val seriesName: String? = null,
    @SerialName("SeasonId") val seasonId: String? = null,
    @SerialName("SeriesId") val seriesId: String? = null,
    @SerialName("BackdropImageTags") val backdropImageTags: List<String>? = null,
    @SerialName("LogoImageTag") val logoImageTag: String? = null,
    @SerialName("ImageBlurHashes") val imageBlurHashes: ImageBlurHashes? = null,
    @SerialName("UserData") val userData: UserData? = null,
    @SerialName("StartDate") val startDate: String? = null,
    @SerialName("EndDate") val endDate: String? = null,
    @SerialName("AlbumId") val albumId: String? = null,
    @SerialName("Studio") val studio: String? = null,
    @SerialName("Studios") val studios: List<Studio>? = null,
    @SerialName("ChannelId") val channelId: String? = null,
    @SerialName("ChannelName") val channelName: String? = null,
    @SerialName("ExternalUrls") val externalUrls: List<ExternalUrl>? = null,
    @SerialName("SupportedCommands") val supportedCommands: List<String>? = null,
    @SerialName("IsFolder") val isFolder: Boolean = false,
    @SerialName("ParentId") val parentId: String? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("Taglines") val taglines: List<String>? = null,
    @SerialName("OfficialRating") val officialRating: String? = null,
    @SerialName("CanDelete") val canDelete: Boolean = false,
    @SerialName("CanDownload") val canDownload: Boolean = false,
    @SerialName("HasSubtitles") val hasSubtitles: Boolean = false,
    @SerialName("MediaSources") val mediaSources: List<MediaSource>? = null,
    @SerialName("Chapters") val chapters: List<Chapter>? = null
) {
    val isVideo: Boolean get() = listOf("Movie", "Episode", "Series").contains(type)
    val isSeries: Boolean get() = type == "Series"
    val isMovie: Boolean get() = type == "Movie"
    val isEpisode: Boolean get() = type == "Episode"
    val isSeason: Boolean get() = type == "Season"
}

@Serializable
data class Studio(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String
)

@Serializable
data class ExternalUrl(
    @SerialName("Name") val name: String,
    @SerialName("Url") val url: String
)

@Serializable
data class Chapter(
    @SerialName("StartPositionTicks") val startPositionTicks: Long,
    @SerialName("Name") val name: String? = null
)

@Serializable
data class BasePerson(
    @SerialName("Name") val name: String,
    @SerialName("Role") val role: String? = null,
    @SerialName("Type") val type: String? = null,
    @SerialName("PrimaryImageTag") val primaryImageTag: String? = null,
    @SerialName("Id") val id: String? = null
)

@Serializable
data class UserData(
    @SerialName("ItemId") val itemId: String? = null,
    @SerialName("PlayState") val playState: PlayState? = null,
    @SerialName("LastPlayedDate") val lastPlayedDate: String? = null,
    @SerialName("PlayCount") val playCount: Int? = null,
    @SerialName("Played") val played: Boolean? = null,
    @SerialName("PlaybackPositionTicks") val playbackPositionTicks: Long? = null,
    @SerialName("AudioStreamIndex") val audioStreamIndex: Int? = null,
    @SerialName("SubtitleStreamIndex") val subtitleStreamIndex: Int? = null,
    @SerialName("Likes") val likes: Boolean? = null,
    @SerialName("UnplayedItemCount") val unplayedItemCount: Int? = null
)

@Serializable
data class PlayState(
    @SerialName("PositionTicks") val positionTicks: Long? = null,
    @SerialName("IsPaused") val isPaused: Boolean? = null,
    @SerialName("IsMuted") val isMuted: Boolean? = null,
    @SerialName("PlayedPercentage") val playedPercentage: Double? = null
)

@Serializable
data class ResultPage<T>(
    val items: List<T>,
    val totalRecordCount: Int,
    val startIndex: Int
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> empty(): ResultPage<T> = ResultPage(emptyList(), 0, 0)
    }
}

@Serializable
data class UserDto(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("HasPassword") val hasPassword: Boolean = false,
    @SerialName("HasConfiguredPassword") val hasConfiguredPassword: Boolean = false,
    @SerialName("Policy") val policy: UserPolicy? = null
)

@Serializable
data class UserPolicy(
    @SerialName("IsAdministrator") val isAdmin: Boolean = false,
    @SerialName("IsHidden") val isHidden: Boolean = false,
    @SerialName("IsDisabled") val isDisabled: Boolean = false,
    @SerialName("BlacklistPlaylists") val blacklistPlaylists: List<String>? = null,
    @SerialName("BlacklistMediaFolders") val blacklistMediaFolders: List<String>? = null
)

@Serializable
data class SystemInfo(
    @SerialName("ServerName") val serverName: String? = null,
    @SerialName("Version") val version: String? = null,
    @SerialName("LocalAddress") val localAddress: String? = null,
    @SerialName("RemoteAddress") val remoteAddress: String? = null,
    @SerialName("IdleTimeoutMilliseconds") val idleTimeoutMs: Int? = null,
    @SerialName("ConfigurationPaths") val configurationPaths: List<String>? = null,
    @SerialName("WebDashboardVersion") val webDashboardVersion: String? = null,
    @SerialName("StartupWizardCompleted") val startupWizardCompleted: Boolean? = null,
    @SerialName("MinRequiredServerVersion") val minRequiredServerVersion: String? = null,
    @SerialName("Id") val serverId: String? = null,
    @SerialName("OperatingSystem") val operatingSystem: String? = null,
    @SerialName("WanLan") val wanLan: String? = null,
    @SerialName("SupportsSimplePlayback") val supportsSimplePlayback: Boolean? = null,
    @SerialName("TimeZone") val timeZone: String? = null
)

@Serializable
data class AuthenticationResult(
    @SerialName("AccessToken") val accessToken: String? = null,
    @SerialName("User") val user: UserDto? = null,
    @SerialName("ServerId") val serverId: String? = null,
    @SerialName("HasPendingPasswordSetup") val hasPendingPasswordSetup: Boolean? = null,
    @SerialName("SecretKey") val secretKey: String? = null,
    @SerialName("UserConfiguration") val userConfiguration: UserConfiguration? = null
)

@Serializable
data class UserConfiguration(
    @SerialName("SubtitlesStyleSettings") val subtitlesStyleSettings: SubtitleStyle? = null,
    @SerialName("PlayMethodDefaults") val playMethodDefaults: PlayMethodDefaults? = null
)

@Serializable
data class SubtitleStyle(
    @SerialName("FontFamily") val fontFamily: String? = null,
    @SerialName("FontSizePercent") val fontSizePercent: Int? = null,
    @SerialName("VerticalOffset") val verticalOffset: Int? = null
)

@Serializable
data class PlayMethodDefaults(
    @SerialName("MaxAudioChannels") val maxAudioChannels: String? = null,
    @SerialName("TranscodingMaxAudioChannels") val transcodingMaxAudioChannels: String? = null
)

@Serializable
data class MediaSource(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String? = null,
    @SerialName("Protocol") val protocol: String = "Http",
    @SerialName("Path") val path: String? = null,
    @SerialName("RunTimeTicks") val runTimeTicks: Long? = null,
    @SerialName("RequiresOpening") val requiresOpening: Boolean = false,
    @SerialName("OpeningTimestampTicks") val openingTimestampTicks: Long? = null,
    @SerialName("RequiresClosing") val requiresClosing: Boolean = false,
    @SerialName("RequiresLooping") val requiresLooping: Boolean = false,
    @SerialName("SupportsTranscoding") val supportsTranscoding: Boolean = true,
    @SerialName("SupportsDirectPlay") val supportsDirectPlay: Boolean = false,
    @SerialName("SupportsDirectStream") val supportsDirectStream: Boolean = false,
    @SerialName("SupportsCopy") val supportsCopy: Boolean = true,
    @SerialName("Container") val container: String? = null,
    @SerialName("Size") val size: Long? = null,
    @SerialName("Bitrate") val bitrate: Int? = null,
    @SerialName("EstimatedContentLength") val estimatedContentLength: Long? = null,
    @SerialName("OptimalBitrate") val optimalBitrate: Int? = null,
    @SerialName("Duration") val duration: String? = null,
    @SerialName("MediaStreams") val mediaStreams: List<MediaStream>? = null,
    @SerialName("CodecProfiles") val codecProfiles: List<CodecProfile>? = null,
    @SerialName("AnalyzeVideoDurration") val analyzeVideoDuration: Boolean = true,
    @SerialName("DefaultAudioStreamIndex") val defaultAudioStreamIndex: Int? = null,
    @SerialName("Subtitles") val subtitles: List<SubtitleTrack>? = null
)

@Serializable
data class SubtitleTrack(
    @SerialName("Format") val format: String,
    @SerialName("Path") val path: String,
    @SerialName("Language") val language: String? = null,
    @SerialName("Name") val name: String? = null,
    @SerialName("IsDefault") val isDefault: Boolean? = null,
    @SerialName("IsForced") val isForced: Boolean? = null
)

@Serializable
data class MediaStream(
    @SerialName("Index") val index: Int = 0,
    @SerialName("Codec") val codec: String? = null,
    @SerialName("CodecType") val codecType: String? = null,
    @SerialName("DisplayTitle") val displayTitle: String? = null,
    @SerialName("IsInterlaced") val isInterlaced: Boolean? = null,
    @SerialName("IsAVC") val isAvc: Boolean? = null,
    @SerialName("Language") val language: String? = null,
    @SerialName("Profile") val profile: String? = null,
    @SerialName("IsDefault") val isDefault: Boolean? = null,
    @SerialName("IsForced") val isForced: Boolean? = null,
    @SerialName("Height") val height: Int? = null,
    @SerialName("Width") val width: Int? = null,
    @SerialName("AverageFrameRate") val averageFrameRate: Float? = null,
    @SerialName("PixelFormat") val pixelFormat: String? = null,
    @SerialName("BitRate") val bitRate: Int? = null,
    @SerialName("Channels") val channels: Int? = null,
    @SerialName("SampleRate") val sampleRate: Int? = null
) {
    val isVideo: Boolean get() = codecType == "video"
    val isAudio: Boolean get() = codecType == "audio"
    val isSubtitle: Boolean get() = codecType == "subtitle"
}

@Serializable
data class CodecProfile(
    @SerialName("Type") val type: String,
    @SerialName("Codec") val codec: String? = null,
    @SerialName("ApplyConditions") val applyConditions: List<String>? = null
)

@Serializable
data class PlaybackRequest(
    @SerialName("UserId") val userId: String,
    @SerialName("MediaSourceId") val mediaSourceId: String? = null,
    @SerialName("AudioStreamIndex") val audioStreamIndex: Int? = null,
    @SerialName("SubtitleStreamIndex") val subtitleStreamIndex: Int? = null,
    @SerialName("MaxStreamingBitrate") val maxStreamingBitrate: Int = 120000000,
    @SerialName("StartTimeTicks") val startTimeTicks: Long = 0,
    @SerialName("IsDynamic") val isDynamic: Boolean = true,
    @SerialName("DeviceProfile") val deviceProfile: SessionProfile? = null
)

@Serializable
data class PlaybackResponse(
    @SerialName("MediaSources") val mediaSources: List<MediaSource?>?,
    @SerialName("PlaySessionId") val playSessionId: String? = null,
    @SerialName("JobCreationProgress") val jobCreationProgress: Double? = null,
    @SerialName("JobCreationElapsedSeconds") val jobCreationElapsedSeconds: Double? = null
)

@Serializable
data class SessionProfile(
    val tagName: String = "emby-server-remote",
    val name: String = "Emby Server Remote",
    val maxStaticBitrate: Int = 120000000,
    val maxStreamingBitrate: Int = 120000000,
    val minutesToLive: Long? = null,
    val deviceId: String = "embytv_android",
    val deviceName: String = "EmbyTV Android",
    val clientVersion: String = "1.0.0",
    var productVersionString: String? = null,
    val encodingProperties: Map<String, Any>? = null
)

@Serializable
data class ImageBlurHashes(
    val poster: Map<String, String>? = null,
    val thumb: Map<String, String>? = null,
    val banner: Map<String, String>? = null,
    val logo: Map<String, String>? = null,
    val clearArt: Map<String, String>? = null,
    val disc: Map<String, String>? = null,
    val box: Map<String, String>? = null,
    val screenshot: Map<String, String>? = null,
    val menu: Map<String, String>? = null,
    val alpha: Map<String, String>? = null
)

