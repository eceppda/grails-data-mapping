package org.grails.datastore.gorm.couchbase.bean.factory

import com.couchbase.client.CouchbaseClient
import org.grails.datastore.gorm.events.AutoTimestampEventListener
import org.grails.datastore.gorm.events.DomainEventListener
import org.grails.datastore.mapping.couchbase.CouchbaseDatastore
import org.grails.datastore.mapping.model.MappingContext
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseDatastoreFactoryBean implements FactoryBean<CouchbaseDatastore>, ApplicationContextAware  {

    CouchbaseClient couchbase
    MappingContext mappingContext
    Map<String, String> config = [:]
    ApplicationContext applicationContext

    CouchbaseDatastore getObject() {
        CouchbaseDatastore datastore
        if(couchbase) {
            datastore = new CouchbaseDatastore()
        } else {
            datastore = new CouchbaseDatastore()
        }

        applicationContext.addApplicationListener new DomainEventListener(datastore)
        applicationContext.addApplicationListener new AutoTimestampEventListener(datastore)

        datastore.afterPropertiesSet()
        datastore
    }

    Class<?> getObjectType() { CouchbaseDatastore }

    boolean isSingleton() { true }
}
