package ro.adriantosca.cipcirip.datagather.atlas

import org.apache.pdfbox.contentstream.PDContentStream
import org.apache.pdfbox.contentstream.operator.Operator
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdfparser.PDFStreamParser
import org.apache.pdfbox.pdfwriter.ContentStreamWriter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageTree
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.common.PDStream
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import ro.adriantosca.cipcirip.datagather.BirdInfo
import java.awt.image.BufferedImage
import java.io.IOException
import java.util.ArrayList

class PageImageExtractor {
    fun extract(document: PDDocument, birdInfo: BirdInfo): BufferedImage {
        val pages = document.pages
        PDDocument().use { paintDocument ->
            // pageNumber is 1-based, nextPageIndex is 0-based, want the next page after  pageNumber
            val pageImage = extractPageImage(birdInfo, paintDocument, pages)
            return pageImage
        }
    }
    private fun extractPageImage(
        birdInfo: BirdInfo,
        paintDocument: PDDocument,
        pages: PDPageTree
    ): BufferedImage {
        val nextPageIndex = birdInfo.pageNumber
        val paintPage = paintDocument.importPage(pages[nextPageIndex])
        val newTokens = createTokensWithoutText(paintPage)
        val newContents = PDStream(paintDocument)
        writeTokensToStream(newContents, newTokens)
        paintPage.setContents(newContents)
        processResources(paintPage.resources)
        val pdfRenderer = PDFRenderer(paintDocument)
        val pageImage = pdfRenderer.renderImageWithDPI(0, 300f, ImageType.RGB)
        return pageImage
    }

    @Throws(IOException::class)
    private fun processResources(resources: PDResources) {
        for (name in resources.xObjectNames) {
            val xobject = resources.getXObject(name)
            if (xobject is PDFormXObject) {
                writeTokensToStream(
                    xobject.contentStream,
                    createTokensWithoutText(xobject)
                )
                processResources(xobject.resources)
            }
        }
        for (name in resources.patternNames) {
            val pattern = resources.getPattern(name)
            if (pattern is PDTilingPattern) {
                writeTokensToStream(
                    pattern.contentStream,
                    createTokensWithoutText(pattern)
                )
                processResources(pattern.resources)
            }
        }
    }

    @Throws(IOException::class)
    private fun writeTokensToStream(newContents: PDStream, newTokens: List<Any>) {
        newContents.createOutputStream(COSName.FLATE_DECODE).use { out ->
            val writer = ContentStreamWriter(out)
            writer.writeTokens(newTokens)
        }
    }

    @Throws(IOException::class)
    private fun createTokensWithoutText(contentStream: PDContentStream): List<Any> {
        val parser = PDFStreamParser(contentStream)
        var token: Any? = parser.parseNextToken()
        val newTokens = ArrayList<Any>()
        while (token != null) {
            if (token is Operator) {
                val op = token as Operator?
                if (!("TJ" != op!!.name && "Tj" != op.name && "'" != op.name)
                ) {
                    // remove the argument to this operator
                    newTokens.removeAt(newTokens.size - 1)

                    token = parser.parseNextToken()
                    continue
                } else if ("\"" == op.name) {
                    // remove the 3 arguments to this operator
                    newTokens.removeAt(newTokens.size - 1)
                    newTokens.removeAt(newTokens.size - 1)
                    newTokens.removeAt(newTokens.size - 1)

                    token = parser.parseNextToken()
                    continue
                }
            }
            newTokens.add(token)
            token = parser.parseNextToken()
        }
        return newTokens
    }
}
