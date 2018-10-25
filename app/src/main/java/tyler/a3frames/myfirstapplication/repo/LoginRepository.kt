package tyler.a3frames.myfirstapplication.repo

import tyler.a3frames.myfirstapplication.pojo.User

interface LoginRepository {

    fun getUser(): User
    fun saveUser(user: User)
}