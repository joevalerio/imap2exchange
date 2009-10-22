
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
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
 * <p>Java class for DistinguishedPropertySetType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DistinguishedPropertySetType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Meeting"/>
 *     &lt;enumeration value="Appointment"/>
 *     &lt;enumeration value="Common"/>
 *     &lt;enumeration value="PublicStrings"/>
 *     &lt;enumeration value="Address"/>
 *     &lt;enumeration value="InternetHeaders"/>
 *     &lt;enumeration value="CalendarAssistant"/>
 *     &lt;enumeration value="UnifiedMessaging"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DistinguishedPropertySetType")
@XmlEnum
public enum DistinguishedPropertySetType {

    @XmlEnumValue("Meeting")
    MEETING("Meeting"),
    @XmlEnumValue("Appointment")
    APPOINTMENT("Appointment"),
    @XmlEnumValue("Common")
    COMMON("Common"),
    @XmlEnumValue("PublicStrings")
    PUBLIC_STRINGS("PublicStrings"),
    @XmlEnumValue("Address")
    ADDRESS("Address"),
    @XmlEnumValue("InternetHeaders")
    INTERNET_HEADERS("InternetHeaders"),
    @XmlEnumValue("CalendarAssistant")
    CALENDAR_ASSISTANT("CalendarAssistant"),
    @XmlEnumValue("UnifiedMessaging")
    UNIFIED_MESSAGING("UnifiedMessaging");
    private final String value;

    DistinguishedPropertySetType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DistinguishedPropertySetType fromValue(String v) {
        for (DistinguishedPropertySetType c: DistinguishedPropertySetType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
