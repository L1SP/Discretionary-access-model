package model

enum class AccessType(val stringValue: String) {
    FullAccess("Полный доступ"),
    Grant("Выдача доступа"),
    Read("Чтение"),
    Write("Запись")
}