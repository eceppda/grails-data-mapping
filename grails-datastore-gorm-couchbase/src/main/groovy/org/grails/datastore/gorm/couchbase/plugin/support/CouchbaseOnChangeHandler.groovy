package org.grails.datastore.gorm.couchbase.plugin.support

import org.grails.datastore.gorm.plugin.support.OnChangeHandler
import org.grails.datastore.mapping.core.Datastore
import org.springframework.transaction.PlatformTransactionManager

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseOnChangeHandler extends OnChangeHandler {

    CouchbaseOnChangeHandler(Datastore datastore, PlatformTransactionManager transactionManager) {
        super(datastore, transactionManager)
    }

    @Override
    String getDatastoreType() {
        return "Couchbase"
    }
}
