package band.mlgb.kfun

import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.Log
import band.mlgb.kfun.inject.DaggerTFSLComponent
import band.mlgb.kfun.inject.TFSLModule
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import javax.inject.Inject
import kotlin.concurrent.thread


class IDDetectorActivity : PickImageActivity() {

    @Inject
    lateinit var imageClassifier: ImageClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTFSLComponent.builder().tFSLModule(TFSLModule(this)).build().inject(this)
    }

    override fun handleLiveImage(image: Image, rotation: Int) {
        postLiveResult("new image: ${image.width} : ${image.height}")

        val ti = TensorImage()
        ti.load(image)
        runInferenceAndPostResult(ti)
    }

    private fun runInferenceAndPostResult(tensorImage: TensorImage) {
        // 0 - top
        // 1 - left
        // 2 - height
        // 3 - width
        // 4 = No Document,
        // 5 = passport
        // 6 = ID card front
        // 7 = ID card back
        // 8 = Invalid ID)

        val result = imageClassifier.classify(tensorImage)
        val labelMap = mapOf(
            "0" to "top",
            "1" to "left",
            "2" to "height",
            "3" to "width",
            "4" to "No Document",
            "5" to "passport",
            "6" to "ID card front",
            "7" to "ID card back",
            "8" to "Invalid ID",
        )
        val scores = result[0].categories.subList(4, 9).toMutableList()
        scores.sortBy { it.score }
        scores.forEach {
            val label = labelMap[it.label]
            Log.d("MLGB", "$label : ${it.score}")
        }
        postLiveResult(labelMap[scores[4].label])
    }

    override fun handleImage(bitmap: Bitmap) {
        runInferenceAndPostResult(TensorImage.fromBitmap(bitmap))
    }
}