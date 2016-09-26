
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
 *         &lt;element name="IsDoubleLoginWarningMessageDisplayedResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "isDoubleLoginWarningMessageDisplayedResult"
})
@XmlRootElement(name = "IsDoubleLoginWarningMessageDisplayedResponse")
public class IsDoubleLoginWarningMessageDisplayedResponse {

    @XmlElement(name = "IsDoubleLoginWarningMessageDisplayedResult")
    protected boolean isDoubleLoginWarningMessageDisplayedResult;

    /**
     * Gets the value of the isDoubleLoginWarningMessageDisplayedResult property.
     * 
     */
    public boolean isIsDoubleLoginWarningMessageDisplayedResult() {
        return isDoubleLoginWarningMessageDisplayedResult;
    }

    /**
     * Sets the value of the isDoubleLoginWarningMessageDisplayedResult property.
     * 
     */
    public void setIsDoubleLoginWarningMessageDisplayedResult(boolean value) {
        this.isDoubleLoginWarningMessageDisplayedResult = value;
    }

}
