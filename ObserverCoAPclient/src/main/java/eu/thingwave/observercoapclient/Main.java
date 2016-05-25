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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.util.Duration.millis;
import org.eclipse.californium.core.CoapResponse;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@thingwave.eu>
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\nThingWave - CoAP observer client v0.0.2\n");
        
        if (args.length != 1) {
            System.out.println("Usage: ObserverCoAPclient.jar [endPoint]\nExample: ObserverCoAPclient.jar coap://localhost/observable\n");
            System.exit(1);
        }
        
        // Define a new CoapObserver with the pased uri
        CoapObserver coapObserver = new CoapObserver(args[0]) {
            @Override
            public void incomingData(CoapResponse response) {
                // Print directly on the screen the data
                System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())))
                        +" ["+response.getOptions().getContentFormat()+"]"+" ("+response.getCode()+") => "
                        +response.getResponseText());
            }
            @Override
            public void error() {
                System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())))+
                        "Error/Expired Observer");
            }
        };
        
        // To execute if exit
        Runtime.getRuntime().addShutdownHook(
            new Thread() {
                @Override
                public void run() {
                    coapObserver.stopObserver();
                    System.out.println("\n\nThanks for using our services.\nThingWave AB (www.thingwave.eu)\n\n");
                }
            }
        );
        
        // Start to Observe
        coapObserver.startObserver();
                
        while (true) { // Do nothing
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
