package controller

import model.AccessType
import model.User
import service.AccessTableService
import service.IAccessTableService

class AccessTableController(private val service: IAccessTableService): IAccessTableController {
    override var currentUser: User? = null

    override fun createTable(userCount: Int, propertyCount: Int) {
        service.generateRandomTable(userCount, propertyCount)
    }

    override fun getUserAccesses(user: User?): String {
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

    override fun getTable(): String {
        var result = ""
        val userSet = service.getUsers()
        for (user in userSet)
            result += getUserAccesses(user)
        return result
    }

    override fun logIn(userName: String): Boolean {
        currentUser = service.findUser(userName)
        return currentUser != null
    }

    override fun getCurrentUserName(): String {
        return currentUser?.name.orEmpty()
    }

    override fun performAction(
        propertyName: String,
        action: AccessType,
        recipientName: String?,
        recipientAction: AccessType?
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