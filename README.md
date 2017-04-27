# Society Application
The following is a submission for the third Object Oriented Programming assignment. This is a group submission created by the following students:

| Name            | Student Number |
| --------------- |----------------|
| Robert Vaughan  | C15341261      |
| Padraig Redmond | C15755659      |
| Eoghan Quirke   | C15507837      |
| Simon Unsworth  | C15500557      |

# You Tube

[![Joining a Society](https://img.youtube.com/vi/mr11fARf72I/0.jpg)](https://www.youtube.com/watch?v=mr11fARf72I)

[![App Functionality](https://img.youtube.com/vi/oROsaa0amNU/0.jpg)](https://www.youtube.com/watch?v=oROsaa0amNU)

# Concept

During the beginning of Dublin Institute of Technology's academic year of 2016/2017, each contributor to this repository recruited members for the Computer Science Society within the college. During this time period, it was then each contributor realised that there is no dedicated society application that is utilised across all institutes of education that students may use to get information about societies available to them. Once a few months past, the aforementioned contributors arranged to create a project together which shall be used as a submission for their Object Oriented Programmingc assignment. Through various meetings on what to design, it was then that student and contributor, Robert Vaughan, realised that the project provided the perfect opportunity to develop an application that can allow users to easily join societies and pull information on them.

The original concept agreed by the group was to create an application where a user could scan a generated QR Code that would instantly add them to a society. This is to reduce the time a person takes filling out forms for every society that takes around two to three minutes into a ten to fifteen second interaction. The application will also allow a user to search all of the available societies within the college and give any committee member user tools to add people to the society by generating QR codes. A Chair user may also have the power to update the committee and society with in-app tools. The group also deemed it fit to create an admin web interface that will allow administrators to view requested data. 


# App

## Features

The front end of the application was developed using Android Studio. The application will launch with a Stater Activity that will check if the user has valid credentials saved within the application's database to log in. If so, the application will direct the user to the homepage. If not, the user will be sent to the to the Login Activity. The Login utilises support text hint layouts to allow the fields to animate movement once clicked on and contains two buttons, 'Login' and 'Register'. If a user attempts a login, error checking will occur and the entered fields to ensure if they are valid. If the fields are not valid, a focus will be set on the field with the error message, if the credentials are valid but the login fails, an error message will return to the user. All of these features may also be found in the Register Activity.

Once the user has entered valid details, they are placed in the Home Activity. The Home Activity contains Fragments which allow for modularity to take place within the application. These fragments are controlled by a navigation drawer which acts as a sub-menu within the application. The following is the list of fragments/sub-headings:

* Society Fragment
* Search Societies
* Scan QR Code
* Generate QR Code
* Update User Details
* Chair Tools

### Society Fragment

The Society Fragment will alter the Home Activity to display a page for the society that a user has clicked on. The page will contain information on the society with buttons displaying on the bottom of the page. A 'Generate QR' Button will display if you are a committee member of that society while a 'Chair Tools' runs a fragment that allows a Chair of a society to edit committee members. 

### Search Societies

This fragment allows a user to search for societies within the app. The search displays a drop-down list of all active societies. A user’s search may also be filtered with the use of radio buttons available at the bottom of the page.

### Scan QR Code

When a user clicks on 'Scan QR' in the navigation drawer, a QR scanner will access their camera and display a code reader. Once a user scans a QR code, a query will be sent to the server to determine if the QR code is valid. If not, the appropriate error handling will take place. If so, the user will be apart of that society.

### Generate QR Code

When a committee member user wishes to generate a QR Code for a member to signup to their society. This fragment will request a QR token from the database, once this token is recieved, a WebView will open the token's URL and display a QR code that is valid for 5 minutes. 

### Update User Details

If a user wishes to edit their details, their current information will be displayed on edit text fields. When a user clicks on the 'Update' button, it will update the database.  

### Chair Tools

The chair tools allows a Chair user of a society to add and remove members from the society with their emails. 

## Implementation

### Login/Register
Queries a database on a webserver once someone logs in or registers. Both operations deal with input errors and failed queries and will display appropriate error messaging.

### Home
Makes use of fragments to display different layouts within the homepage. A navigation drawer will control the movement between different fragments. All user data is pulled from a local database or instance while the app is running. 

### QR Generator
Server is queried to create a token which will be return upon success. This token will be intserted into a URL which will point to the QR code. The app with display the QR code and shall only be readable once.

### User Details
All of the user’s information is stored on a local database once they login. When they wish to edit their data, the database will be queried to return all required information. The information will be displayed in edit text fields, allowing a user to update their data. If an update query is made, the server will update will update the user’s information.

### QR Scanner
With the use of the ZXing library, the application can open the user’s camera to display a code scanner. Once the code is scanned, a thread will send a query that the QR has been scanned by that user to the server. The server will then add them to the society from which the code was generated from. 

### Society Search
Allows the user to search for a society by name. Possible results will display in a drop-down list. Radio buttons under the search bar allow the user to dynamically refine the list of searchable societies based on whether they are a member/committee/chair of a society. These pages will pull information from the local database that will describe the society and depending on the user's credentials, buttons will display allowing them to either generate a QR code or utilise the Chair Tools. 


# API

The API is built in Python, and is connected to a MySQL database. Originally we intended to design a RESTful API, but that proved difficult as we were also using Python for the first time.
Instead we have one script for each piece of functionality that the app requires for interacting with the database.

All callable scripts take a post request, and return a JSON object.
The JSON always contains the fields "return_code" and "return_msg", to inform the app what the result of the request was.
The following return codes are also used in the case of an error:

| Return code | Return message                      |
|-------------|-------------------------------------|
|      0      | Success                             |
|      1      | Invalid session id                  |
|      2      | Invalid username or password        |
|      3      | Email already in use                |
|      4      | Email not verified                  |
|      5      | Incorrectly formatted request       |
|      6      | Database error                      |
|      7      | Invalid permissions for this action |
|      8      | Invalid join token                  |
|      9      | Already a member                    |

The scripts all make use of the api.py script to get the post data, set return codes, set the header, and send the response. Database access is done through the database.py module.

## Security
Two things that were never implemented HTTPS support and defending against SQL injections. Both of these would be quite simple to fit into the system, and other than that, the server is very secure.

When logging in, the user's password is paired with a randomly generated 128 bit salt, and passed to a cryptographic hash function. Both the resulting 256 bit hash and the salt are the only things stored.
This method protects against both rainbow tables and brute force attacks, even if the database is compromised.

After the server confirms the user's login, it returns a randomly generated 128 bit session ID, which paired with the user's member ID, is the only way to validate themselves with the server for future requests. With this setup, the user's password is only ever sent to the server once, and is never stored on either the server or the app.

## QR Code Generation
To join a society, the user must scan a generated QR code from a committee member's app. The QR code consists of two JSON fields, the society ID and a token. When the QR code is generated, these two values are stored in the database, along with the timestamp of the creation time. When a user sends a request to join a society, the token and society ID are checked against that database to ensure they are valid, along with checking the timestamp. QR codes are setup to expire after 5 minutes, and in the case where they do, the user will not be permitted to join the society with that token.

On a request being received, the server will check the database for expired QR codes, and will delete the locally stored images along with the database entries.

# Admin Tools

## Features/Implementation

### Login
This is where the admin logs in to the tools. If the admin enters the details correctly they are issue in session_id
in the form of a cookie. This cookie must be recieved in every other page otherwise the admin will be redirected back to
the login page.

### home
This is where the admin can view the contents of all tables. They can view certain tables 
in more detail.

### edit
Allows the user to edit fields in a database entry.
The user is not allowed edit primary keys or timestamps to prevent them breaking the db

### view
Views a memeber in more detail including societies they are chair/committee/member of.

### societiesView
Views societies in more detail including their members/committee members/chair
 
# Key Notes

* Secure password salting and hashing
* Dynamically updated search view using radio buttons
* Progress bars during app operations
* Pull down screen to refresh local data from server
* QR codes generated and stored on server
* Fully operational login/register features that are handled on Raspberry Pi
* All server capabilities are hosted on a Raspberry Pi
* QR scanner within the application
* Application utilises session_id's to keep the user logged in
* Back end is written entirely in python, which we learned ourselves
* Admin tools generates html tables dynamically based on tables in the database.

# Download

If you wish to download this application, clone this repository and open the 'app' directory in your desired Android IDE. Then follow these instructions:

* Check if the following directory exists within the build folder -> SocietiesApp\app\SocietiesApp\app\build\libs
* If the libs directory does not exist, create one. Note that the directory must be called 'libs'
* If a libs folder is present, but the [okio](https://github.com/square/okio) and [okhttp](https://github.com/square/okiohttp://square.github.io/okhttp/) libraries are not present, please download them and place them in your libs directory. Android Studio should recognise them as libraries, however if it does not, open the directory in Android Studio and right click on both libraries and click 'Add as Library'
