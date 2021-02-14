
package com.baeldung.soap.ws.client.generated;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.2
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CountryServiceImplService", targetNamespace = "http://server.ws.soap.baeldung.com/", wsdlLocation = "file:/D:/projetos/baeldung/tutorials/maven-modules/maven-plugins/jaxws/src/main/resources/country.wsdl")
public class CountryServiceImplService
    extends Service
{

    private final static URL COUNTRYSERVICEIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException COUNTRYSERVICEIMPLSERVICE_EXCEPTION;
    private final static QName COUNTRYSERVICEIMPLSERVICE_QNAME = new QName("http://server.ws.soap.baeldung.com/", "CountryServiceImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/D:/projetos/baeldung/tutorials/maven-modules/maven-plugins/jaxws/src/main/resources/country.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        COUNTRYSERVICEIMPLSERVICE_WSDL_LOCATION = url;
        COUNTRYSERVICEIMPLSERVICE_EXCEPTION = e;
    }

    public CountryServiceImplService() {
        super(__getWsdlLocation(), COUNTRYSERVICEIMPLSERVICE_QNAME);
    }

    public CountryServiceImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), COUNTRYSERVICEIMPLSERVICE_QNAME, features);
    }

    public CountryServiceImplService(URL wsdlLocation) {
        super(wsdlLocation, COUNTRYSERVICEIMPLSERVICE_QNAME);
    }

    public CountryServiceImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, COUNTRYSERVICEIMPLSERVICE_QNAME, features);
    }

    public CountryServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CountryServiceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CountryService
     */
    @WebEndpoint(name = "CountryServiceImplPort")
    public CountryService getCountryServiceImplPort() {
        return super.getPort(new QName("http://server.ws.soap.baeldung.com/", "CountryServiceImplPort"), CountryService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CountryService
     */
    @WebEndpoint(name = "CountryServiceImplPort")
    public CountryService getCountryServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://server.ws.soap.baeldung.com/", "CountryServiceImplPort"), CountryService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (COUNTRYSERVICEIMPLSERVICE_EXCEPTION!= null) {
            throw COUNTRYSERVICEIMPLSERVICE_EXCEPTION;
        }
        return COUNTRYSERVICEIMPLSERVICE_WSDL_LOCATION;
    }

}
