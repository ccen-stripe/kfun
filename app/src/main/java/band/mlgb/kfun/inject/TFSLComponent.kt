package band.mlgb.kfun.inject

import band.mlgb.kfun.IDDetectorActivity
import dagger.Component

@Component(modules = [TFSLModule::class])
interface TFSLComponent {
    fun inject(activity: IDDetectorActivity)
}