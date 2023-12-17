package cli

import controller.AccessTableController
import model.AccessType

class CLI {
    private val controller = AccessTableController()

    private fun printWelcome() {
        print("Это лабораторная по дискретиционной модели безопасности\n")
    }

    private fun printGeneration() {
        print("Введите количество пользователей для генерации(желательно больше 2): ")
        val userCount = readln().toInt()
        print("Введите количество сущностей для генерации: ")
        val propertyCount = readln().toInt()
        controller.createTable(userCount, propertyCount)
        print("Матрица была успешно сгенерирована\n")
    }

    private fun printSelfAccesses() {
        print(controller.getUserAccesses())
    }

    private fun printAllAccesses() {
        print(controller.getTable())
    }

    private fun printLoginLoop() {
        var exit = false
        while (!exit) {
            print("Введите имя пользователя для входа: ")
            val userName = readln()
            if (controller.logIn(userName)) {
                println("Вы вошли под пользователем ${controller.getCurrentUserName()}")
                exit = true
            }
        }
    }

    private fun printReadAction() {
        print("Введите имя сущности: ")
        val propertyName = readln()
        when (controller.performAction(propertyName, AccessType.Read)) {
            null -> println("Сущности с таким именем нет")
            false -> println("Недостаточно прав")
            true -> println("Чтение прошло успешно")
        }
    }

    private fun printWriteAction() {
        print("Введите имя сущности: ")
        val propertyName = readln()
        when (controller.performAction(propertyName, AccessType.Write)) {
            null -> println("Сущности с таким именем нет")
            false -> println("Недостаточно прав")
            true -> println("Запись прошла успешно")
        }
    }

    private fun printGrantAction() {
        print("Введите имя сущности: ")
        val propertyName = readln()
        print("Введите имя получателя прав: ")
        val recipientName = readln()
        print("Введите передаваемое право: ")
        val recipientAction: AccessType? = when (readln()) {
            "Полный доступ" -> AccessType.FullAccess
            "Выдача доступа" -> AccessType.Grant
            "Чтение" -> AccessType.Read
            "Запись" -> AccessType.Write
            else -> null
        }
        when (controller.performAction(propertyName, AccessType.Grant, recipientName, recipientAction)) {
            null -> println("Сущности с таким именем нет")
            false -> println("Недостаточно прав")
            true -> println("Выдача доступа прошла успешно")
        }
    }

    private fun printActionLoop() {
        var choice = 0
        while (choice != 5) {
            println("Выберите пункт меню:")
            println("1. Чтение")
            println("2. Запись")
            println("3. Выдача доступа")
            println("4. Просмотр своих доступов")
            println("5. Выход в главное меню")
            choice = readln().toInt()
            when (choice) {
                1 -> printReadAction()
                2 -> printWriteAction()
                3 -> printGrantAction()
                4 -> printSelfAccesses()
            }
        }
    }

    private fun printExit(): Boolean {
        var choice = 0
        while (choice != 1 && choice != 2) {
            println("Выйти из программы?")
            println("1. Да")
            println("2. Нет")
            choice = readln().toInt()
        }
        return choice == 1
    }

    fun run() {
        printWelcome()
        printGeneration()
        var exit = false
        while (!exit) {
            printAllAccesses()
            printLoginLoop()
            printActionLoop()
            exit = printExit()
        }
    }
}
