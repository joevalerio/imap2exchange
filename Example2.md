[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md) | [Example2](Example2.md)

# batch conversion with mailbox size checker #

In this example I have configured the tool to do a plain vanilla batch conversion, plus I user Yale's ImapMailboxSizer.  If you want to use a mailbox sizer, you will have to implement one.  We have two different source email systems, one system has a web site that I modified to create a quick WS type page to get me the size of a users mailbox.  The other system, I rely on a flat file rsync'ed to my config directory with uids and their mailbox sizes.

| **File** | **Change** |
|:---------|:-----------|
|[imap2exchange-config.xml](e2_imap2exchange_config_xml.md) | Here you can see that I define the preConversionAction and inject it in the ExchangeConversionFactory|
|[imap2exchange-yale-config.xml](e2_imap2exchange_yale_config_xml.md)| Here you can see that I instantiate the two ImapMailboxSizers and alias the preConversionAction for sanity's sake in the properties file |
|[imapservers.xml](e2_imapservers_xml.md) | In this file you can see where I injected the previously defined ImapMailboxSizers into the ImapServers for the preConversionAction to use. |
|[imap2exchange-config.properties](e2_imap2exchange_config_properties.md)| In here you can see that I define the values of the properties for the fore mentioned objects. |