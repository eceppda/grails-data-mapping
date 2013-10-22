package org.grails.datastore.mapping.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;
import org.grails.datastore.mapping.core.AbstractSession;
import org.grails.datastore.mapping.couchbase.engine.CouchbaseEntityPersister;
import org.grails.datastore.mapping.couchbase.query.CouchbaseQuery;
import org.grails.datastore.mapping.engine.Persister;
import org.grails.datastore.mapping.model.MappingContext;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.transactions.SessionOnlyTransaction;
import org.grails.datastore.mapping.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * Represents an active connection to a couchbase server
 *
 * @author Jeff Hemminger <jeff@kropek.org>
 */
public class CouchbaseSession extends AbstractSession<CouchbaseClient> {

    private static final Logger log = LoggerFactory.getLogger(CouchbaseSession.class);
    private CouchbaseClient client;
    CouchbaseDatastore couchbaseDatastore;
    private boolean errorOccured = false;
    private Gson gson;


    public CouchbaseSession(CouchbaseDatastore datastore, MappingContext mappingContext, ApplicationEventPublisher publisher) {
        this(datastore, mappingContext, publisher, false);
        this.gson = new Gson();
    }

    public CouchbaseSession(CouchbaseDatastore datastore, MappingContext mappingContext, ApplicationEventPublisher publisher, boolean stateless) {
        super(datastore, mappingContext, publisher, stateless);
        couchbaseDatastore = datastore;
        this.gson = new Gson();
        try {
            connect(datastore.couchbaseOptions);
        }
        catch (IllegalStateException ignored) {
            // can't call authenticate() twice, and it's probably been called at startup
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect(Map<String, String> connectionDetails) throws IOException {
        log.info("Connecting to Couchbase Server");
        ArrayList<URI> uriList = new ArrayList<URI>();

        String u = connectionDetails.get("uris");
        String[] uris = u.split(";");

        for(String uri: uris) {
            uriList.add(URI.create(uri));
        }

        client = new CouchbaseClient(
                uriList,
                connectionDetails.get("bucket"),
                connectionDetails.get("password")
        );

        log.info("Connection made");

    }

    public Gson getGsonInstance() {
        return this.gson;
    }

    @Override
    public CouchbaseQuery createQuery(@SuppressWarnings("rawtypes") Class type) {
        return (CouchbaseQuery) super.createQuery(type);
    }

    @Override
    protected Transaction<CouchbaseClient> beginTransactionInternal() {
        return new SessionOnlyTransaction<CouchbaseClient>(getNativeInterface(),this);
    }

    @Override
    public void flush() {
        if (!errorOccured) {
            super.flush();
        }
    }

    @Override
    protected Persister createPersister(Class cls, MappingContext mappingContext) {
        final PersistentEntity entity = mappingContext.getPersistentEntity(cls.getName());
        return entity == null ? null : new CouchbaseEntityPersister(mappingContext, entity, this, publisher);
    }

    @Override
    public void disconnect() {
        super.disconnect();
        getNativeInterface().shutdown();
    }

    public CouchbaseClient getNativeInterface() {
        return client;
    }
}
