package tyler.a3frames.myfirstapplication.model

import tyler.a3frames.myfirstapplication.pojo.User
import tyler.a3frames.myfirstapplication.view.LoginActivityMVP
import tyler.a3frames.myfirstapplication.repo.LoginRepository

class LoginModel(private val repository: LoginRepository) : LoginActivityMVP.Model {



    override fun createUser(firstName: String, lastName: String) {

        repository.saveUser(User(firstName, lastName))
    }

    override fun getUser(): User {
       return repository.getUser()
    }







}