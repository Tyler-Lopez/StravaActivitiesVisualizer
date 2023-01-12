package com.activityartapp.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.activityartapp.domain.FileRepository
import com.activityartapp.util.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val context: Context
) : FileRepository {

    override suspend fun saveBitmapToGallery(bitmap: Bitmap): Response<Unit> {
        return try {
            val fileName = System.currentTimeMillis().toString() + ".png"
            val resolver = context.contentResolver

            val uri: Uri? = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                println("Here saving to gallery when Build is > Q")
                /**
                 * https://stackoverflow.com/questions/8560501/android-save-image-into-gallery
                 * https://stackoverflow.com/questions/57726896/mediastore-images-media-insertimage-deprecated
                 */
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    initializeContentValues(fileName).apply {
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            DIRECTORY_DOWNLOADS
                        )
                    }
                )
            } else {
                println("Here saving to gallery when Build is < Q")
                val file = File(
                    Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
                    fileName
                )
                /** MediaStore.Images.Media.DATA is deprecated in API 29 **/
                resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    initializeContentValues(fileName).apply {
                        put(MediaStore.Images.Media.DATA, file.absolutePath)
                    }
                )
            }

            uri?.let {
                resolver.openOutputStream(it)?.apply {
                    bitmap.compressAsPng(this)
                    flushAndClose()
                }
            }

            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }

    override suspend fun saveBitmapToCache(bitmap: Bitmap): Response<Uri> {
        return try {
            val imageFolder = File(context.cacheDir, "images")
            imageFolder.mkdirs()
            println("Image folder made")
            val file = File(imageFolder, "cached_image.png")
            FileOutputStream(file).apply {
                bitmap.compressAsPng(outputStream = this)
                flushAndClose()
            }
            Response.Success(FileProvider.getUriForFile(context, "com.activityartapp", file))
        } catch (e: Exception) {
            println("Here exception caught saving to cache, $e")
            Response.Error(exception = e)
        }
    }

    private fun initializeContentValues(fileName: String): ContentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    }

    private fun Bitmap.compressAsPng(outputStream: OutputStream) {
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }

    private fun OutputStream.flushAndClose() {
        flush()
        close()
    }
}