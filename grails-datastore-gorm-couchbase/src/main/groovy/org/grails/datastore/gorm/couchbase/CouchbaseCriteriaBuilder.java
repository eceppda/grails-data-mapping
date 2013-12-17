package org.grails.datastore.gorm.couchbase;

import grails.gorm.CriteriaBuilder;
import org.grails.datastore.mapping.core.Session;
import org.grails.datastore.mapping.query.Query;
import org.grails.datastore.mapping.query.api.Criteria;
import org.grails.datastore.mapping.query.api.QueryArgumentsAware;

import java.util.Map;

/**
 * This is a wire frame of a potential class that could be used to build criteria
 * once a query language is supported.
 *
 * @author Jeff Hemminger <jeff@kropek.org>
 */
@SuppressWarnings("rawtypes")
public class CouchbaseCriteriaBuilder extends CriteriaBuilder {

    public CouchbaseCriteriaBuilder(final Class<?> targetClass, final Session session, final Query query) {
        super(targetClass, session, query);
    }

    public CouchbaseCriteriaBuilder(final Class<?> targetClass, final Session session) {
        super(targetClass, session);
    }

    public Criteria arguments(Map arguments) {
        ((QueryArgumentsAware)this.query).setArguments(arguments);
        return this;
    }
}
