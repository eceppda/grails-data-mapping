package org.grails.datastore.gorm.couchbase.bean.factory

import groovy.transform.Canonical
import org.grails.datastore.gorm.bean.factory.AbstractMappingContextFactoryBean
import org.grails.datastore.mapping.couchbase.config.CouchbaseMappingContext
import org.grails.datastore.mapping.model.MappingContext
import org.springframework.util.Assert

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseMappingContextFactoryBean extends AbstractMappingContextFactoryBean {

    String defaultDatabaseName
    DefaultMappingHolder defaultMapping

    @Override
    protected MappingContext createMappingContext() {
        Assert.hasText(defaultDatabaseName, "Property [defaultDatabaseName] must be set!")
        return new CouchbaseMappingContext(defaultDatabaseName, defaultMapping?.defaultMapping)
    }
}

@Canonical
class DefaultMappingHolder {
    Closure defaultMapping
}
