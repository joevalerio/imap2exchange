[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md) | [Example1](Example1.md)

# vanilla batch conversion example #

In this example I have configured the tool to do a plain vanilla batch conversion.

There are four configuration files that are used to initialize the environment via Spring, and the user file.  They are:

| **File** | **Purpose** |
|:---------|:------------|
|[imap2exchange-config.xml](e1_imap2exchange_config_xml.md)|This file creates the object factories and their relationships.|
|[imap2exchange-yale-config.xml](e1_imap2exchange_yale_config_xml.md)|This file creates yale specific implementations.  You may choose to use these.|
|[imapservers.xml](e1_imapservers_xml.md)|This is where the imapservers are configured to their ImapServerFactory|
|[imap2exchange-config.properties](e1_imap2exchange_config_properties.md)| This file is the property file that sets the values of all the factories and object defined in the the above spring configuration files.|
|[batchUsers](e1_batchUsers.md)| This is the file that the batch loader uses.|