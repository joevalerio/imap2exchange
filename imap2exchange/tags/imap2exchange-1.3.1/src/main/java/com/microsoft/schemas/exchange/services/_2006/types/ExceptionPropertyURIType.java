
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
 * <p>Java class for ExceptionPropertyURIType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExceptionPropertyURIType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="attachment:Name"/>
 *     &lt;enumeration value="attachment:ContentType"/>
 *     &lt;enumeration value="attachment:Content"/>
 *     &lt;enumeration value="recurrence:Month"/>
 *     &lt;enumeration value="recurrence:DayOfWeekIndex"/>
 *     &lt;enumeration value="recurrence:DaysOfWeek"/>
 *     &lt;enumeration value="recurrence:DayOfMonth"/>
 *     &lt;enumeration value="recurrence:Interval"/>
 *     &lt;enumeration value="recurrence:NumberOfOccurrences"/>
 *     &lt;enumeration value="timezone:Offset"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExceptionPropertyURIType")
@XmlEnum
public enum ExceptionPropertyURIType {

    @XmlEnumValue("attachment:Name")
    ATTACHMENT_NAME("attachment:Name"),
    @XmlEnumValue("attachment:ContentType")
    ATTACHMENT_CONTENT_TYPE("attachment:ContentType"),
    @XmlEnumValue("attachment:Content")
    ATTACHMENT_CONTENT("attachment:Content"),
    @XmlEnumValue("recurrence:Month")
    RECURRENCE_MONTH("recurrence:Month"),
    @XmlEnumValue("recurrence:DayOfWeekIndex")
    RECURRENCE_DAY_OF_WEEK_INDEX("recurrence:DayOfWeekIndex"),
    @XmlEnumValue("recurrence:DaysOfWeek")
    RECURRENCE_DAYS_OF_WEEK("recurrence:DaysOfWeek"),
    @XmlEnumValue("recurrence:DayOfMonth")
    RECURRENCE_DAY_OF_MONTH("recurrence:DayOfMonth"),
    @XmlEnumValue("recurrence:Interval")
    RECURRENCE_INTERVAL("recurrence:Interval"),
    @XmlEnumValue("recurrence:NumberOfOccurrences")
    RECURRENCE_NUMBER_OF_OCCURRENCES("recurrence:NumberOfOccurrences"),
    @XmlEnumValue("timezone:Offset")
    TIMEZONE_OFFSET("timezone:Offset");
    private final String value;

    ExceptionPropertyURIType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExceptionPropertyURIType fromValue(String v) {
        for (ExceptionPropertyURIType c: ExceptionPropertyURIType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
