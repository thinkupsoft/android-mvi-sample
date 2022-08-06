package com.thinkup.common.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import com.thinkup.common.JPG
import com.thinkup.common.random
import java.io.*
import kotlin.jvm.Throws


object FileUtil {
    const val MAX_HEIGHT = 1024
    const val MAX_WIDTH = 1024

    fun getFile(context: Context?): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File(Environment.getExternalStorageDirectory(),
                InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME
            )
        } else {
            File(context?.filesDir, InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME)
        }
    }

    fun getContentFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver?.openInputStream(uri)
        return copyInputStreamToFile(context, inputStream!!)
    }

    fun copyInputStreamToFile(context: Context, inputStream: InputStream): File {
        val file = getFile(context)
        file.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
        return file
    }

    fun createNewTempFile(context: Context, uri: Uri): File {
        val newName = String.random()
        val inputStream = context.contentResolver?.openInputStream(uri)
        return inputStream?.let {
            val file = File.createTempFile(newName, JPG, context!!.filesDir)
            file.outputStream().use { fileOut ->
                inputStream.copyTo(fileOut)
            }
            file
        } ?: run { getFile(context) }
    }

    fun decodeFile(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    @Throws(IOException::class)
    fun handleSamplingAndRotationBitmap(
        context: Context,
        selectedImage: Uri?,
        path: String?
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var imageStream = context.contentResolver.openInputStream(selectedImage!!)
        BitmapFactory.decodeStream(imageStream, null, options)
        imageStream!!.close()
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        imageStream = context.contentResolver.openInputStream(selectedImage)
        var img = BitmapFactory.decodeStream(imageStream, null, options)
        img = rotateImageIfRequired(img, selectedImage)
        resaveBitmap(path, img)
        return img
    }

    /**
     * Calculate an inSampleSize for use in a [BitmapFactory.Options] object when decoding
     * bitmaps using the decode* methods from [BitmapFactory]. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     * method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > MAX_HEIGHT || width > MAX_WIDTH) { // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / MAX_HEIGHT.toFloat())
            val widthRatio = Math.round(width.toFloat() / MAX_WIDTH.toFloat())
            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).
            val totalPixels = width * height.toFloat()
            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = MAX_WIDTH * MAX_HEIGHT * 2.toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }
        return inSampleSize
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap?, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path.orEmpty())
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    /**
     * Create a new bitmap rotated with X angle
     */
    private fun rotateImage(img: Bitmap?, degree: Int): Bitmap? {
        if (img == null) return null
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    /**
     * Override a file with the resample bitmap
     */
    private fun resaveBitmap(path: String?, bitmap: Bitmap?): File? {
        // help for fix landscape photos
        var outStream: OutputStream? = null
        var file = File(path)
        if (file.exists()) {
            file.delete()
            file = File(path)
        }
        try { // make a new bitmap from your file
            outStream = FileOutputStream(path)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }
}