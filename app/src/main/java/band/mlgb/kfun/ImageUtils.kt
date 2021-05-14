@file:JvmName("ImageUtils")

package band.mlgb.kfun

import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.IOException


@Throws(IOException::class)
fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {

    val input = context.contentResolver.openInputStream(selectedImage)!!
    val ei: ExifInterface
    ei = if (Build.VERSION.SDK_INT > 23)
        ExifInterface(input)
    else
        ExifInterface(selectedImage.path!!)

    return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
        else -> img
    }
}

private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
    return Matrix().let {
        it.postRotate(degree)
        val ret = Bitmap.createBitmap(img, 0, 0, img.width, img.height, it, true)
        img.recycle()
        ret
    }
}
// stolen from https://stackoverflow.com/questions/56772967/converting-imageproxy-to-bitmap
fun Image.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer // Y
    val vuBuffer = planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}