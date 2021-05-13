package band.mlgb.kfun

import android.graphics.Bitmap
import android.util.Log

class IDDetectorActivity : PickImageActivity() {
        override fun handleImage(bitmap: Bitmap) {
                Log.d("MLGB", "newImageposted")
        }
}