
package com.vectalis.utstestserver._8080.mainserver;

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
 *         &lt;element name="GetAllMyEntityQuotesDirectAccessResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getAllMyEntityQuotesDirectAccessResult"
})
@XmlRootElement(name = "GetAllMyEntityQuotesDirectAccessResponse")
public class GetAllMyEntityQuotesDirectAccessResponse {

    @XmlElement(name = "GetAllMyEntityQuotesDirectAccessResult")
    protected String getAllMyEntityQuotesDirectAccessResult;

    /**
     * Gets the value of the getAllMyEntityQuotesDirectAccessResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAllMyEntityQuotesDirectAccessResult() {
        return getAllMyEntityQuotesDirectAccessResult;
    }

    /**
     * Sets the value of the getAllMyEntityQuotesDirectAccessResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAllMyEntityQuotesDirectAccessResult(String value) {
        this.getAllMyEntityQuotesDirectAccessResult = value;
    }

}
