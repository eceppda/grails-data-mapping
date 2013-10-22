package org.grails.datastore.mapping.couchbase

import org.junit.Test

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class BasicPersistenceSpec extends AbstractCouchbaseTest {

    @Test
    void testBasicPersistenceOperations() {
        cbds.mappingContext.addPersistentEntity(TestEntity)

        CouchbaseSession session = cbds.connect()

        def te = new TestEntity(name: "Bob", id: "111")
        session.persist te
        session.flush()

        assert te != null
        assert te.id != null
        assert te.id instanceof String

        session.clear()
        te = session.retrieve(TestEntity, te.id)
        assert te != null
        assert te.name == "Bob"

        te.name = "Fred"
        session.persist(te)
        session.flush()
        session.clear()

        te = session.retrieve(TestEntity, te.id)
        assert te != null
        assert te.id != null
        assert te.name == 'Fred'

        session.delete te
        session.flush()

        te = session.retrieve(TestEntity, te.id)
        assert te == null
    }

}

class TestEntity {
    String id
    String name
}
