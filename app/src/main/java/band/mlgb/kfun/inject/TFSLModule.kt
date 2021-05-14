package band.mlgb.kfun.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions


@Module
class TFSLModule(val context: Context) {
    @Provides
    @Reusable
    fun provideImageClassifier(): ImageClassifier {
        return ImageClassifier.createFromFileAndOptions(
            context,
            "idDetectorWithMetadata.tflite",
            ImageClassifierOptions.builder()
                .setMaxResults(10)
                .setNumThreads(4)
                .build()
        )
    }
}