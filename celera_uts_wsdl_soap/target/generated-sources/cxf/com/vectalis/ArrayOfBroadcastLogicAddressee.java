
package com.vectalis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfBroadcastLogicAddressee complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfBroadcastLogicAddressee"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BroadcastLogicAddressee" type="{http://www.Vectalis.com/}BroadcastLogicAddressee" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfBroadcastLogicAddressee", propOrder = {
    "broadcastLogicAddressee"
})
public class ArrayOfBroadcastLogicAddressee {

    @XmlElement(name = "BroadcastLogicAddressee", nillable = true)
    protected List<BroadcastLogicAddressee> broadcastLogicAddressee;

    /**
     * Gets the value of the broadcastLogicAddressee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the broadcastLogicAddressee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBroadcastLogicAddressee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BroadcastLogicAddressee }
     * 
     * 
     */
    public List<BroadcastLogicAddressee> getBroadcastLogicAddressee() {
        if (broadcastLogicAddressee == null) {
            broadcastLogicAddressee = new ArrayList<BroadcastLogicAddressee>();
        }
        return this.broadcastLogicAddressee;
    }

}
