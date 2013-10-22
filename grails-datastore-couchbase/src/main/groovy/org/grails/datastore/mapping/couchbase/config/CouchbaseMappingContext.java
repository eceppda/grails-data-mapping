package org.grails.datastore.mapping.couchbase.config;

import groovy.lang.Closure;
import org.grails.datastore.mapping.document.config.DocumentMappingContext;

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
public class CouchbaseMappingContext extends DocumentMappingContext {

    public CouchbaseMappingContext(String defaultDatabaseName) {
        super(defaultDatabaseName);
    }

    public CouchbaseMappingContext(String defaultDatabaseName, Closure defaultMapping) {
        super(defaultDatabaseName, defaultMapping);
    }
}
