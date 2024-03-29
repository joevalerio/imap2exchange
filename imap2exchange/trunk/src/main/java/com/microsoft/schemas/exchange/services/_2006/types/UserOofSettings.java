
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 Yale University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * 
 * Redistribution and use of this software in source or binary forms,
 * with or without modification, are permitted, provided that the
 * following conditions are met:
 * 
 * 1. Any redistribution must include the above copyright notice and
 * disclaimer and this list of conditions in any related documentation
 * and, if feasible, in the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product
 * includes software developed by Yale University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "Yale" and "Yale University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *

 * 
 * <p>Java class for UserOofSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserOofSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OofState" type="{http://schemas.microsoft.com/exchange/services/2006/types}OofState"/>
 *         &lt;element name="ExternalAudience" type="{http://schemas.microsoft.com/exchange/services/2006/types}ExternalAudience"/>
 *         &lt;element name="Duration" type="{http://schemas.microsoft.com/exchange/services/2006/types}Duration" minOccurs="0"/>
 *         &lt;element name="InternalReply" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyBody" minOccurs="0"/>
 *         &lt;element name="ExternalReply" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyBody" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserOofSettings", propOrder = {
    "oofState",
    "externalAudience",
    "duration",
    "internalReply",
    "externalReply"
})
public class UserOofSettings {

    @XmlElement(name = "OofState", required = true)
    protected OofState oofState;
    @XmlElement(name = "ExternalAudience", required = true)
    protected ExternalAudience externalAudience;
    @XmlElement(name = "Duration")
    protected Duration duration;
    @XmlElement(name = "InternalReply")
    protected ReplyBody internalReply;
    @XmlElement(name = "ExternalReply")
    protected ReplyBody externalReply;

    /**
     * Gets the value of the oofState property.
     * 
     * @return
     *     possible object is
     *     {@link OofState }
     *     
     */
    public OofState getOofState() {
        return oofState;
    }

    /**
     * Sets the value of the oofState property.
     * 
     * @param value
     *     allowed object is
     *     {@link OofState }
     *     
     */
    public void setOofState(OofState value) {
        this.oofState = value;
    }

    /**
     * Gets the value of the externalAudience property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalAudience }
     *     
     */
    public ExternalAudience getExternalAudience() {
        return externalAudience;
    }

    /**
     * Sets the value of the externalAudience property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalAudience }
     *     
     */
    public void setExternalAudience(ExternalAudience value) {
        this.externalAudience = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDuration(Duration value) {
        this.duration = value;
    }

    /**
     * Gets the value of the internalReply property.
     * 
     * @return
     *     possible object is
     *     {@link ReplyBody }
     *     
     */
    public ReplyBody getInternalReply() {
        return internalReply;
    }

    /**
     * Sets the value of the internalReply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplyBody }
     *     
     */
    public void setInternalReply(ReplyBody value) {
        this.internalReply = value;
    }

    /**
     * Gets the value of the externalReply property.
     * 
     * @return
     *     possible object is
     *     {@link ReplyBody }
     *     
     */
    public ReplyBody getExternalReply() {
        return externalReply;
    }

    /**
     * Sets the value of the externalReply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplyBody }
     *     
     */
    public void setExternalReply(ReplyBody value) {
        this.externalReply = value;
    }

}
