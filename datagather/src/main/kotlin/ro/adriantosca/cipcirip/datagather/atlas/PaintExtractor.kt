package ro.adriantosca.cipcirip.datagather.atlas

import org.apache.pdfbox.pdmodel.PDDocument
import ro.adriantosca.cipcirip.datagather.BirdInfo
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

class PaintExtractor {
    fun extract(document: PDDocument, birdInfoList: List<BirdInfo>, directoryPath: String,
                skipNotExisting: Boolean) {
        birdInfoList.forEach { birdInfo ->
            extract(document, birdInfo, directoryPath, skipNotExisting)
        }
    }

    private fun extract(document: PDDocument, birdInfo: BirdInfo, directoryPath: String,
                        skipNotExisting: Boolean) {
        val file = File("$directoryPath/${birdInfo.code}.jpg")
        if (!file.exists()) {
            if (skipNotExisting) {
                println("Does not exists ${file.path}, but skipping.")
            } else {
                val pageImage = PageImageExtractor().extract(document, birdInfo)

                val birdImage = extractBirdPaintImage(pageImage)

                applyFixes(birdInfo.code, birdImage)

                Paths.get(directoryPath).toFile().mkdirs()
                ImageIO.write(birdImage, "jpg", file)
//            paintDocument.save(File("/Users/at/Projects/CipCirip/datagather/data/${birdInfo.code}.pdf"))
                println("Extracted ${file.path}.")
            }
        } else {
            println("Exists ${file.path}, skipping.")
        }
    }

    private fun applyFixes(code: String, image: BufferedImage) {
        val graphics2D = image.createGraphics()
        graphics2D.color = Color.WHITE
        when (code) {
            "anser_erythropus" -> graphics2D.fillRect(0, 0, 785, 215)
            "pluvialis_apricaria" -> graphics2D.fillRect(1128, 0, image.width - 1128 - 1, 621)
            "accipiter_gentilis" -> graphics2D.fillRect(1172, 354, 1172, 1118 - 354)
        }
    }

    private fun extractBirdPaintImage(pageImage: BufferedImage): BufferedImage {
        removeBottomRightPageRect(pageImage)

        val hasMap = hasMap(pageImage)
        if (hasMap) {
            if (checkLineIsColor(pageImage, 1815, Color.WHITE)) {
                removeBottom(pageImage, 1800)
            } else {
                val cleanedLeft = cleanMapLeft(pageImage)
                val cleanedRight = cleanMapRight(pageImage)
                if (cleanedLeft || cleanedRight) {
                    removeBottom(pageImage, 2100)
                }
            }
        }

        val trimmed = trimImage(pageImage)

        val squareImage = makeSquaredImage(trimmed)

        val resized = if (512 < squareImage.width)
            resizeImage(squareImage, 512, 512)
        else
            squareImage

        return resized
    }

    private fun resizeImage(original: BufferedImage, newWidth: Int, newHeight: Int): BufferedImage {
        val resized = BufferedImage(newWidth, newHeight, original.type)
        val g = resized.createGraphics()
        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        )
        g.drawImage(
            original, 0, 0, newWidth, newHeight, 0, 0, original.width,
            original.height, null
        )
        g.dispose()
        return resized
    }

    private fun hasMap(pageImage: BufferedImage): Boolean {
        val x1 = findXNonWhite(pageImage, 2000)
        val x2 = findXNonWhite(pageImage, 2300)
        if (x1 != x2) return false
        val x3 = findXNonWhite(pageImage, 2700)
        if (x2 != x3) return false
        val x4 = findXNonWhite(pageImage, 3000)
        if (x3 != x4) return false
        return true
    }

    private fun makeSquaredImage(trimmed: BufferedImage): BufferedImage {
        val biggerSize = if (trimmed.width < trimmed.height) trimmed.height else trimmed.width
        val squareImage = BufferedImage(biggerSize, biggerSize, TYPE_INT_RGB)
        val graphics2D = squareImage.createGraphics()
        graphics2D.color = Color.white
        graphics2D.fillRect(0, 0, biggerSize, biggerSize)
        graphics2D.drawImage(trimmed, null, (biggerSize - trimmed.width) / 2, (biggerSize - trimmed.height) / 2)
        return squareImage
    }

    private fun removeBottomRightPageRect(subImage: BufferedImage) {
        val graphicsRemove = subImage.createGraphics()
        graphicsRemove.color = Color.white
        graphicsRemove.fillRect(2328, 3356, subImage.width - 2328, subImage.height - 3356)
    }

    private fun removeBottom(image: BufferedImage, fromY: Int) {
        val graphics2D = image.createGraphics()
        graphics2D.color = Color.white
        graphics2D.fillRect(0, fromY, image.width - 1, image.height - fromY - 1)
    }

    private fun cleanMapLeft(image: BufferedImage): Boolean {
        var mapX = -1
        var color = image.getRGB(50, 1870)
        for (x in 50 until image.width) {
            val rgb = image.getRGB(x, 1870)
            if (color != rgb) {
                mapX = x
                color = rgb
                break
            }
        }
        if (mapX == -1) return false
        var mapY = -1
        for (y in 1870 downTo 1770) {
            val rgb = image.getRGB(mapX, y)
            if (color != rgb) {
                mapY = y
                break
            }
        }
        if (mapY == -1) return false

        var cutX = -1
        for (x in mapX until 2260) {
            val rgb = image.getRGB(x, mapY)
            if (colorWithinTolerance(rgb, Color.WHITE.rgb, 0.01)) {
                cutX = x
                break
            }
        }

        if (cutX != -1) {
            val graphics2D = image.createGraphics()
            graphics2D.color = Color.white

            graphics2D.fillRect(0, mapY, cutX, image.height - mapY - 1)

            return true
        }

        return false
    }

    private fun cleanMapRight(image: BufferedImage): Boolean {
        var mapX = -1
        var color = image.getRGB(2330, 1870)
        for (x in 2330 downTo 150) {
            val rgb = image.getRGB(x, 1870)
            if (color != rgb) {
                mapX = x
                color = rgb
                break
            }
        }
        if (mapX == -1) return false
        var mapY = -1
        for (y in 1870 downTo 1770) {
            val rgb = image.getRGB(mapX, y)
            if (color != rgb) {
                mapY = y
                break
            }
        }
        if (mapY == -1) return false

        var cutX = -1
        for (x in mapX downTo 150) {
            val rgb = image.getRGB(x, mapY)
            if (colorWithinTolerance(rgb, Color.WHITE.rgb, 0.01)) {
                cutX = x
                break
            }
        }

        if (cutX != -1) {
            val graphics2D = image.createGraphics()
            graphics2D.color = Color.white

            graphics2D.fillRect(cutX, mapY, image.width - 1, image.height - mapY - 1)

            return true
        }

        return false
    }

    private fun colorWithinTolerance(a: Int, b: Int, tolerance: Double): Boolean {
        val aRed = (a and 0x00FF0000).ushr(16)   // Red level
        val aGreen = (a and 0x0000FF00).ushr(8)    // Green level
        val aBlue = a and 0x000000FF            // Blue level

        val bRed = (b and 0x00FF0000).ushr(16)   // Red level
        val bGreen = (b and 0x0000FF00).ushr(8)    // Green level
        val bBlue = b and 0x000000FF            // Blue level

        val distance = Math.sqrt(
            ((aRed - bRed) * (aRed - bRed) +
                    (aGreen - bGreen) * (aGreen - bGreen) +
                    (aBlue - bBlue) * (aBlue - bBlue)).toDouble()
        )

        // 510.0 is the maximum distance between two colors
        // (0,0,0,0 -> 255,255,255,255)
        val percentAway = distance / 510.0

        return percentAway < tolerance
    }

//    private fun cleanMap(mapCorner: Pair<Int, Int>, image: BufferedImage) {
//        val mcX = mapCorner.first
//        val mcY = mapCorner.second
//        var cutXLeft = -1
//        for (x in mcX until 2260) {
//            val rgb = image.getRGB(x, mcY)
//            if (rgb == Color.WHITE.rgb) {
//                cutXLeft = x
//                break
//            }
//        }
//        var cutXRight = image.width - 1
//        for (x in image.width -1 downTo 150) {
//            val rgb = image.getRGB(x, mcY)
//            if (rgb == Color.WHITE.rgb) {
//                cutXRight = x
//                break
//            }
//        }
//        val graphics2D = image.createGraphics()
//        graphics2D.color = Color.white
//
//        if (cutXLeft != -1) {
//            graphics2D.fillRect(0, mcY, cutXLeft, image.height - mcY)
//        }
//
//        if (cutXRight != -1) {
//            graphics2D.fillRect(cutXRight, mcY, image.width - 1, image.height - mcY)
//        }
//
//        if (cutXLeft == -1 && cutXRight == -1) {
//            graphics2D.fillRect(0, mcY, image.width, image.height - mcY)
//        }
//    }

    private fun findXNonWhite(image: BufferedImage, y: Int): Int {
        for (x in 0 until image.width) {
            val rgb = image.getRGB(x, y)
            if (!colorWithinTolerance(rgb, Color.WHITE.rgb, 0.01)) {
                return x
            }
        }
        return -1
    }

    private fun checkLineIsColor(image: BufferedImage, y: Int, color: Color): Boolean {
        for (x in 0 until image.width) {
            val rgb = image.getRGB(x, y)
            if (!colorWithinTolerance(rgb, color.rgb, 0.01)) {
                return false
            }
        }
        return true
    }

    private fun trimImage(image: BufferedImage): BufferedImage {
        val width = image.width
        val height = image.height
        var left = 0
        var top = 0
        var right = width - 1
        var bottom = height - 1
        var minRight = width - 1
        var minBottom = height - 1

        top@ while (top < bottom) {
            for (x in 0 until width) {
                if (!colorWithinTolerance(image.getRGB(x, top), Color.WHITE.rgb, 0.01)) {
                    minRight = x
                    minBottom = top
                    break@top
                }
            }
            top++
        }

        left@ while (left < minRight) {
            for (y in height - 1 downTo top + 1) {
                if (!colorWithinTolerance(image.getRGB(left, y), Color.WHITE.rgb, 0.01)) {
                    minBottom = y
                    break@left
                }
            }
            left++
        }

        bottom@ while (bottom > minBottom) {
            for (x in width - 1 downTo left) {
                if (!colorWithinTolerance(image.getRGB(x, bottom), Color.WHITE.rgb, 0.01)) {
                    minRight = x
                    break@bottom
                }
            }
            bottom--
        }

        right@ while (right > minRight) {
            for (y in bottom downTo top) {
                if (!colorWithinTolerance(image.getRGB(right, y), Color.WHITE.rgb, 0.01)) {
                    break@right
                }
            }
            right--
        }

        return image.getSubimage(left, top, right - left + 1, bottom - top + 1)
    }
}
