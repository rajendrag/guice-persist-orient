package ru.vyarus.guice.persist.orient.base.model.support

import ru.vyarus.guice.persist.orient.db.scheme.annotation.Persistent
import ru.vyarus.guice.persist.orient.model.VersionedEntity

/**
 * Model bean used for schema init from package and for auto scan schema initializer.
 * @author Vyacheslav Rusakov 
 * @since 18.07.2014
 */
@Persistent
class ModelAuto extends VersionedEntity {
    String nick
    int age
}
