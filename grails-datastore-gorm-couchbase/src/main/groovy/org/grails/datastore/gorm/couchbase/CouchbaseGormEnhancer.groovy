package org.grails.datastore.gorm.couchbase

import com.couchbase.client.CouchbaseClient
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormInstanceApi
import org.grails.datastore.gorm.GormStaticApi
import org.grails.datastore.gorm.finders.FinderMethod
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.core.Session
import org.grails.datastore.mapping.core.SessionCallback
import org.grails.datastore.mapping.core.SessionImplementor
import org.grails.datastore.mapping.couchbase.CouchbaseDatastore
import org.grails.datastore.mapping.couchbase.CouchbaseSession
import org.grails.datastore.mapping.couchbase.engine.CouchbaseEntityPersister
import org.grails.datastore.mapping.couchbase.engine.DBObject
import org.springframework.transaction.PlatformTransactionManager

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseGormEnhancer extends GormEnhancer {

    CouchbaseGormEnhancer(Datastore datastore, PlatformTransactionManager transactionManager) {
        super(datastore, transactionManager)
    }

    CouchbaseGormEnhancer(Datastore datastore) {
        this(datastore, null)
    }

    protected <D> GormStaticApi<D> getStaticApi(Class<D> cls) {
        return new CouchbaseGormStaticApi<D>(cls, datastore, getFinders())
    }

    protected <D> GormInstanceApi<D> getInstanceApi(Class<D> cls) {
        final api = new CouchbaseGormInstanceApi<D>(cls, datastore)
        api.failOnError = failOnError
        return api
    }
}

class CouchbaseGormInstanceApi<D> extends GormInstanceApi<D> {

    CouchbaseGormInstanceApi(Class<D> persistentClass, Datastore datastore) {
        super(persistentClass, datastore)
    }

    /**
     * Allows accessing to dynamic properties with the dot operator
     *
     * @param instance The instance
     * @param name The property name
     * @return The property value
     */
    def propertyMissing(D instance, String name) {
        getAt(instance, name)
    }

    /**
     * Allows setting a dynamic property via the dot operator
     * @param instance The instance
     * @param name The property name
     * @param val The value
     */
    def propertyMissing(D instance, String name, val) {
        putAt(instance, name, val)
    }

    /**
     * Allows subscript access to schemaless attributes.
     *
     * @param instance The instance
     * @param name The name of the field
     */
    void putAt(D instance, String name, value) {
        if (instance.hasProperty(name)) {
            instance.setProperty(name, value)
        } else {
            execute (new SessionCallback<DBObject>() {
                DBObject doInSession(Session session) {
                    SessionImplementor si = (SessionImplementor)session

                    if (si.isStateless(persistentEntity)) {
                        CouchbaseDatastore ms = (CouchbaseDatastore)datastore
                        CouchbaseEntityPersister persister = session.getPersister(instance)
                        def id = persister.getObjectIdentifier(instance)
                        // TODO
                        return null
                    }
                    else {
                        final dbo = getDbo(instance)
                        dbo?.put name, value
                        return dbo
                    }
                }
            })

        }
    }

    /**
     * Allows subscript access to schemaless attributes.
     *
     * @param instance The instance
     * @param name The name of the field
     * @return the value
     */
    def getAt(D instance, String name) {
        if (instance.hasProperty(name)) {
            return instance.getProperty(name)
        }

        def dbo = getDbo(instance)
        if (dbo != null && dbo.containsField(name)) {
            return dbo.get(name)
        }
        return null
    }

    /**
     * Return the DBObject instance for the entity
     *
     * @param instance The instance
     * @return The DBObject instance
     */
    DBObject getDbo(D instance) {
        execute (new SessionCallback<DBObject>() {
            DBObject doInSession(Session session) {

                if (!session.contains(instance) && !instance.save()) {
                    throw new IllegalStateException(
                            "Cannot obtain DBObject for transient instance, save a valid instance first")
                }

                CouchbaseEntityPersister persister = session.getPersister(instance)
                def id = persister.getObjectIdentifier(instance)
                def dbo = session.getCachedEntry(persister.getPersistentEntity(), id)
                if (dbo == null) {
                    CouchbaseDatastore ms = (CouchbaseDatastore)datastore
                }
                return dbo
            }
        })
    }
}

class CouchbaseGormStaticApi<D> extends GormStaticApi<D> {

    CouchbaseGormStaticApi(Class<D> persistentClass, Datastore datastore, List<FinderMethod> finders) {
        super(persistentClass, datastore, finders)
    }

    @Override
    CouchbaseCriteriaBuilder createCriteria() {
        return new CouchbaseCriteriaBuilder(persistentClass, datastore.currentSession)
    }

    /**
     * @return The database for this domain class
     */
    CouchbaseClient getDB() {
        CouchbaseSession ms = (CouchbaseSession)datastore.currentSession
        ms.getNativeInterface()
    }

}