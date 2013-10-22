package org.grails.datastore.mapping.couchbase

import org.junit.Before
import org.springframework.context.support.GenericApplicationContext

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
abstract class AbstractCouchbaseTest {

    protected CouchbaseDatastore cbds

    // your development connection here
    def connectionDetails = [uris:"http://localhost:8091/pools", bucket:"default", password:""]

    @Before
    void setUp() {
        cbds = new CouchbaseDatastore(connectionDetails)
        def ctx = new GenericApplicationContext()
        ctx.refresh()
        cbds.applicationContext = ctx
    }
}
