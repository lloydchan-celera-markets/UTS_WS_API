
package com.vectalis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetAllMyRepliesBrokerDirectAccessResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getAllMyRepliesBrokerDirectAccessResult"
})
@XmlRootElement(name = "GetAllMyRepliesBrokerDirectAccessResponse")
public class GetAllMyRepliesBrokerDirectAccessResponse {

    @XmlElement(name = "GetAllMyRepliesBrokerDirectAccessResult")
    protected String getAllMyRepliesBrokerDirectAccessResult;

    /**
     * Gets the value of the getAllMyRepliesBrokerDirectAccessResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAllMyRepliesBrokerDirectAccessResult() {
        return getAllMyRepliesBrokerDirectAccessResult;
    }

    /**
     * Sets the value of the getAllMyRepliesBrokerDirectAccessResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAllMyRepliesBrokerDirectAccessResult(String value) {
        this.getAllMyRepliesBrokerDirectAccessResult = value;
    }

}
