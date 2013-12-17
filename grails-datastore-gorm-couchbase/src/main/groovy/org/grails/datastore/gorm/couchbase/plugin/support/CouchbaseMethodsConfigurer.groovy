package org.grails.datastore.gorm.couchbase.plugin.support

import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormInstanceApi
import org.grails.datastore.gorm.GormStaticApi
import org.grails.datastore.gorm.couchbase.CouchbaseGormEnhancer
import org.grails.datastore.gorm.couchbase.CouchbaseGormInstanceApi
import org.grails.datastore.gorm.couchbase.CouchbaseGormStaticApi
import org.grails.datastore.gorm.finders.FinderMethod
import org.grails.datastore.gorm.plugin.support.ApplicationContextConfigurer
import org.grails.datastore.gorm.plugin.support.DynamicMethodsConfigurer
import org.grails.datastore.mapping.couchbase.CouchbaseDatastore
import org.grails.datastore.mapping.couchbase.engine.CouchbaseEntityPersister
import org.springframework.transaction.PlatformTransactionManager

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseMethodsConfigurer extends ApplicationContextConfigurer {

    CouchbaseMethodsConfigurer(CouchbaseDatastore datastore, PlatformTransactionManager transactionManager) {
        super(datastore, transactionManager)
    }

    @Override
    void configure() {
        DynamicMethodsConfigurer.configure()
        def asTypeHook = { Class cls ->
            CouchbaseEntityPersister p = datastore.currentSession.getPersister(cls)
            if (p != null) {
                // TODO
            }
            else {
                throw new IllegalArgumentException("Cannot convert DBOject [$delegate] to target type $cls. Type is not a persistent entity")
            }
        }
    }

    @Override
    String getDatastoreType() {
        return "Couchbase"
    }

    @Override
    protected GormStaticApi createGormStaticApi(Class cls, List<FinderMethod> finders) {
        new CouchbaseGormStaticApi(cls, datastore, finders)
    }

    @Override
    protected GormInstanceApi createGormInstanceApi(Class cls) {
        final api = new CouchbaseGormInstanceApi(cls, datastore)
        api.failOnError = failOnError
        api
    }

    @Override
    protected GormEnhancer createEnhancer() {
        def ge
        if (transactionManager == null) {
            ge = new CouchbaseGormEnhancer(datastore)
        }
        else {
            ge = new CouchbaseGormEnhancer(datastore, transactionManager)
        }
        ge.failOnError = failOnError
        ge
    }
}
