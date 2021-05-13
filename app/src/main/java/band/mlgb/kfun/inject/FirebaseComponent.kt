package band.mlgb.kfun.inject

import band.mlgb.kfun.IDDetectorActivity
import band.mlgb.kfun.LangIdActivity
import band.mlgb.kfun.SmartReplyActivity
import band.mlgb.kfun.TranslatorActivity
import dagger.Component

@Component(modules = [FirebaseNlpModule::class])
interface FirebaseComponent {
        fun inject(activity: LangIdActivity)
        fun inject(activity: SmartReplyActivity)
        fun inject(activity: TranslatorActivity)
        fun inject(activity: IDDetectorActivity)
}