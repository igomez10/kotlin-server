import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val age: Int){
    override fun toString(): String {
        return "Name: $name, Age: $age"
    }
}
