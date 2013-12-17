package org.grails.datastore.gorm.couchbase.plugin.support

import grails.gorm.tests.Person
import grails.spring.BeanBuilder
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager
import org.grails.datastore.mapping.couchbase.config.CouchbaseMappingContext
import spock.lang.Specification

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseSpringConfigurerSpec extends Specification {

    void "Test configure Couchbase via Spring"() {

        when: "The spring configurer is used"

            def configurer = new CouchbaseSpringConfigurer()
            def config = configurer.getConfiguration()
            def bb = new BeanBuilder()
            final binding = new Binding()
            def application = new DefaultGrailsApplication([Person] as Class[], Thread.currentThread().contextClassLoader)

            application.config.grails.couchbase.databaseName = "test"

            final closureConfig = {
                '*'(reference: true)
            }

            application.config.grails.couchbase.default.mapping = closureConfig
            application.initialise()
            application.registerArtefactHandler(new DomainClassArtefactHandler())
            binding.setVariable("application", application)
            binding.setVariable("manager", new DefaultGrailsPluginManager([] as Class[], application))
            bb.setBinding(binding)
            bb.beans {
                grailsApplication(DefaultGrailsApplication, [Person] as Class[], Thread.currentThread().contextClassLoader) { bean ->
                    bean.initMethod = "initialise"
                }
                pluginManager(DefaultGrailsPluginManager, [] as Class[], ref("grailsApplication"))
            }
            bb.beans config
            def ctx = bb.createApplicationContext()
            CouchbaseMappingContext mappingContext = ctx.getBean("couchbaseMappingContext", CouchbaseMappingContext)
            def entity = mappingContext?.getPersistentEntity(Person.name)

        then:"The application context is created"
            ctx != null
            ctx.containsBean("persistenceInterceptor")
            mappingContext.defaultMapping = closureConfig
            entity != null
            entity.getPropertyByName('pets').getMapping().mappedForm.reference == true
    }
}
