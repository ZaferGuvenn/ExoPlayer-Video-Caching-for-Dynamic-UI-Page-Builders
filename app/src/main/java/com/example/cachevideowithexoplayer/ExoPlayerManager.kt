package com.example.cachevideowithexoplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.cachevideowithexoplayer.model.BaseModel
import com.siro.android.ui.adapter.viewholder.HomeTopBannerViewHolder
import java.io.File

@UnstableApi
object ExoPlayerManager {
    private val players: MutableList<ExoPlayer> = mutableListOf()
    private val playersTemp: MutableList<ExoplayerModelClass> = mutableListOf()
    private var downloadCache: Cache? = null
    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"

    fun initializePlayer(context: Context, position: Int): ExoPlayer {
        val player = ExoPlayer.Builder(context).build()
        player.setHandleAudioBecomingNoisy(true)
        players.add(player) // Add player to the list when initialized
        playersTemp.add(ExoplayerModelClass(player, position)) // Add player model to temp list
        return player
    }

    private fun createNewPlayer(context: Context): ExoPlayer {
        val player = ExoPlayer.Builder(context).build()
        player.setHandleAudioBecomingNoisy(true)
        return player
    }

    fun setMediaItem(
        exoPlayer: ExoPlayer,
        videoUri: String,
        playbackPosition: Long = 0L,
        playbackStateListener: Player.Listener
    ) {
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource =
            ProgressiveMediaSource.Factory(buildCacheDataSourceFactory())
                .createMediaSource(mediaItem)
        exoPlayer.setMediaSource(mediaSource, playbackPosition)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.playWhenReady = true
        exoPlayer.addListener(playbackStateListener)
        exoPlayer.prepare()
        //    exoPlayer.play()
    }

    fun releasePlayer(
        holder: HomeTopBannerViewHolder,
        listItems: BaseModel
    ) {
        val player = holder.exoPlayer
        player?.removeListener(holder.playbackStateListener())
        listItems.playbackPosition = player?.currentPosition ?: 0L
        player?.stop()
        player?.clearMediaItems()
        if (player != null) {
            players.remove(player)
            playersTemp.remove(ExoplayerModelClass(player, holder.bindingAdapterPosition))
        }
    }


    fun releaseAllPlayers() {
        for (player in players) {
            player.stop()
            player.release()
        }
        players.clear()
        playersTemp.clear()

    }

    fun getRunningPlayers():ArrayList<ExoPlayer>{
        return arrayListOf<ExoPlayer>().also {
            players.forEach {player ->
                if(player.isPlaying || player.playWhenReady){
                    it.add(player)
                }
            }
        }
    }

    fun pauseAllPlayers() {
        for (player in playersTemp) {
            player.exoplayer.playWhenReady = false
            player.exoplayer.pause()
        }
    }

    fun pauseAllPlayer(exoPlayer: ExoPlayer?) {
        for (player in playersTemp) {
            if (exoPlayer == player.exoplayer) {

            } else {
                player.exoplayer.playWhenReady = false
                player.exoplayer.pause()
            }
        }
    }

    fun playExoPlayer(exoPlayer: ExoPlayer?) {
        for (player in playersTemp) {
            if (exoPlayer == player.exoplayer) {
                exoPlayer?.playWhenReady = true
            } else {
                player.exoplayer.playWhenReady = false
                player.exoplayer.pause()
            }
        }
    }

    fun playNextOrPreviousVideo(
        exoPlayer: ExoPlayer?,
        bindingAdapterPosition: Int,
        scrollUp: Boolean
    ) {
        if (playersTemp.size > 1) {
            playersTemp.sortBy { it.position }
            val index = playersTemp.indexOf(exoPlayer?.let {
                ExoplayerModelClass(
                    it,
                    bindingAdapterPosition
                )
            })
            if (scrollUp) {
                if (index != 0) {
                    playersTemp[index - 1].exoplayer.playWhenReady = true
                    playersTemp[index - 1].exoplayer.play()
                }
            } else {
                if (index != playersTemp.lastIndex) {
                    playersTemp[index + 1].exoplayer.playWhenReady = true
                    playersTemp[index + 1].exoplayer.play()
                }
            }
        }
    }

    fun buildCacheDataSourceFactory(): DataSource.Factory {
        val cache = getDownloadCache()
        val cacheSink = CacheDataSink.Factory()
            .setCache(cache)
        val upstreamFactory =
            DefaultDataSource.Factory(getAppContext(), DefaultHttpDataSource.Factory())
        return CacheDataSource.Factory()
            .setCache(cache)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    @Synchronized
    private fun getDownloadCache(): Cache {
        if (downloadCache == null) {
            val downloadContentDirectory = File(
                getAppContext().cacheDir,
                DOWNLOAD_CONTENT_DIRECTORY
            )
            downloadCache =
                SimpleCache(
                    downloadContentDirectory, NoOpCacheEvictor(), StandaloneDatabaseProvider(
                        getAppContext()
                    )
                )
        }
        return downloadCache!!
    }
}

