/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.client.methods;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.annotation.NotThreadSafe;

import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Basic implementation of an HTTP request that can be modified. Methods of the
 * {@link AbortableHttpRequest} interface implemented by this class are thread safe.
 *
 * @since 4.0
 */
@NotThreadSafe
public abstract class HttpRequestBase extends AbstractHttpMessage
    implements HttpUriRequest, AbortableHttpRequest, Cloneable {

    /** The abort lock. */
    private Lock abortLock;
    
    /** The aborted. */
    private volatile boolean aborted;

    /** The uri. */
    private URI uri;
    
    /** The conn request. */
    private ClientConnectionRequest connRequest;
    
    /** The release trigger. */
    private ConnectionReleaseTrigger releaseTrigger;

    /**
	 * Instantiates a new http request base.
	 */
    public HttpRequestBase() {
        super();
        this.abortLock = new ReentrantLock();
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.methods.HttpUriRequest#getMethod()
     */
    public abstract String getMethod();

    /* (non-Javadoc)
     * @see org.apache.http.HttpMessage#getProtocolVersion()
     */
    public ProtocolVersion getProtocolVersion() {
        return HttpProtocolParams.getVersion(getParams());
    }

    /**
	 * Returns the original request URI.
	 * <p>
	 * Please note URI remains unchanged in the course of request execution and
	 * is not updated if the request is redirected to another location.
	 * 
	 * @return the uri
	 */
    public URI getURI() {
        return this.uri;
    }

    /* (non-Javadoc)
     * @see org.apache.http.HttpRequest#getRequestLine()
     */
    public RequestLine getRequestLine() {
        String method = getMethod();
        ProtocolVersion ver = getProtocolVersion();
        URI uri = getURI();
        String uritext = null;
        if (uri != null) {
            uritext = uri.toASCIIString();
        }
        if (uritext == null || uritext.length() == 0) {
            uritext = "/";
        }
        return new BasicRequestLine(method, uritext, ver);
    }

    /**
	 * Sets the uri.
	 * 
	 * @param uri
	 *            the new uri
	 */
    public void setURI(final URI uri) {
        this.uri = uri;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.methods.AbortableHttpRequest#setConnectionRequest(org.apache.http.conn.ClientConnectionRequest)
     */
    public void setConnectionRequest(final ClientConnectionRequest connRequest)
            throws IOException {
        if (this.aborted) {
            throw new IOException("Request already aborted");
        }
        this.abortLock.lock();
        try {
            this.connRequest = connRequest;
        } finally {
            this.abortLock.unlock();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.methods.AbortableHttpRequest#setReleaseTrigger(org.apache.http.conn.ConnectionReleaseTrigger)
     */
    public void setReleaseTrigger(final ConnectionReleaseTrigger releaseTrigger)
            throws IOException {
        if (this.aborted) {
            throw new IOException("Request already aborted");
        }
        this.abortLock.lock();
        try {
            this.releaseTrigger = releaseTrigger;
        } finally {
            this.abortLock.unlock();
        }
    }

    /**
	 * Cleanup.
	 */
    private void cleanup() {
        if (this.connRequest != null) {
            this.connRequest.abortRequest();
            this.connRequest = null;
        }
        if (this.releaseTrigger != null) {
            try {
                this.releaseTrigger.abortConnection();
            } catch (IOException ex) {
            }
            this.releaseTrigger = null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.methods.HttpUriRequest#abort()
     */
    public void abort() {
        if (this.aborted) {
            return;
        }
        this.abortLock.lock();
        try {
            this.aborted = true;
            cleanup();
        } finally {
            this.abortLock.unlock();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.methods.HttpUriRequest#isAborted()
     */
    public boolean isAborted() {
        return this.aborted;
    }

    /**
     * Resets internal state of the request making it reusable.
     *
     * @since 4.2
     */
    public void reset() {
        this.abortLock.lock();
        try {
            cleanup();
            this.aborted = false;
        } finally {
            this.abortLock.unlock();
        }
    }

    /**
     * A convenience method to simplify migration from HttpClient 3.1 API. This method is
     * equivalent to {@link #reset()}.
     *
     * @since 4.2
     */
    public void releaseConnection() {
        reset();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        HttpRequestBase clone = (HttpRequestBase) super.clone();
        clone.abortLock = new ReentrantLock();
        clone.aborted = false;
        clone.releaseTrigger = null;
        clone.connRequest = null;
        clone.headergroup = (HeaderGroup) CloneUtils.clone(this.headergroup);
        clone.params = (HttpParams) CloneUtils.clone(this.params);
        return clone;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getMethod() + " " + getURI() + " " + getProtocolVersion();
    }    

}
