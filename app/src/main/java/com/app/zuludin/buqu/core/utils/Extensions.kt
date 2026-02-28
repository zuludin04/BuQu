package com.app.zuludin.buqu.core.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.app.zuludin.buqu.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, ".jpg", externalCacheDir
    )
    return image
}

fun Context.shareImage(title: String, image: Bitmap, filename: String) {
    val file = try {
        val outputFile = File.createTempFile(filename, ".png", externalCacheDir)
        val outPutStream = FileOutputStream(outputFile)
        image.compress(Bitmap.CompressFormat.PNG, 100, outPutStream)
        outPutStream.flush()
        outPutStream.close()
        outputFile
    } catch (e: Throwable) {
        return toast(e)
    }
    val uri = file.toUriCompat(this)
    val shareIntent = Intent().apply {
        flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        action = Intent.ACTION_SEND
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    val chooser = createChooser(shareIntent, title)
    chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

    startActivity(chooser)
}

fun File.toUriCompat(context: Context): Uri {
    return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", this)
}

fun Context.toast(throwable: Throwable) =
    throwable.message?.let { toast(it) }
        ?: toast("Unknown error")

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Bitmap.rotateImage(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Context.fixImageRotation(uri: Uri): Bitmap? {
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val exifInputStream = contentResolver.openInputStream(uri) ?: return bitmap
    val exif = ExifInterface(exifInputStream)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    exifInputStream.close()

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> bitmap.rotateImage(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> bitmap.rotateImage(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> bitmap.rotateImage(270f)
        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }
}
