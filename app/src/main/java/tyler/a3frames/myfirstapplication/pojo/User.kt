package tyler.a3frames.myfirstapplication.pojo

class User(private val firstName: String, private val lastName: String) {
        var id: Int? = null

    fun setId(id: Int) {
        this.id = id
    }

    fun getFirstName(): String {
        return firstName
    }


    fun getLastName(): String {
        return lastName
    }
}