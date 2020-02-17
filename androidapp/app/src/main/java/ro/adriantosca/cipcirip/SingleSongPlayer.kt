package ro.adriantosca.cipcirip

import android.app.Application
import android.media.MediaPlayer
import mu.KotlinLogging
import org.koin.core.KoinComponent
import ro.adriantosca.cipcirip.model.Organism
import java.io.FileNotFoundException

private val logger = KotlinLogging.logger {}

class SingleSongPlayer(private val mApplication: Application): KoinComponent {

    private val mediaPlayer = MediaPlayer()

    private var mOnStoppedHandler: SongPlayHandler? = null
    private var mOnStartedHandler: SongPlayHandler? = null
    var currentPlayingOrganism: Organism? = null
        private set

    fun play(organism: Organism, onStartedListener: SongPlayHandler, onStoppedListener: SongPlayHandler) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mOnStoppedHandler?.let { it() }
        mOnStoppedHandler = onStoppedListener
        mOnStartedHandler = onStartedListener
        try {
            mediaPlayer.reset()
            mApplication.assets.openFd("media/songs/${organism.code}.mp3").use {
                mediaPlayer.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            mediaPlayer.setOnCompletionListener {
                mOnStoppedHandler?.let { it() }
                currentPlayingOrganism = null
            }
            mediaPlayer.setOnPreparedListener {
                it.start()
                mOnStartedHandler?.let { it() }
                currentPlayingOrganism = organism
            }
            mediaPlayer.prepareAsync()
        } catch (ex: FileNotFoundException) {
            logger.error { ex }
        }
    }

    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mOnStoppedHandler?.let { it() }
        currentPlayingOrganism = null
    }
}

typealias SongPlayHandler = () -> Unit