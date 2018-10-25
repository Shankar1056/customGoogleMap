package tyler.a3frames.login_mvp_dagger2.root

import dagger.Component
import tyler.a3frames.myfirstapplication.module.LoginModule
import tyler.a3frames.myfirstapplication.view.LoginActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, LoginModule::class))
interface ApplicationComponent {

    fun inject(loginActivitty: LoginActivity) {

    }
}