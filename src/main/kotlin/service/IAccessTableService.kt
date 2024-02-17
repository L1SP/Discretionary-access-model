package service

import model.AccessType
import model.Property
import model.User

interface IAccessTableService {
    fun grantAccess(user: User, property: Property, accessType: AccessType)
    fun requestAccess(user: User, property: Property, accessType: AccessType): Boolean
    fun generateRandomTable(userCount: Int, propertyCount: Int)
    fun findUser(named: String): User?
    fun findProperty(named: String): Property?
    fun getUsers(): HashSet<User>
    fun getProperties(): HashSet<Property>
    fun getAccessForProperty(user: User, property: Property): HashSet<AccessType>?
}