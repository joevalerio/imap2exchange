[Home](http://code.google.com/p/imap2exchange/) | [examples](examples.md)

# imap2exchange Examples #

Each of the examples below builds on the one before it.  This will give you a start point to configure the application for your use.

| **Example** | **Description** |
|:------------|:----------------|
|[vanilla batch conversion example](Example1.md)|This example is a simple example that shows the key players for doing a conversion.  This is bare bones. |
|[batch conversion with mailbox size checker](Example2.md)| This example show how to use a mailbox checker preConversionAction to check the size of mailboxes prior to conversions.  This relies on the fact that the default implementation of ImapServer has a ImapMailboxSizer property.  If it did not have the property you would just subclass the ImapServer, add a property and getter and setters, and use that specific implementation in the imapservers.xml file.  Since I think every will have this need, I included it in the default implementation.|
|[batch conversion with altnames](Example3.md)| This example shows how to use the Alternate System Folder names tool to merge source IMAP folders, such as "trash" and "Trash" into system folders in Exchange like "Deleted Items"|