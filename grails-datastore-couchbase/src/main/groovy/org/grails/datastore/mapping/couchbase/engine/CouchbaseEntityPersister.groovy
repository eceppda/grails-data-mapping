package org.grails.datastore.mapping.couchbase.engine

import com.couchbase.client.CouchbaseClient
import com.google.gson.Gson
import org.grails.datastore.mapping.cache.TPCacheAdapterRepository
import org.grails.datastore.mapping.couchbase.CouchbaseSession
import org.grails.datastore.mapping.engine.AssociationIndexer
import org.grails.datastore.mapping.engine.EntityAccess
import org.grails.datastore.mapping.engine.NativeEntryEntityPersister
import org.grails.datastore.mapping.engine.PropertyValueIndexer
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association
import org.grails.datastore.mapping.query.Query
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher


/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseEntityPersister extends NativeEntryEntityPersister<DBObject, Object>  {

    private static final Logger log = LoggerFactory.getLogger(CouchbaseEntityPersister.class)
    private CouchbaseClient client
    private Gson gson
    public static final COUCHBASE_CLASS_FIELD = "_class"


    public CouchbaseEntityPersister(MappingContext mappingContext, PersistentEntity entity, CouchbaseSession session, ApplicationEventPublisher publisher) {
        super(mappingContext, entity, session, publisher)
        this.client = (CouchbaseClient)session.getNativeInterface()
        this.gson = session.getGsonInstance()
    }

    public CouchbaseEntityPersister(MappingContext mappingContext, PersistentEntity entity, CouchbaseSession session, ApplicationEventPublisher publisher, TPCacheAdapterRepository<Object> cacheAdapterRepository) {
        super(mappingContext, entity, session, publisher, cacheAdapterRepository)
        this.client = (CouchbaseClient)session.getNativeInterface()
        this.gson = session.getGsonInstance()
    }


    @Override
    public String getEntityFamily() {
        log.error("Not Implemented!")
        return null
    }

    @Override
    protected void deleteEntry(String family, Object key, Object entry) {
        log.warn("Deleting: " + (String)key)
        this.client.delete((String)key)
    }

    @Override
    protected Object generateIdentifier(PersistentEntity persistentEntity, DBObject entry) {
        log.info("Generating identity");
        PersistentProperty identity = persistentEntity.getIdentity();
        return identity.toString();
    }

    @Override
    public PropertyValueIndexer getPropertyIndexer(PersistentProperty property) {
        log.error("Not implemented");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AssociationIndexer getAssociationIndexer(DBObject nativeEntry, Association association) {
        log.error("Not implemented");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected DBObject createNewEntry(String family) {
        log.info("Creating identity")
        DBObject dbo = new DBObject()
        PersistentEntity persistentEntity = getPersistentEntity()
        dbo.put(COUCHBASE_CLASS_FIELD, persistentEntity.getDiscriminator())
        return dbo
    }

    @Override
    protected Object getEntryValue(DBObject nativeEntry, String property) {
        Object value = nativeEntry.get(property)
        return value
    }

    @Override
    protected void setEntryValue(DBObject nativeEntry, String key, Object value) {
        nativeEntry.put(key, value)
    }

    @Override
    protected DBObject retrieveEntry(PersistentEntity persistentEntity, String family, Serializable key) {
        String json = client.get((String)key)
        if(json != null) {
            def obj = this.gson.fromJson(json, Map.class)
            DBObject o = new DBObject()
            o.keyValue = obj
            return o
        }

        return null
    }

    @Override
    protected Object storeEntry(PersistentEntity persistentEntity, EntityAccess entityAccess, Object storeId, DBObject nativeEntry) {
        String key = (String)storeId;
        this.client.add(key,0,gson.toJson(nativeEntry.keyValue));
        return key;
    }

    @Override
    protected void updateEntry(PersistentEntity persistentEntity, EntityAccess entityAccess, Object key, DBObject entry) {
        String k = (String)key;
        this.client.set(k, 0, gson.toJson(entry.keyValue));
    }

    @Override
    protected void deleteEntries(String family, List<Object> keys) {
        for(Object k : keys) {
            String key = (String)k;
            this.client.delete(key);
        }

    }

    @Override
    public Query createQuery() {
        log.error("Not implemented");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
