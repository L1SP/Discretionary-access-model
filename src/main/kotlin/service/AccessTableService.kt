package service

import model.AccessType
import model.Property
import model.User
import model.UserType
import repository.IAccessTableRepository

class AccessTableService(private val repository: IAccessTableRepository): IAccessTableService {
    override fun grantAccess(user: User, property: Property, accessType: AccessType) {
        if (repository.accessMap[Pair(user, property)] == null) {
            repository.accessMap[Pair(user, property)] = HashSet()
        }
        if (accessType == AccessType.FullAccess) {
            repository.accessMap[Pair(user, property)]?.clear()
            repository.accessMap[Pair(user, property)]?.add(accessType)
        } else if (repository.accessMap[Pair(user, property)]?.contains(AccessType.FullAccess) == false)
            repository.accessMap[Pair(user, property)]?.add(accessType)
    }

    override fun requestAccess(user: User, property: Property, accessType: AccessType): Boolean {
        val element = repository.accessMap[Pair(user, property)]
        return element?.contains(AccessType.FullAccess) == true
                || element?.contains(accessType) == true
    }

    override fun generateRandomTable(userCount: Int, propertyCount: Int) {
        repository.accessMap.clear()
        repository.userSet.clear()
        repository.propertySet.clear()
        fillRandomUsers(userCount)
        fillRandomProperties(propertyCount)
        grantRandomAccess()
    }

    override fun findUser(named: String): User? {
        return repository.userSet.find { it.name == named }
    }

    override fun findProperty(named: String): Property? {
        return repository.propertySet.find { it.name == named }
    }

    override fun getUsers(): HashSet<User> {
        return repository.userSet
    }

    override fun getProperties(): HashSet<Property> {
        return repository.propertySet
    }

    override fun getAccessForProperty(user: User, property: Property): HashSet<AccessType>? {
        return repository.accessMap[Pair(user, property)]
    }

    private fun insertUser(user: User) {
        repository.userSet.add(user)
    }

    private fun insertProperty(property: Property) {
        repository.propertySet.add(property)
    }

    private fun fillRandomUsers(count: Int) {
        if (count > 0)
            insertUser((User("Администратор", UserType.Administrator)))
        if (count > 1)
            insertUser((User("Гость", UserType.Guest)))
        if (count > 2)
            for (i in 0 until count - 2) {
                insertUser((User("Пользователь ${i + 1}", UserType.User)))
            }
    }

    private fun fillRandomProperties(count: Int) {
        for (i in 0 until count) {
            insertProperty((Property("Сущность ${i + 1}")))
        }
    }

    private fun grantRandomAccess() {
        for (user in repository.userSet)
            for (property in repository.propertySet) {
                if (user.type == UserType.Administrator)
                    grantAccess(user, property, AccessType.FullAccess)
                else
                    for (i in 0..(0..2).random())
                        grantAccess(user, property, AccessType.values().random())
            }
    }
}