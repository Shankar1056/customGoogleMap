package tyler.a3frames.myfirstapplication.view

import tyler.a3frames.myfirstapplication.pojo.User

class LoginActivityMVP {

    interface View {

        fun getFirstName(): String
        fun getLastName(): String
        fun showUserNotAvailable()

        fun showInputError()
        fun setFirstName(firstName: String)
        fun setLastName(lastNamr: String)
        fun saveData()
    }

    interface Presenter {

        fun setView(view: View)
        fun loginButtonClicked()
        fun getCurrentUser()
    }

    interface Model {

        fun createUser(firstName: String, lastName: String)
        fun getUser(): User

    }
}