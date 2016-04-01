[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md) | [Example3](Example3.md) | [imap2exchange-config.xml](e3_imap2exchange_config_xml.md)

# imap2exchange-config.xml #

```
<?xml version="1.0" encoding="UTF-8"?>
<beans
   xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:utils="http://www.chariotsolutions.com/spring/schema/utils"
   xsi:schemaLocation="http://www.springframework.org/schema/beans
                       http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

	<!-- Property Overrider
	     ==========================================================-->
        <bean id="propertyOverrideConfigurer"
              class="edu.yale.its.tp.email.conversion.spring.MyPropertyOverrideConfigurer">
            <property name="location" value="file:${EXCHANGE_CONVERSION_HOME}/config/imap2exchange-config.properties"/>
            <property name="ignoreResourceNotFound" value="true"/>
        </bean>

	<!-- Exchange Conversion Manager
	     Each Conversion is its own thread, so this is a Thread Manager
	     ==========================================================-->
	<bean id="exchangeConversionManager"
	      class="edu.yale.its.tp.email.conversion.ExchangeConversionManager"
	      scope="singleton">
	      <property name="maxCachedThreads" value="1024" />
		  <property name="maxRunningThreads" value="3" />
	</bean>
	
	<!-- Exchange Server Port Factory
	     These values are set in imap2Exchange-config.properties
	     for ease of use.
	     ==========================================================-->
        <bean id="exchangeServerImpl" 
              class="edu.yale.its.tp.email.conversion.yale.YaleExchangeServerPortFactory"
              scope="singleton"/>
          
        <alias name="exchangeServerImpl" alias="exchangeServer" />

	<!-- Exchange Conversion Factory
	     ==========================================================-->
	<bean id="exchangeConversionFactory"
	      class="edu.yale.its.tp.email.conversion.ExchangeConversionFactory"
	      scope="singleton">
	      <property name="maxMessageGrpSize" value="5242880" />
	      <property name="maxMessageSize" value="20971520" />
              <property name="pluggableConversionActions">
		<map>
	            <entry key="userSetupAction">
			<ref bean="userSetupAction"/>
		    </entry>
		    <entry key="preConversionAction">
			<ref bean="preConversionAction"/>
		    </entry>
		</map>          
              </property>
              <property name="altNames" ref="altNames" /> 
	</bean>

	<!-- Pluggable Conversion Actions
	     ==========================================================-->
        <bean name="userSetupActionImpl"
              class="edu.yale.its.tp.email.conversion.util.GenericUserSetupAction"
              scope="singleton" />

        <alias name="userSetupActionImpl"
               alias="userSetupAction" />

        <bean id="preConversionActionImpl"
              class="edu.yale.its.tp.email.conversion.yale.YalePreConversionAction"
              scope="singleton" />

        <alias name="preConversionActionImpl"
               alias="preConversionAction" />

	<!-- User Factory
	     ==========================================================-->
        <alias name="userFactoryImpl"
               alias="userFactory"/>
    
        <bean id="userFactoryImpl"
              class="edu.yale.its.tp.email.conversion.yale.YaleUserFactory"
              scope="singleton"/>
          
	<!-- AD 
	     These values are set in imap2Exchange-config.properties
	     for ease of use.
	     ==========================================================-->
        <bean id="ad" 
              class="edu.yale.its.tp.email.conversion.yale.YaleAD"
              scope="singleton" />

        <!-- Folder Alt Names
             This section is used to define imap folders that are to be merged
             into Exchange system folders.
             ==========================================================-->
        <bean id="altNames"
              class="edu.yale.its.tp.email.conversion.imap.FolderAltNames"
              scope="singleton">
            <property name="altNames">          
                <list>
                    <ref local="deletedAltName"/>
                    <ref local="spamAltName"/>
                </list>
            </property>
	</bean>
	
	<bean id="deletedAltName"          
	      class="edu.yale.its.tp.email.conversion.imap.FolderAltName">
	    <property name="exchangeFolderName" value="Deleted Items"/>
	        <property name="imapFolderNames">
		    <list>
		        <value>trash</value>
		    </list>
		</property>
	</bean>
	
	<bean id="spamAltName"          
              class="edu.yale.its.tp.email.conversion.imap.FolderAltName">
            <property name="exchangeFolderName" value="Junk E-Mail"/>
	        <property name="imapFolderNames">
		    <list>
		        <value>tagged-spam</value>
		    </list>
		</property>
	</bean>

</beans>

```