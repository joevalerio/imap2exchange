[Home](http://code.google.com/p/imap2exchange/) | [Logging and Auditability](Logging.md)

# Logging and Auditability #

Any time you perform a conversion of something as important as email you must have an auditable system.  I have spent some time trying to manage the vast output from this system and present it so that it is easy to determine what happened at a granular level, but also to stand at 10,000 ft and find the 2 people in 300 that failed.  Because of the complexity of the log4j setting in this application, I felt is was at least responsible for me to explain what I did, and present you with how I will use this application.

First I will start with how I see these tools working in production.  Here at Yale we have approximately 5200 users that are going to be converted from IMAP to Exchange.  This is due to the decommissioning of our MeetingMaker Server, and Exchange becoming our Calendering Server.   This is a large number of users to convert if we can only do five at a time during normal business hours.  Until I can comfortably say that a 2G jvm can handle more that 5 concurrent conversions, that is the number I am sticking with.  Anyhow, I see our support organization putting together a schedule of users to convert on any given day.  This schedule would be submitted to Systems, who would run the batch imap2exchange conversion utility in N-number of jvms to accomplish the task over the course of the evening.  In the morning, the support personnel would check the website for conversion statuses.  Since I will have the log directories for both co-located,  the web app will be able to see the results and even see what the errors where, i.e. Source Mailbox was too big, the Exchange Account was never provisioned... whatever.  Now that you can see that there is a need for both types of applications,  here is how I have the logging configured and I will also include a sample audit log, of my own personal mailbox.

### log4j.properties ###
```
# Set root logger level to DEBUG and its only appender to A1.
log4j.rootLogger=WARN, C

# C is set to be a ConsoleAppender.
log4j.appender.C=org.apache.log4j.ConsoleAppender
log4j.appender.C.layout=org.apache.log4j.PatternLayout
log4j.appender.C.layout.ConversionPattern=%-4r [%t] %-5p %x - %m%n

# F is set to be a FileAppender.
log4j.appender.F=org.apache.log4j.FileAppender
log4j.appender.F.File=${EXCHANGE_CONVERSION_HOME}/logs/conversion.manager.log
log4j.appender.F.layout=org.apache.log4j.PatternLayout
log4j.appender.F.layout.ConversionPattern=%-4r [%t] %-5p %x - %m%n

# P is set to be a FileAppender.
log4j.appender.P=org.apache.log4j.FileAppender
log4j.appender.P.File=${EXCHANGE_CONVERSION_HOME}/logs/completed.log 
log4j.appender.P.layout=org.apache.log4j.PatternLayout
log4j.appender.P.layout.ConversionPattern=%m%n

log4j.appender.T=edu.yale.its.tp.email.conversion.log.ThreadAppender
log4j.appender.T.outputFolder=${EXCHANGE_CONVERSION_HOME}/logs
log4j.appender.T.managerLogFilename=conversion.manager.log
log4j.appender.T.layout=org.apache.log4j.PatternLayout
log4j.appender.T.layout.ConversionPattern=%-4r [%t] %-5p %x - %m%n

# assign edu.yale code only to the thread appender
#*******************************************************
log4j.additivity.edu.yale=false
log4j.logger.edu.yale=INFO,T
log4j.logger.edu.yale.its.tp.email.conversion.ExchangeConversionManager=INFO, F
log4j.logger.edu.yale.its.tp.email.conversion.ExchangeConversionManager.completed=INFO, P
```

First, log4j usually separates log by logger name, usually by convention the package names of the class files.  For imap2exchange I wanted to separate a users entire conversion history into a single file.  Since Each conversion is run in its own thread, aptly names "i2e: ${uid}@${sourcePo}".  I took advantage of this by writing a ThreadAppender.  This appender will create a FileAppender for each thread.  The name of the file will be "${uid}@${sourcePo}" and it will be located in the outputFolder.  Any output that is logged to the ThreadAppender from a thread other than a conversion thread, it will be logged to the managerLogFilename in the same directory.

I also created a FileAppender for a very specific logger defined in the ExchangeConversionManager simply for the purpose of logging the conversion results after the conversion has completed.  The conversion will have a status of success or failed.

The Console appender is used to pipe any output from non-edu sources to the console.  In tomcat this will result in messages being logged to catalina.out.

The additivity property stops all edu.yale classes from defaulting into the rootLogger.

Here is my conversion log:
```
629  [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Conversion Starting: jjv6@pantheon-po09.its.yale.edu - Feb 15, 2008 14:58:09
1135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - UPN for jjv6: joseph.valerioo@yale.local
1136 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - SMTP for jjv6: joseph.valerio@labxhub1.its.yale.edu
1191 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Requesting Size from: https://config.mail.yale.edu/account-tool-dev/usage?uid=jjv6&po=pantheon-po09.its.yale.edu
3421 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Mailbox size OK: actual[232902656] < max[786432000]
3729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in  for Merged Folder: null
18456 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Tagged-Spam for Merged Folder: Tagged-Spam
18503 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Potentially-Infected for Merged Folder: Potentially-Infected
18505 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Junk E-mail for Merged Folder: Junk E-mail
18507 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Trash for Merged Folder: Trash
18569 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Drafts for Merged Folder: Drafts
18573 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Sent for Merged Folder: Sent
20525 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Software for Merged Folder: Software
20579 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in test for Merged Folder: test
20582 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Misc Reserved Emails for Merged Folder: Misc Reserved Emails
20599 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Templates for Merged Folder: Templates
20602 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in trash for Merged Folder: Trash
20603 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in .ssh for Merged Folder: .ssh
20715 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in tester for Merged Folder: tester
20716 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in OperMail for Merged Folder: OperMail
20761 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Sakai-Dev List for Merged Folder: Sakai-Dev List
20898 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Slide-User List for Merged Folder: Slide-User List
20919 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in asdfasdf for Merged Folder: asdfasdf
20923 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in sakai-nightly for Merged Folder: sakai-nightly
20932 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Tomcat-list for Merged Folder: Tomcat-list
20934 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in CAS List for Merged Folder: CAS List
21130 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in tsm for Merged Folder: tsm
21137 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Sakai-Dev List-Copy for Merged Folder: Sakai-Dev List-Copy
21344 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in ExchangeTest for Merged Folder: ExchangeTest
21380 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Cyrus Upgrade for Merged Folder: Cyrus Upgrade
21469 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in personal for Merged Folder: personal
21472 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Tasks for Merged Folder: Tasks
21477 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Calendar for Merged Folder: Calendar
21480 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Notes for Merged Folder: Notes
21483 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Journal for Merged Folder: Journal
21487 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in Contacts for Merged Folder: Contacts
21492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in INBOX for Merged Folder: INBOX
33563 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Cyrus Upgrade
33563 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Cyrus Upgrade] does not exist, creating it
33663 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 5 messages to create in Cyrus Upgrade
33671 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 5 messages (3265625 byes) from folder Cyrus Upgrade.
36356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
36356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Cyrus Upgrade
36356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
36356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Cyrus Upgrade
36356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
36405 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Tagged-Spam
36405 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Tagged-Spam] does not exist, creating it
36508 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 40 messages to create in Tagged-Spam
36549 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 40 messages (897212 byes) from folder Tagged-Spam.
37957 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
37958 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Tagged-Spam
37958 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
37958 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Tagged-Spam
37958 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
38005 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Sent
38006 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Sent] does not exist, creating it
38108 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 657 messages to create in Sent
38122 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder Sent, subject:see this(9548234)
46488 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 32 messages (495895 bytes) from folder Sent.
47482 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 10 messages (4782050 bytes) from folder Sent.
51854 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 131 messages (4840037 bytes) from folder Sent.
58802 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 15 messages (5112340 bytes) from folder Sent.
63707 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 147 messages (4600845 bytes) from folder Sent.
69289 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 70 messages (3391804 bytes) from folder Sent.
73299 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 6 messages (3081753 bytes) from folder Sent.
76015 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 25 messages (3149252 bytes) from folder Sent.
78873 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 14 messages (4628917 bytes) from folder Sent.
83248 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder Sent, subject:trace(10636763)
92196 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 35 messages (4945295 bytes) from folder Sent.
98640 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 42 messages (2040404 bytes) from folder Sent.
101605 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 15 messages (3891276 bytes) from folder Sent.
105643 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 36 messages (3324102 bytes) from folder Sent.
109225 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 10 messages (4713853 bytes) from folder Sent.
113903 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 4 messages (3739361 bytes) from folder Sent.
116892 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 63 messages (4196219 byes) from folder Sent.
121491 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
121492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Sent
121492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
121492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Sent
121492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
121576 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Drafts
121614 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1 messages to create in Drafts
121617 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 1 messages (2120 byes) from folder Drafts.
121678 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1 messages to create in Deleted Items
121679 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 1 messages(deleted) (4247 byes) from folder Drafts.
121746 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Drafts
121746 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
121746 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Drafts
121746 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
121784 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Software
121784 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Software] does not exist, creating it
121890 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 11 messages to create in Software
121907 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 11 messages (1560042 byes) from folder Software.
123356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
123356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Software
123356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
123356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Software
123356 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
123408 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Junk E-mail
123446 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Junk E-mail
123446 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
123447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Junk E-mail
123447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
123447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Junk E-mail
123447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
123495 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Tasks
123496 [i2e: jjv6@pantheon-po09.its.yale.edu] WARN   - Not Syncronizing Messages in a Non-Mail Exchange System folder: Tasks
123496 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of personal
123496 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [personal] does not exist, creating it
123590 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1 messages to create in personal
123592 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 1 messages (4116 byes) from folder personal.
123649 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
123649 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in personal
123650 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
123650 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in personal
123650 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
123724 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Calendar
123724 [i2e: jjv6@pantheon-po09.its.yale.edu] WARN   - Not Syncronizing Messages in a Non-Mail Exchange System folder: Calendar
123724 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of tester
123725 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [tester] does not exist, creating it
123827 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in tester
123827 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
123827 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in tester
123827 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
123828 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in tester
123828 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
124051 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in tester/uwash-libs for Merged Folder: uwash-libs
124052 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Indexing Messages in tester/.settings for Merged Folder: .settings
124091 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of .settings
124091 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [.settings] does not exist, creating it
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in .settings
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in .settings
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in .settings
124189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
124238 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of uwash-libs
124239 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [uwash-libs] does not exist, creating it
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in uwash-libs
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in uwash-libs
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in uwash-libs
124320 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
124779 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Contacts
124779 [i2e: jjv6@pantheon-po09.its.yale.edu] WARN   - Not Syncronizing Messages in a Non-Mail Exchange System folder: Contacts
124779 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of sakai-nightly
124780 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [sakai-nightly] does not exist, creating it
124873 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 43 messages to create in sakai-nightly
124930 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 43 messages (74875 byes) from folder sakai-nightly.
125567 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
125568 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in sakai-nightly
125568 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
125568 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in sakai-nightly
125568 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
125608 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Misc Reserved Emails
125608 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Misc Reserved Emails] does not exist, creating it
125716 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 2 messages to create in Misc Reserved Emails
125719 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 2 messages (415749 byes) from folder Misc Reserved Emails.
126153 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
126153 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Misc Reserved Emails
126153 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
126153 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Misc Reserved Emails
126153 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
126190 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of test
126191 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [test] does not exist, creating it
126276 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in test
126277 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
126277 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in test
126277 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
126277 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in test
126277 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
126329 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Trash[Trash, trash]
126329 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Trash] does not exist, creating it
126420 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 78 messages to create in Trash[Trash, trash]
126506 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 78 messages (1554542 byes) from folder Trash[Trash, trash].
129064 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
129064 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Trash[Trash, trash]
129064 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
129065 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Trash[Trash, trash]
129065 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
129104 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of CAS List
129104 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [CAS List] does not exist, creating it
129189 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 313 messages to create in CAS List
129542 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 313 messages (5209102 byes) from folder CAS List.
141981 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
141982 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in CAS List
141982 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
141982 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in CAS List
141982 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
142030 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Notes
142030 [i2e: jjv6@pantheon-po09.its.yale.edu] WARN   - Not Syncronizing Messages in a Non-Mail Exchange System folder: Notes
142030 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of .ssh
142030 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [.ssh] does not exist, creating it
142134 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in .ssh
142135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
142135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in .ssh
142135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
142135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in .ssh
142135 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
142235 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Templates
142235 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Templates] does not exist, creating it
142327 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1 messages to create in Templates
142329 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 1 messages (9832 byes) from folder Templates.
142445 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
142445 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Templates
142445 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
142445 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Templates
142445 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
142485 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of asdfasdf
142485 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [asdfasdf] does not exist, creating it
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in asdfasdf
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in asdfasdf
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in asdfasdf
142589 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
142633 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Sakai-Dev List
142633 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Sakai-Dev List] does not exist, creating it
142741 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 807 messages to create in Sakai-Dev List
143937 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 807 messages (4594969 byes) from folder Sakai-Dev List.
164253 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
164253 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Sakai-Dev List
164253 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
164253 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Sakai-Dev List
164253 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
164309 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Potentially-Infected
164310 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Potentially-Infected] does not exist, creating it
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Potentially-Infected
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Potentially-Infected
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Potentially-Infected
164415 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
164458 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Journal
164458 [i2e: jjv6@pantheon-po09.its.yale.edu] WARN   - Not Syncronizing Messages in a Non-Mail Exchange System folder: Journal
164458 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of INBOX
164501 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 2616 messages to create in INBOX
164813 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 242 messages (4035290 bytes) from folder INBOX.
173721 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 107 messages (3160423 bytes) from folder INBOX.
178830 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 4 messages (4953083 bytes) from folder INBOX.
183699 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 83 messages (4728874 bytes) from folder INBOX.
189338 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 289 messages (5232347 bytes) from folder INBOX.
199273 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder INBOX, subject:Wanna see something really cute?(15872271)
219492 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 310 messages (4869966 bytes) from folder INBOX.
229705 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 387 messages (4223232 bytes) from folder INBOX.
240362 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 9 messages (1699876 bytes) from folder INBOX.
241946 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 21 messages (5234233 bytes) from folder INBOX.
247542 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 6 messages (5209876 bytes) from folder INBOX.
254706 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder INBOX, subject:trace(10638005)
269203 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder INBOX, subject:sakai-dev(5427304)
275229 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 236 messages (4358635 bytes) from folder INBOX.
286736 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 47 messages (4132978 bytes) from folder INBOX.
292969 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 135 messages (4934451 bytes) from folder INBOX.
299982 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 268 messages (4183721 bytes) from folder INBOX.
308642 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 74 messages (4492433 bytes) from folder INBOX.
314174 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 30 messages (5242283 bytes) from folder INBOX.
319305 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving single messages bigger then max message group size (5242880 bytes)from folder INBOX, subject:analyzerclient.jar file(10927843)
329665 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 299 messages (5127411 bytes) from folder INBOX.
339951 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 65 messages (819995 byes) from folder INBOX.
341689 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 4 messages to create in Deleted Items
341696 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 4 messages(deleted) (20252 byes) from folder INBOX.
341847 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in INBOX
341847 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
341847 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in INBOX
341847 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
341990 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Slide-User List
341990 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Slide-User List] does not exist, creating it
342079 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 92 messages to create in Slide-User List
342184 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 92 messages (490914 byes) from folder Slide-User List.
344348 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
344349 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Slide-User List
344349 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
344349 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Slide-User List
344349 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
344392 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of OperMail
344392 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [OperMail] does not exist, creating it
344482 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 208 messages to create in OperMail
344707 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 208 messages (1108224 byes) from folder OperMail.
348729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
348729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in OperMail
348729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
348729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in OperMail
348729 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
348776 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of ExchangeTest
348777 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [ExchangeTest] does not exist, creating it
348881 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1 messages to create in ExchangeTest
348884 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 1 messages (1520115 byes) from folder ExchangeTest.
350200 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
350200 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in ExchangeTest
350200 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
350201 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in ExchangeTest
350201 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
350240 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Sakai-Dev List-Copy
350240 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Sakai-Dev List-Copy] does not exist, creating it
350327 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 1216 messages to create in Sakai-Dev List-Copy
351231 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving group of 845 messages (5242560 bytes) from folder Sakai-Dev List-Copy.
372582 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 371 messages (2138329 byes) from folder Sakai-Dev List-Copy.
380321 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
380321 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Sakai-Dev List-Copy
380321 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
380321 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Sakai-Dev List-Copy
380321 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
380437 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of Tomcat-list
380437 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [Tomcat-list] does not exist, creating it
380520 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Tomcat-list
380521 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
380521 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Tomcat-list
380521 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
380521 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Tomcat-list
380521 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
380569 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Starting Conversion of tsm
380569 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Exchange Folder [tsm] does not exist, creating it
380689 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 29 messages to create in tsm
380719 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Moving last group of 29 messages (149246 byes) from folder tsm.
381383 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to create in Deleted Items
381384 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in tsm
381384 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to check for updates in Deleted Items
381384 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in tsm
381384 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - There are 0 messages to delete in Deleted Items
381446 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Conversion for jjv6 completed successfully.
381447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - 6126 messages [218.3 MB] were moved.
381447 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Conversion Completed: jjv6@pantheon-po09.its.yale.edu - Feb 15, 2008 15:04:30
381448 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Feb 15, 2008 14:58:09    jjv6@pantheon-po09.its.yale.edu warning 6 Mins 20 Secs  6126    218.3 MB
381448 [i2e: jjv6@pantheon-po09.its.yale.edu] INFO   - Caught "NoMoreConversions" event, exiting YaleExchangeConversion.
```