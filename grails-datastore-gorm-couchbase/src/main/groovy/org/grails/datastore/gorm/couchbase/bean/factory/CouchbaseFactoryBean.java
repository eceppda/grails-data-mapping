package org.grails.datastore.gorm.couchbase.bean.factory;

import com.couchbase.client.CouchbaseClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.grails.datastore.mapping.couchbase.config.CouchbaseClientOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.net.UnknownHostException;

/**
 * @author Jeff Hemminger <jeff@kropek.org>
 */
public class CouchbaseFactoryBean implements FactoryBean<CouchbaseClient>, InitializingBean/*,
    PersistenceExceptionTranslator*/  {

    /**
     * Logger, available to subclasses.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private CouchbaseClient client;
    private CouchbaseClientOptions couchbaseOptions;
    private String host;
    private Integer port;

    public void setCouchbaseOptions(CouchbaseClientOptions couchbaseOptions) {
        this.couchbaseOptions = couchbaseOptions;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CouchbaseClient getObject() throws Exception {
        Assert.notNull(client, "CouchbaseClient must not be null");
        return client;
    }

    public Class<? extends CouchbaseClient> getObjectType() {
        return CouchbaseClient.class;
    }

    public boolean isSingleton() {
        return false;
    }

    public void afterPropertiesSet() throws UnknownHostException {
        // apply defaults - convenient when used to configure for tests
        // in an application context
        if (client != null) {
            return;
        }

    }

}
