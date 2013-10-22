package org.grails.datastore.mapping.couchbase.engine

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class DBObject {

    Map keyValue = [:]

    void put(String key, Object value) {
        keyValue.put(key, value)

    }

    Object get(String key) {

        if( keyValue.containsKey(key)) {
            return keyValue.get(key)
        }
        return null
    }

    void removeField(String key) {

        if(keyValue.containsKey(key)) {
            keyValue.remove(key)
        }
    }

    void putAll( Map m) {
        m.keySet().each {
            keyValue.put(it, m.get(it))
        }
    }

}
