package org.grails.datastore.gorm.couchbase.plugin.support

import org.grails.datastore.gorm.couchbase.bean.factory.CouchbaseMappingContextFactoryBean
import org.grails.datastore.gorm.couchbase.bean.factory.DefaultMappingHolder
import org.grails.datastore.gorm.plugin.support.SpringConfigurer
import org.grails.datastore.mapping.couchbase.config.CouchbaseMappingContext

/**
 * {code}
 *   grails {
 *     couchbase {
 *       host = "localhost"
 *       port = 27017
 *       username = "blah"
 *       password = "blah"
 *       databaseName = "foo"
 *     }
 *   }
 *   {code}
 *
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseSpringConfigurer extends SpringConfigurer {

    @Override
    String getDatastoreType() {
        return "Couchbase"
    }

    @Override
    Closure getSpringCustomizer() {
        return {
            def couchbaseConfig = application.config?.grails?.couchbase.clone()

            def databaseName = couchbaseConfig?.remove("defaultBucketName") ?: application.metadata.getApplicationName()

            couchbaseMappingContext(CouchbaseMappingContextFactoryBean) {
                defaultDatabaseName = databaseName
                grailsApplication = ref('grailsApplication')
                pluginManager = ref('pluginManager')
                if(couchbaseConfig.default.mapping instanceof Closure) {
                    defaultMapping = new DefaultMappingHolder((Closure)couchbaseConfig.default.mapping)
                }
            }

            couchbaseOptions(CouchbaseOptionsFactoryBean) {
                if(couchbaseConfig?.options) {
                    for (option in couchbaseConfig.remove("options")) {
                        setProperty(option.key, option.value)
                    }
                }
            }

            couchbase(CoucbaseFactoryBean) {
                options = couchbaseOptions


            }


        }
    }
    
}
