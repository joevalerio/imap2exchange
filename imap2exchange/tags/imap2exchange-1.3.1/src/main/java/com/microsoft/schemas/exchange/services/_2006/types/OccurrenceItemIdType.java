
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * <p>Java class for OccurrenceItemIdType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OccurrenceItemIdType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}BaseItemIdType">
 *       &lt;attribute name="RecurringMasterId" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}DerivedItemIdType" />
 *       &lt;attribute name="ChangeKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="InstanceIndex" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OccurrenceItemIdType")
public class OccurrenceItemIdType
    extends BaseItemIdType
{

    @XmlAttribute(name = "RecurringMasterId", required = true)
    protected String recurringMasterId;
    @XmlAttribute(name = "ChangeKey")
    protected String changeKey;
    @XmlAttribute(name = "InstanceIndex", required = true)
    protected int instanceIndex;

    /**
     * Gets the value of the recurringMasterId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecurringMasterId() {
        return recurringMasterId;
    }

    /**
     * Sets the value of the recurringMasterId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecurringMasterId(String value) {
        this.recurringMasterId = value;
    }

    /**
     * Gets the value of the changeKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangeKey() {
        return changeKey;
    }

    /**
     * Sets the value of the changeKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangeKey(String value) {
        this.changeKey = value;
    }

    /**
     * Gets the value of the instanceIndex property.
     * 
     */
    public int getInstanceIndex() {
        return instanceIndex;
    }

    /**
     * Sets the value of the instanceIndex property.
     * 
     */
    public void setInstanceIndex(int value) {
        this.instanceIndex = value;
    }

}