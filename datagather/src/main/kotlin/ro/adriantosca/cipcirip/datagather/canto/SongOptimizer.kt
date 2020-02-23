package ro.adriantosca.cipcirip.datagather.canto

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Paths


class SongOptimizer {
    fun optimize(fromDirectoryPath: String, toDirectoryPath: String) {
        File(toDirectoryPath).mkdirs()
        File(fromDirectoryPath).walk()
            .filter { it.extension == "mp3" }
            .forEach {
                print("${it.name}...")

                val outFileA = Paths.get(toDirectoryPath, "A_${it.name}").toFile()
                val removenoise = "ffmpeg -i ${it.absolutePath} -af afftdn ${outFileA.absolutePath}"
                exec(removenoise)

                val outFileB = Paths.get(toDirectoryPath, "B_${it.name}").toFile()
                val silenceremove = "ffmpeg -i ${outFileA.absolutePath} -acodec copy -vf silenceremove=start_periods=1:start_duration=1:start_threshold=30db ${outFileB.absolutePath}"
                exec(silenceremove)

                val outFileC = Paths.get(toDirectoryPath, "C_${it.name}").toFile()
                val cut = "ffmpeg -t 15 -i ${outFileB.absolutePath} -acodec copy ${outFileC.absolutePath}"
                exec(cut)

                val outFile = Paths.get(toDirectoryPath, it.name).toFile()
                val reencode = "ffmpeg -i ${outFileC.absolutePath} -codec:a libmp3lame -qscale:a 9 -y ${outFile.absolutePath}"
                exec(reencode)

                File(outFileA.absolutePath).delete()
                File(outFileB.absolutePath).delete()
                File(outFileC.absolutePath).delete()

                println("OK")
            }
    }

    private fun exec(task: String) {
        val pb = ProcessBuilder(
            task.split(" ")
        )
        pb.redirectErrorStream(true)
        val process = pb.start()
        BufferedReader(InputStreamReader(process.inputStream)).useLines {
            it.forEach {
                println(it)
            }
        }
        process.waitFor()
    }
}
