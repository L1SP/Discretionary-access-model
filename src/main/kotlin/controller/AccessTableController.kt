package controller

import model.AccessType
import model.User
import service.AccessTableService

class AccessTableController {
    private val service = AccessTableService()
    private var currentUser: User? = null

    fun createTable(userCount: Int, propertyCount: Int) {
        service.generateRandomTable(userCount, propertyCount)
    }

    fun getUserAccesses(user: User? = currentUser): String {
        var result = ""
        val propertySet = service.getProperties()
        if (user != null) {
            result += ("${user.name}: ${user.type.stringValue}\n")
            for (property in propertySet) {
                result += ("${property.name}: ")
                val accesses = service.getAccessForProperty(user, property)
                if (accesses == null)
                    result += ("Нет доступов")
                else {
                    result += ("Права доступа: ")
                    for (access in accesses) {
                        result += (access.stringValue + ' ')
                    }
                }
                result += '\n'
            }
        }
        return result + '\n'
    }

    fun getTable(): String {
        var result = ""
        val userSet = service.getUsers()
        for (user in userSet)
            result += getUserAccesses(user)
        return result
    }

    fun logIn(userName: String): Boolean {
        currentUser = service.findUser(userName)
        return currentUser != null
    }

    fun getCurrentUserName(): String {
        return currentUser?.name.orEmpty()
    }

    fun performAction(
        propertyName: String,
        action: AccessType,
        recipientName: String? = null,
        recipientAction: AccessType? = null
    ): Boolean? {
        val property = service.findProperty(propertyName)
        val user = currentUser
        val accessResult: Boolean

        if (user != null && property != null)
            accessResult = service.requestAccess(user, property, action)
        else
            return null

        if (!accessResult)
            return false

        if (action == AccessType.Grant && recipientName != null && recipientAction != null) {
            if (service.requestAccess(user, property, recipientAction)) {
                val recipient = service.findUser(recipientName) ?: return null
                service.grantAccess(recipient, property, recipientAction)
            } else
                return false
        }
        return true
    }
}