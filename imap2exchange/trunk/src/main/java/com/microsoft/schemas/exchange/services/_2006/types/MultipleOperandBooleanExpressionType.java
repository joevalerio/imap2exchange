
package com.microsoft.schemas.exchange.services._2006.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
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
 * <p>Java class for MultipleOperandBooleanExpressionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultipleOperandBooleanExpressionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}SearchExpressionType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}SearchExpression" maxOccurs="unbounded" minOccurs="2"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultipleOperandBooleanExpressionType", propOrder = {
    "searchExpression"
})
@XmlSeeAlso({
    OrType.class,
    AndType.class
})
public abstract class MultipleOperandBooleanExpressionType
    extends SearchExpressionType
{

    @XmlElementRef(name = "SearchExpression", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", type = JAXBElement.class)
    protected List<JAXBElement<? extends SearchExpressionType>> searchExpression;

    /**
     * Gets the value of the searchExpression property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchExpression property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchExpression().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link IsEqualToType }{@code >}
     * {@link JAXBElement }{@code <}{@link IsGreaterThanOrEqualToType }{@code >}
     * {@link JAXBElement }{@code <}{@link ContainsExpressionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExistsType }{@code >}
     * {@link JAXBElement }{@code <}{@link NotType }{@code >}
     * {@link JAXBElement }{@code <}{@link IsGreaterThanType }{@code >}
     * {@link JAXBElement }{@code <}{@link SearchExpressionType }{@code >}
     * {@link JAXBElement }{@code <}{@link IsLessThanType }{@code >}
     * {@link JAXBElement }{@code <}{@link OrType }{@code >}
     * {@link JAXBElement }{@code <}{@link AndType }{@code >}
     * {@link JAXBElement }{@code <}{@link IsLessThanOrEqualToType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExcludesType }{@code >}
     * {@link JAXBElement }{@code <}{@link IsNotEqualToType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends SearchExpressionType>> getSearchExpression() {
        if (searchExpression == null) {
            searchExpression = new ArrayList<JAXBElement<? extends SearchExpressionType>>();
        }
        return this.searchExpression;
    }

}
