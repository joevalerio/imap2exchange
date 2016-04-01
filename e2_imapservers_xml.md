[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md) | [Example2](Example2.md) | [imapservers.xml](e2_imapservers_xml.md)

# imapservers.xml #

```
<?xml version="1.0" encoding="UTF-8"?>
<beans
   xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:utils="http://www.chariotsolutions.com/spring/schema/utils"
   xsi:schemaLocation="http://www.springframework.org/schema/beans
                       http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">


    <bean id="imapServerFactory" 
          parent="imapServerFactoryImpl" />

    <bean id="imapServerFactoryImpl" 
          class="edu.yale.its.tp.email.conversion.yale.YaleImapServerFactory"
          scope="singleton">
          <property name="connectionPoolSize" value="50"/>
          <property name="imapServers">
          	  <list>
			<!-- Ref to Imap Servers -->    
		       	<ref local="imapServer1"/>
		       	<ref local="imapServer2"/>
		       	<ref local="imapServer3"/>
          	  </list>
          </property>
    </bean>

    <!-- IMAP Servers -->    
    <bean id="imapServer1" class="edu.yale.its.tp.email.conversion.imap.ImapServer">
    	<property name="sizer"><ref bean="centralImapMailboxSizer"/></property>
    </bean>

    <bean id="imapServer2" class="edu.yale.its.tp.email.conversion.imap.ImapServer">
    	<property name="sizer"><ref bean="centralImapMailboxSizer"/></property>
    </bean>

    <bean id="imapServer3" class="edu.yale.its.tp.email.conversion.imap.ImapServer">
    	<property name="sizer"><ref bean="medImapMailboxSizer"/></property>
    </bean>

</beans>
```