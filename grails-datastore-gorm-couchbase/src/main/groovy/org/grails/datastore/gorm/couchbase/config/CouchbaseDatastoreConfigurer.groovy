package org.grails.datastore.gorm.couchbase.config

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.validation.GrailsDomainClassValidator
import org.grails.datastore.gorm.events.AutoTimestampEventListener
import org.grails.datastore.gorm.events.DomainEventListener
import org.grails.datastore.mapping.couchbase.CouchbaseDatastore
import org.grails.datastore.mapping.couchbase.config.CouchbaseMappingContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.GenericApplicationContext

/**
 * Support class for configuration of Couchbase
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseDatastoreConfigurer {

    /**
     * Configures Couchbase for use with Gorm
     *
     * @param The configuration
     */
    static CouchbaseDatastore configure(Map configuration = Collections.emptyMap(), Class... classes) {
        ExpandoMetaClass.enableGlobally()

        def ctx = new GenericApplicationContext()
        ctx.refresh()
        return configure(classes, ctx, configuration)
    }

    static CouchbaseDatastore configure(ConfigurableApplicationContext ctx, Map configuration, Class... classes) {
        final context = new CouchbaseMappingContext(configuration['bucketName'])
        def grailsApplication = new DefaultGrailsApplication(classes, Thread.currentThread().getContextClassLoader())
        grailsApplication.initialise()
        for (cls in classes) {
            def validator = new GrailsDomainClassValidator()
            validator.setGrailsApplication(grailsApplication)
            validator.setMessageSource(ctx)
            validator.setDomainClass(grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, cls.name))
            final entity = context.addPersistentEntity(cls)
            context.addEntityValidator(entity, validator)
        }
        def couchbaseDatastore = new CouchbaseDatastore(context, configuration, ctx)

        couchbaseDatastore.applicationContext.addApplicationListener new DomainEventListener(couchbaseDatastore)
        couchbaseDatastore.applicationContext.addApplicationListener new AutoTimestampEventListener(couchbaseDatastore)

        return couchbaseDatastore
    }
}
