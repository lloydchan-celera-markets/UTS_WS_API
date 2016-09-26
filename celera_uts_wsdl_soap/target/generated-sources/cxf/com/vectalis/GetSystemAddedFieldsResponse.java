
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
 *         &lt;element name="GetSystemAddedFieldsResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
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
    "getSystemAddedFieldsResult"
})
@XmlRootElement(name = "GetSystemAddedFieldsResponse")
public class GetSystemAddedFieldsResponse {

    @XmlElement(name = "GetSystemAddedFieldsResult")
    protected byte[] getSystemAddedFieldsResult;

    /**
     * Gets the value of the getSystemAddedFieldsResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetSystemAddedFieldsResult() {
        return getSystemAddedFieldsResult;
    }

    /**
     * Sets the value of the getSystemAddedFieldsResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetSystemAddedFieldsResult(byte[] value) {
        this.getSystemAddedFieldsResult = value;
    }

}
