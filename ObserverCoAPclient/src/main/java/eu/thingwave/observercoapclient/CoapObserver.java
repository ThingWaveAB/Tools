/*
 * The MIT License
 *
 * Copyright 2016 Pablo Puñal Pereira <pablo.punal@thingwave.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.thingwave.observercoapclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@thingwave.eu>
 */
public abstract class CoapObserver {
    private final String uri;
    private final CoapHandler coapHandler;
    private CoapObserveRelation relation;
    private CoapClient coapClient;
    
    public CoapObserver(String uri) {
        Logger.getGlobal().setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.config.NetworkConfig").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.CoapEndpoint").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.EndpointManager").setLevel(Level.OFF);
        this.uri = uri;
        coapHandler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                incomingData(response);
            }

            @Override
            public void onError() {
                error();
            }
        };
    }
    
    public void startObserver() {
        coapClient = new CoapClient(uri);
        coapClient.setTimeout(60);
        System.out.println("Starting to observe "+coapClient.getURI());
        relation = coapClient.observe(coapHandler);
    }
    
    public void stopObserver() {
        
        System.out.println("Stoping to observe "+coapClient.getURI());
        if (relation!=null) relation.reactiveCancel();
    }
    
    abstract public void incomingData(CoapResponse response);
    abstract public void error();
}
