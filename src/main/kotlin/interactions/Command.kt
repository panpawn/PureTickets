package interactions

interface Command {
    val name: String
    val permission: String
    val targets: Boolean
}