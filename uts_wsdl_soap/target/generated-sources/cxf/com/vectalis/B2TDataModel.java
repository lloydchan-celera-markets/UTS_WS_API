package com.vectalis;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.7
 * 2017-03-06T13:05:49.101+08:00
 * Generated source version: 3.1.7
 * 
 */
@WebServiceClient(name = "B2T_DataModel", 
                  wsdlLocation = "file:/home/lloyd/git/UTS_WS_API/uts_wsdl_soap/src/main/resources/wsdl/MainServer_DirectAccess__Broker.wsdl",
                  targetNamespace = "http://www.Vectalis.com/") 
public class B2TDataModel extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.Vectalis.com/", "B2T_DataModel");
    public final static QName B2TDataModelSoap12 = new QName("http://www.Vectalis.com/", "B2T_DataModelSoap12");
    public final static QName B2TDataModelSoap = new QName("http://www.Vectalis.com/", "B2T_DataModelSoap");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/lloyd/git/UTS_WS_API/uts_wsdl_soap/src/main/resources/wsdl/MainServer_DirectAccess__Broker.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(B2TDataModel.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/lloyd/git/UTS_WS_API/uts_wsdl_soap/src/main/resources/wsdl/MainServer_DirectAccess__Broker.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public B2TDataModel(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public B2TDataModel(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public B2TDataModel() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public B2TDataModel(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public B2TDataModel(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public B2TDataModel(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns B2TDataModelSoap
     */
    @WebEndpoint(name = "B2T_DataModelSoap12")
    public B2TDataModelSoap getB2TDataModelSoap12() {
        return super.getPort(B2TDataModelSoap12, B2TDataModelSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns B2TDataModelSoap
     */
    @WebEndpoint(name = "B2T_DataModelSoap12")
    public B2TDataModelSoap getB2TDataModelSoap12(WebServiceFeature... features) {
        return super.getPort(B2TDataModelSoap12, B2TDataModelSoap.class, features);
    }


    /**
     *
     * @return
     *     returns B2TDataModelSoap
     */
    @WebEndpoint(name = "B2T_DataModelSoap")
    public B2TDataModelSoap getB2TDataModelSoap() {
        return super.getPort(B2TDataModelSoap, B2TDataModelSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns B2TDataModelSoap
     */
    @WebEndpoint(name = "B2T_DataModelSoap")
    public B2TDataModelSoap getB2TDataModelSoap(WebServiceFeature... features) {
        return super.getPort(B2TDataModelSoap, B2TDataModelSoap.class, features);
    }

}
