**GoogleSheeets2SlackNotification Application**

GoogleSheeets2SlackNotification application is an automation tool that checks cell updates on Google Sheets and sends notification 
messages to users by slack channels.

**Summary**

This application fetchs sheets cell content from any Google Sheet and creates a notification messages
to the related user/candidate by slack. 

Application provides a faster notification system, faster feedbacks to/from clients, increases  awareness on processes 
which are followed by Google sheets.

**Installation**

GoogleSheeets2SlackNotification is a spring boot application, which can be executed by command line by  simple **java -jar** command. 

Application reads data from Google Sheets, so yo need to configure a google service account and credentials 
for that account, that provides  google sheets api authentication. 

Slack web api is used for sending messages, which requires creating a Slack application and enabling 
slack application bot token. Bot tokens provide authentication and direct message abilities for slack applications. 

In installation folder, place application jar file, in same directory create folders /config and /sheet-templates 

Directory structure:

/app.jar

/config/

/sheet-templates/

application.properties should be placed in config directory.
tool scan interval and slack bot user token can be configured by this file.
Default scan interval is 10 minutes, if not set. 

sample application.properties file looks like this :

`sheet.read.interval=*/10 * * * * *
slackAppBotUserToken=xoxb-505329468914-507165135362`

use **sheet.read.interval** to set scan interval, use cron expression.

use **slackAppBotUserToken** parameter to set your slack bot token. 

Download google service account credentials from Google console, rename file as **serviceaccount.json** and
put that file in config folder. This provides google authentication.

in installation directory run;

java -jar .\docs-slack-integration-0.0.1-SNAPSHOT.jar  

command to start web application. It runs on 8090 port, you can configure this by application.properties file.
 
**Sheets Configuration**

we use **./sheet-templates/** folder to place sheet configuration files.

A Google Sheet is represented by a configuration file in application. If you want to use application for 5 sheets, 
then you need to have 5 seperate independent sheet configuration files.

Create a configuration file for your sheet which provides sheet id, data config values of sheet, so that 
application can access and parse sheet data. 

Every sheet config file should have ***.sheet** extension.

Mandatory parameters: 

**sheetId** is unique id that defines your google sheet, it is located between /d/ and /edit/ in you sheets url.
sample config:
sheetId=1MwRjw8RCrCGaP-ql-OALGuMCfDZvOYkgLR7MILlIcGU

**data.queryRange** is the area in your sheet that you wanted to be scanned by application. Any field that you want 
to use in notification message should exist in that area.

sample config:
data.queryRange=TARGET!A3:T

data.userName.index is username for row, application sends notification about updates on rows to users, 
so row data related user info should exist in row. this is index for this column in given range. 

**Be careful indexes atart at first column of your predefined queryRange and index for first column is Zero.**

data.userName.index=0

Every sheet shold have a cerdentials area that have a mapping for username and useremail infos. Application scans this area,
maps username with user email, and uses this mail address for getting users slack credentials.

**credential.queryRange**=Users!A2:G

**credential.userName.index** is users name in credential area. Above mentioned indexing rule  is used for 
obtaining index value

credential.userName.index=1

**credential.email.index** is users email in credential area. Above mentioned indexing rule  is used for 
obtaining index value

credential.email.index=6





