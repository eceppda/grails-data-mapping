package org.grails.datastore.mapping.couchbase.config

/**
 * Settings for a Couchbase connection instance.
 * @author Jeff Hemminger <jeff@kropek.org>
 */
class CouchbaseClientOptions {

    public static class Builder {

        int opTimeout = 2500
        int timeoutExceptionThreshold = 998
        int readBufSize	= 16384
        int opQueueMaxBlockTime	= 10000
        boolean shouldOptimize = false
        int maxReconnectDelay = 30000
        int minReconnectInterval = 1100
        int obsPollInterval	= 400
        int obsPollMax = 10

        public Builder opTimeout(final int opTimeout) {
            this.opTimeout = opTimeout
            return this
        }

        public Builder timeoutExceptionThreshold(final int timeoutExceptionThreshold) {
            this.timeoutExceptionThreshold = timeoutExceptionThreshold
            return this
        }

        public Builder readBufSize(final int readBufSize) {
            this.readBufSize = readBufSize
            return this
        }

        public Builder opQueueMaxBlockTime(final int opQueueMaxBlockTime) {
            this.opQueueMaxBlockTime = opQueueMaxBlockTime
            return this
        }

        public Builder shouldOptimize(final boolean shouldOptimize) {
            this.shouldOptimize = shouldOptimize
            return this
        }

        public Builder maxReconnectDaily(final int maxReconnectDaily) {
            this.maxReconnectDelay = maxReconnectDaily
            return this
        }

        public Builder minReconnectInterval(final int minReconnectInterval) {
            this.minReconnectInterval = minReconnectInterval
            return this
        }

        public Builder obsPollInterval(final int obsPollInterval) {
            this.obsPollInterval = obsPollInterval
            return this
        }

        public Builder obsPollMax(final int obsPollMax) {
            this.obsPollMax = obsPollMax
            return this
        }

        public CouchbaseClientOptions build() {
            return new CouchbaseClientOptions(this);
        }
    }
}
