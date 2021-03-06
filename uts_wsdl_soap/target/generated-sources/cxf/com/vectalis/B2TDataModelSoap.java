package com.vectalis;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.7
 * 2017-03-06T13:05:49.066+08:00
 * Generated source version: 3.1.7
 * 
 */
@WebService(targetNamespace = "http://www.Vectalis.com/", name = "B2T_DataModelSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface B2TDataModelSoap {

    /**
     * Ping Main server
     */
    @WebMethod(operationName = "Ping", action = "http://www.Vectalis.com/Ping")
    @RequestWrapper(localName = "Ping", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.Ping")
    @ResponseWrapper(localName = "PingResponse", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.PingResponse")
    @WebResult(name = "PingResult", targetNamespace = "http://www.Vectalis.com/")
    public java.lang.String ping();

    /**
     * Retrieve all the quotes of your company since the last call - Broker Direct Access only
     */
    @WebMethod(operationName = "GetAllMyEntityQuotesDirectAccessDelta", action = "http://www.Vectalis.com/GetAllMyEntityQuotesDirectAccessDelta")
    @RequestWrapper(localName = "GetAllMyEntityQuotesDirectAccessDelta", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyEntityQuotesDirectAccessDelta")
    @ResponseWrapper(localName = "GetAllMyEntityQuotesDirectAccessDeltaResponse", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyEntityQuotesDirectAccessDeltaResponse")
    @WebResult(name = "GetAllMyEntityQuotesDirectAccessDeltaResult", targetNamespace = "http://www.Vectalis.com/")
    public java.lang.String getAllMyEntityQuotesDirectAccessDelta(
        @WebParam(name = "xmlDocumentString", targetNamespace = "http://www.Vectalis.com/")
        java.lang.String xmlDocumentString
    );

    /**
     * Retrieve all the quotes of your company - Broker Direct Access only
     */
    @WebMethod(operationName = "GetAllMyEntityQuotesDirectAccess", action = "http://www.Vectalis.com/GetAllMyEntityQuotesDirectAccess")
    @RequestWrapper(localName = "GetAllMyEntityQuotesDirectAccess", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyEntityQuotesDirectAccess")
    @ResponseWrapper(localName = "GetAllMyEntityQuotesDirectAccessResponse", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyEntityQuotesDirectAccessResponse")
    @WebResult(name = "GetAllMyEntityQuotesDirectAccessResult", targetNamespace = "http://www.Vectalis.com/")
    public java.lang.String getAllMyEntityQuotesDirectAccess(
        @WebParam(name = "xmlDocumentString", targetNamespace = "http://www.Vectalis.com/")
        java.lang.String xmlDocumentString
    );

    /**
     * Impact the data from the DataModel for DirectAccess (Login and Logout only)
     */
    @WebMethod(operationName = "UpdateDirectAccess", action = "http://www.Vectalis.com/UpdateDirectAccess")
    @RequestWrapper(localName = "UpdateDirectAccess", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.UpdateDirectAccess")
    @ResponseWrapper(localName = "UpdateDirectAccessResponse", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.UpdateDirectAccessResponse")
    @WebResult(name = "UpdateDirectAccessResult", targetNamespace = "http://www.Vectalis.com/")
    public java.lang.String updateDirectAccess(
        @WebParam(name = "xmlDocumentString", targetNamespace = "http://www.Vectalis.com/")
        java.lang.String xmlDocumentString
    );

    /**
     * Retrieve all the replies sent to your company - Sales and Broker DirectAccess Only
     */
    @WebMethod(operationName = "GetAllMyRepliesDirectAccess", action = "http://www.Vectalis.com/GetAllMyRepliesDirectAccess")
    @RequestWrapper(localName = "GetAllMyRepliesDirectAccess", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyRepliesDirectAccess")
    @ResponseWrapper(localName = "GetAllMyRepliesDirectAccessResponse", targetNamespace = "http://www.Vectalis.com/", className = "com.vectalis.GetAllMyRepliesDirectAccessResponse")
    @WebResult(name = "GetAllMyRepliesDirectAccessResult", targetNamespace = "http://www.Vectalis.com/")
    public java.lang.String getAllMyRepliesDirectAccess(
        @WebParam(name = "xmlDocumentString", targetNamespace = "http://www.Vectalis.com/")
        java.lang.String xmlDocumentString
    );
}
