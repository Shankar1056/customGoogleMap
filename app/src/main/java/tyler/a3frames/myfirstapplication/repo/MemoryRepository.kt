package tyler.a3frames.myfirstapplication.repo

import tyler.a3frames.myfirstapplication.pojo.User

class MemoryRepository : LoginRepository {
    private var user: User?= null

    override fun saveUser(user: User) {
        var usr = user

        if (usr == null) {
            usr = getUser()
        }

        this.user = usr

    }

    override fun getUser(): User {

            if (user == null) {
                val user = User("Shankar", "Kumar")
                user.setId(0)
                return user
            } else {
                return user as User
            }

    }
   }