[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md) | [Example3](Example3.md)

# batch conversion with altnames #

In this example builds on the mailbox size checker example.  Here we add the functionality to merge Imap folders into Exchange System Folders.

| **File** | **Change** |
|:---------|:-----------|
|[imap2exchange-config.xml](e3_imap2exchange_config_xml.md) | Here you can see that I define two altNames and group them in an altNames object which is injected into the ExchangeConversionFactory. Please note that there is no need to enter "trash" and "Trash" as the folder names are always compared case insensitive.|