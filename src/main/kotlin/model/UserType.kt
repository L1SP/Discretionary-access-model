package model

enum class UserType(val stringValue: String) {
    Administrator("Администратор"),
    User("Пользователь"),
    Guest("Гость")
}