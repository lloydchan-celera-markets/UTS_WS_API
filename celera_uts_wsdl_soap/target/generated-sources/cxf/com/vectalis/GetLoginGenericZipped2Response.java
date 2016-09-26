
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
 *         &lt;element name="GetLoginGenericZipped2Result" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
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
    "getLoginGenericZipped2Result"
})
@XmlRootElement(name = "GetLoginGenericZipped2Response")
public class GetLoginGenericZipped2Response {

    @XmlElement(name = "GetLoginGenericZipped2Result")
    protected byte[] getLoginGenericZipped2Result;

    /**
     * Gets the value of the getLoginGenericZipped2Result property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetLoginGenericZipped2Result() {
        return getLoginGenericZipped2Result;
    }

    /**
     * Sets the value of the getLoginGenericZipped2Result property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetLoginGenericZipped2Result(byte[] value) {
        this.getLoginGenericZipped2Result = value;
    }

}
