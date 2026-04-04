//truck/vehicle info



data class Truck(
    val NumberPlate: String?,
    val makeAndModel: String?,
    val conditionAtCheckIn: String?,
    val kilometersAtCheckIn: Int,
    val WorkOnStatus: String?,
    val conditonAtCheckOut: String?,
    val kilometersAtCheckOut: Int,
    val tasks: MutableList<Task> =mutableListOf()
)




