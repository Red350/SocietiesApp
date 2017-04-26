# Society Application
The following is a submission for the third Object Oriented Programming assignment. This is a group submission created by the following students:

| Name            | Student Number |
| --------------- |----------------|
| Robert Vaughan  | C15341261      |
| Padraig Redmond | C15755659      |
| Eoghan Quirke   | C15507837      |
| Simon Unsworth  | C15500557      |

# You Tube

# Concept

During the beginning of Dublin Institute of Technology's academic year of 2016/2017, each contributor to this repository recruited members for the Computer Science Scoitey within the college. During this time period, it was then each contributor realised that there is no deadicated socitey app that is utilised across all institutes of education that students may use to get information about societies available to them. Once a few months past, the aformentioned contributor's arranged to create a project together which shall be used as a submission for their Object Oriented Programming. Through various meetings on what to design, it was then that student and contributor, Robert Vaughan, realised that the project provided the perfect oppertunity to develop an app that can allow users to easily join societies and pull information on them.

# Features

## Front-End

The front end of the application was developed using Android Studio. The app will launch with a Stater Activity that will check if the user has valid credentials saved within the app's database to log in. If so, the application will direct the user to the homepage. If not, the user will be sent to the to the Login Activity. The Login utilises support text hint layouts to allow the fields to animate movement once clicked on and contains two buttons, 'Login' and 'Register'. If a user attempts a login, error checking will occur and the entered fields to ensure if they are vaid. If the fields are not valid, a focus will be set on the field with the error message, if the the credentials are valid but the login fails, an error message will return to the user. All of these features may also be found in the Register Activity.

Once the user has entered valid details, they are placed in the Home Activity. The Home Activity contains Fragments which allow for modularity to take place within the application. These fragments are controlled by a navigation drawer which acts as a sub-menu within the application. The following is the list of fragments/sub-headings:

* Socitey Fragment
* Search Sociteies
* Scan QR Code
* Generate QR Code
* Update User Details
* Chair Tools

### Socitey Fragment

The Socitey Fragment will alter the Home Activity to display a page for the society that a user has clicked on. The page will contain information on the society with buttons displaying on the bottom of the page. A 'Generate QR' Button will display if you are a committee member of that society while a 'Chair Tools' runs a fragment that allows a Chair of a society to edit committee members. 

### Search Sociteies

This fragment allows a user to search for socities within the app. The search displays a drop down list of all active socities. A users search may also be filtered with the use of radio buttons available at the bottom of the page.

### Scan QR Code

When a user clicks on 'Scan QR' in the navigation drawer, a QR scanner will access their camera and display a code reader. Once a user scans a QR code, a query will be sent to the server to detmine if the QR code is valid. If not, the appropriate error handling will take place. If so, the user will be apart of that socitey.

### Generate QR Code

When a committee member user wishes to generate a QR Code for a member to signup to their society. This fragment will request a QR token from the database, once this token is recieved, a webview will open the token's URL and display a QR code that is valid for 5 minutes. 

### Update User Details

If a user wishes to edit their details, their current information will be displayed on edit text fields. When a user clicks on the the 'Update' button, it will update the database.  

### Chair Tools

The chair tools allows a Chair user of a society to add and remove members from the society with their emails. 

# Implementation

## Front-End

## Back-End

## Admin Tools
 
# Key Notes

* QR generator within the application
* Fully operation login/register features that are handled on Rasberry Pi
* All server capabilities are hosted on a Rasberry Pi
* QR scanner within the application
* Fragmentation within that app utilised
* Application utilises session_id's to keep the user logged in
* Error handling on login and register

# Code

# Download

If you wish to download this application, clone this repository and open the 'app' directory in your desired Andriod IDE. Then follow these instructions:

* Check if the following dirctory exists within the build folder -> SocietiesApp\app\SocietiesApp\app\build\libs
* If the libs directory does not exist, create one. Note that the directory must be called 'libs'
* If a libs folder is present, but the [okio](https://github.com/square/okio) and [okhttp](https://github.com/square/okiohttp://square.github.io/okhttp/) libraries are not present, please download them and place them in your libs directory. Android Studio should recognise them as libraries, however if it does not, open the directory in Android Studio and right click on both libraries and click 'Add as Library'