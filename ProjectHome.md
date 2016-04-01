# Overview #

The imap2exchange Conversion Utility merely moves mail. This utility depends on the Exchange account provisioned and IMAP access to the legacy mail store. This tool is a simple queue manager that converts X number of mailbox concurrently, while queuing N number of conversions in a FIFO manner. I have created a command line utility that executes conversions based on a user file, and I have created a Web App to run conversion add via a form. If you happen to have a need for an other form of conversionManager Application, say swing, then please feel free to write it.

Many thanks to [JProfiler](http://www.ej-technologies.com/products/jprofiler/overview.html)

# Download #
## imap2exchange ##
<table>
<blockquote><tr>
<blockquote><td>Current Version</td>
<td><a href='http://imap2exchange.googlecode.com/files/imap2exchange-1.3.1-bin.tar.gz'>imap2exchange-1.3.1-bin.tar.gz</a></td>
</blockquote></tr>
<tr>
<blockquote><td>API (JavaDocs)</td>
<td><a href='http://tp.its.yale.edu/javadocs/imap2exchange-api/'>http://tp.its.yale.edu/javadocs/imap2exchange-api/</a></td>
</blockquote></tr>
<tr>
<blockquote><td>Documentation</td>
<td><a href='Documentation.md'>imap2exchange documentation</a></td>
</blockquote></tr>
<tr>
<blockquote><td>Examples</td>
<td><a href='examples.md'>imap2exchange examples</a></td>
</blockquote></tr>
<tr>
<blockquote><td>Subversion (read-only)</td>
<td><a href='http://imap2exchange.googlecode.com/svn/'>http://imap2exchange.googlecode.com/svn/</a></td>
</blockquote></tr>
<tr>
<blockquote><td>Logging and Auditability</td>
<td><a href='Logging.md'>Logging and Auditability</a></td>
</blockquote></tr>
<tr>
<blockquote><td>misc help</td>
<td><a href='Background.md'>imap2exchange background information</a> <br /><a href='ExchangeWebServices.md'>Introduction to MS Exchange Web Services via Java(JAXWS)</a></td>
</blockquote></tr>
</table></blockquote>

## 1.3.1 Changes ##

  * Revamped how the folder synchronization happens to allow for merging of folders independent of their respective depth.

## 1.2.7 Changes ##

  * Fixed the Sent Items Date Issue. All messages that were saved to the IMAP store from IMAP Clients never receive a Received header. Exchange would mark these messages as received at the time of the conversion. As I send the MIME content to the EWS I add a Received header from imap2exchange and set the date to the original create date of the message, resulting in the correct date displaying in outlook.
  * I added timers to record the time spent doing specific tasks the aid in system configuration modifications increasing performance. All times are in milliseconds.

```
    Example:

          Exchange Connect
              cnt:   6
              max:   1774
              min:   2
              total: 1790
          Exchange Message Delivery
              cnt:   1
              max:   583
              min:   583
              total: 583
          Exchange MetaData Calls
              cnt:   5
              max:   412
              min:   87
              total: 1003
          IMAP Full Mime Message Calls
              cnt:   11
              max:   5
              min:   1
              total: 29
          IMAP MetaData Calls
              cnt:   3
              max:   89
              min:   0
              total: 120
```
## 1.2.6 Changes ##

  * Fixed logging to work with web-tool again... opps sorry

## 1.2.5 Changes ##

  * Use AllTrustingSLLSocketFactory for jaxws to get around java/MS PKI keystore incompatibility issues.

## 1.2.4 Changes ##

  * added Imap Folder exclusion list
  * Minor Logging/Error handling bug fixes

## 1.2.3 Changes ##

  * Does not convert messages from root folders named below and I log a warning messages alerting the fact:
    * Journal
    * Notes
    * Calendar
    * Contacts
    * Tasks
  * Increased logging on single message failures, i.e. invalid MIME Content
  * Removed code trying to get messages in parallel from a folder in IMAP - Doesn't work.

## 1.2.2 Changes ##

  * Minor Bug Fixes
  * Better Logging and Error Control

## 1.2.1 Changes ##

  * Minor Modifications for the web container
  * Statistics - # of Msgs moved and Size.
  * Logging to a file per User per PO
  * removed dependency of my jvm monitor tool (included in the web server)

## 1.2.0 Changes ##

  * Modified code to use Spring for initialization for easier use in web container
  * minor bug fixes

Requirements Java 1.6 As much RAM as possible, 2G preferred.

## imap2exchange-server ##

Current Version - N/A Please get it from subversion, modify the build.properties and the config files in WEB-INF and build the war. Subversion (read-only) -  	http://imap2exchange.googlecode.com/svn/imap2exchange-server/trunk
Notes on imap2exchange-server

There are 2 jsps that are Yale specific. They are the poFinder.jsp, and info.jsp. info.jsp should be easy to update for your implementation, but you can just remove poFinder.jsp from the menu.jsp to avoid it.