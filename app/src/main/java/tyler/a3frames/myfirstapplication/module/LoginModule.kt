package tyler.a3frames.myfirstapplication.module

import dagger.Module
import dagger.Provides
import tyler.a3frames.myfirstapplication.model.LoginModel
import tyler.a3frames.myfirstapplication.view.LoginActivityMVP
import tyler.a3frames.myfirstapplication.presenter.LoginActivityPresenter
import tyler.a3frames.myfirstapplication.repo.LoginRepository
import tyler.a3frames.myfirstapplication.repo.MemoryRepository

@Module
class LoginModule{

    @Provides
    fun provideLoginActivityPresenter(model: LoginActivityMVP.Model): LoginActivityMVP.Presenter {
        return LoginActivityPresenter(model)
    }

    @Provides
    fun provideLoginActivityModel(repository: LoginRepository): LoginActivityMVP.Model {
        return LoginModel(repository)
    }

    @Provides
    fun provideLoginRepository(): LoginRepository {
        return MemoryRepository()
    }
}
