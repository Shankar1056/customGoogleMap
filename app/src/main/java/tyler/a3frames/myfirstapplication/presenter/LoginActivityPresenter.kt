package tyler.a3frames.myfirstapplication.presenter

import org.jetbrains.annotations.Nullable
import tyler.a3frames.myfirstapplication.view.LoginActivityMVP

class LoginActivityPresenter(private val model: LoginActivityMVP.Model) : LoginActivityMVP.Presenter {
    @Nullable
    private lateinit var view: LoginActivityMVP.View


    override fun setView(view: LoginActivityMVP.View) {
        this.view = view
    }

    override fun loginButtonClicked() {
        if (view != null){
            if (view.getFirstName().toString().trim().equals("") || view.getLastName().toString().trim().equals("")){
                view.showInputError()
            }else{
                model.createUser(view.getFirstName().toString().trim(), view.getLastName().toString().trim())
                view.saveData()
            }
        }
    }

    override fun getCurrentUser() {
        val user = model.getUser()

        if (user != null){
            if (view != null) {
                view.setFirstName(user.getFirstName())
                view.setLastName(user.getLastName())
            }
        }else{
            view.showUserNotAvailable()
        }
    }
}