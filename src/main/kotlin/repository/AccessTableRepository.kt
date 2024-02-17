package repository

import model.AccessType
import model.Property
import model.User

class AccessTableRepository: IAccessTableRepository {
    override var userSet = HashSet<User>()
    override var propertySet = HashSet<Property>()
    override var accessMap = HashMap<Pair<User, Property>, HashSet<AccessType>>()
}