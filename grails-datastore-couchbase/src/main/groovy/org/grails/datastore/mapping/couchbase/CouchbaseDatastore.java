package org.grails.datastore.mapping.couchbase;

import com.couchbase.client.CouchbaseClient;
import org.grails.datastore.mapping.core.AbstractDatastore;
import org.grails.datastore.mapping.core.Session;
import org.grails.datastore.mapping.core.StatelessDatastore;
import org.grails.datastore.mapping.couchbase.config.CouchbaseMappingContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.authentication.UserCredentials;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A datastore implementation for the Couchbase document store.
 * Provides the ability to create a connection to a couchbase server.
 * @author Jeff Hemminger <jeff@kropek.org>
 */
public class CouchbaseDatastore extends AbstractDatastore implements DisposableBean, StatelessDatastore {

    protected Map<String, String> couchbaseOptions = new HashMap<String,String>();
    protected boolean stateless = false;
    protected UserCredentials userCredentials;
    private CouchbaseClient client;

    /**
     * Constructs a CouchbaseDatastore using the default database name of "test" and defaults for the host and port.
     * Typically used during testing.
     */
    public CouchbaseDatastore(Map<String, String> couchbaseOptions) {
        this(new CouchbaseMappingContext("test"), couchbaseOptions, null);
        this.couchbaseOptions = couchbaseOptions;
    }

    /**
     * Constructs a CouchbaseDatastore using the given MappingContext and connection details map.
     *
     * @param mappingContext The CouchbaseMappingContext
     * @param connectionDetails The connection details containing the HOST and PORT settings
     */
    public CouchbaseDatastore(CouchbaseMappingContext mappingContext,
                          Map<String, String> connectionDetails, ConfigurableApplicationContext ctx) {
        super(mappingContext, connectionDetails, ctx);

        initializeConverters(mappingContext);

    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    @Override
    protected Session createSession(Map<String, String> connDetails) {
        return new CouchbaseSession(this, getMappingContext(), getApplicationEventPublisher(), stateless);
    }

    public void destroy() throws Exception {
        super.destroy();
        if (client != null) {
            client.shutdown();
        }
    }

    @Override
    public boolean isSchemaless() {
        return true;
    }

}
