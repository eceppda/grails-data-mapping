package org.grails.datastore.mapping.couchbase.query;

import org.grails.datastore.mapping.couchbase.CouchbaseSession;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.query.Query;
import org.grails.datastore.mapping.query.api.QueryArgumentsAware;

import java.util.List;
import java.util.Map;

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
public class CouchbaseQuery extends Query implements QueryArgumentsAware {

    public CouchbaseQuery(CouchbaseSession session, PersistentEntity entity) {
        super(session, entity);
    }

    @Override
    protected List executeQuery(PersistentEntity entity, Junction criteria) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setArguments(@SuppressWarnings("rawtypes") Map arguments) {
        // Currently not implemented
    }
}
