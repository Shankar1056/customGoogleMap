package tyler.a3frames.login_mvp_dagger2.root

import android.app.Application
import tyler.a3frames.myfirstapplication.module.LoginModule

class App: Application() {


    private lateinit var component: ApplicationComponent
    fun getComponent(): ApplicationComponent {
        return component
    }
    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .loginModule(LoginModule())
                .build()
    }


}