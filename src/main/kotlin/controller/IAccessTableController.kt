package controller

import model.AccessType
import model.User

interface IAccessTableController {
    var currentUser: User?
    fun createTable(userCount: Int, propertyCount: Int)
    fun getUserAccesses(user: User? = currentUser): String
    fun getTable(): String
    fun logIn(userName: String): Boolean
    fun getCurrentUserName(): String
    fun performAction(
        propertyName: String,
        action: AccessType,
        recipientName: String? = null,
        recipientAction: AccessType? = null
    ): Boolean?
}