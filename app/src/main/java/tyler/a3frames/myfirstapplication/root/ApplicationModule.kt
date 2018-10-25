package tyler.a3frames.login_mvp_dagger2.root

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {


    @Provides
    @Singleton
    fun provideContext(): Application{
        return application
    }
}