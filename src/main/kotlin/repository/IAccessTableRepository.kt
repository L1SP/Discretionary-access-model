package repository

import model.AccessType
import model.Property
import model.User

interface IAccessTableRepository {
    var userSet: HashSet<User>
    var propertySet: HashSet<Property>
    var accessMap: HashMap<Pair<User, Property>, HashSet<AccessType>>
}