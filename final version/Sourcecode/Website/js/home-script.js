/**********************************************************************
**  SOURCE FILE:   home-script.js -  Javascript for the home page
**      
**  PROGRAM:    Android GPS // Big Brother
**
**  FUNCTIONS:
**
**
**  DATE:       February  27th, 2015
**              
**
**  DESIGNER:   Rhea Lauzon A00881688 
**          
**
**  PROGRAMMER: Rhea Lauzon A00881688
**
**  NOTES:
**  Displays the information from data in a table
*************************************************************************/

/*******************************************************************
** Function: $(document).ready()
**
** Date: February 27th, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			$(document).ready(function()
**
** Returns:
**			void
**
** Notes:
**	When the document is ready it creates the google map and loading
**  the initial markers.
**
**********************************************************************/
$(document).ready(function()
{
	//load the data from the file
	loadData();

});


/*******************************************************************
** Function: loadData
**
** Date: March 1st, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			loadData()
**
** Returns:
**			void
**
** Notes:
**	Parses the XML file for users and add them to the table.
**  Called repeatedly by a timer.
**
**********************************************************************/
function loadData()
{
	//clear out the data for the refresh
	$('#users > tbody').empty();


	if (window.XMLHttpRequest)
	{
		xhttp = new XMLHttpRequest();
	}

	//open the file
	xhttp.open("GET", "data.xml", false);
	xhttp.send();
	xmlDoc = xhttp.responseXML;

	//fetch all the users
	var users = xmlDoc.getElementsByTagName("user");

	//loop through all the users
	for (var i = 0; i < users.length; i++)
	{
		//fetch the latitude and longitude
		var latitude = users[i].getElementsByTagName("latitude")[0].childNodes[0].nodeValue;
        var longitude = users[i].getElementsByTagName("longitude")[0].childNodes[0].nodeValue;

        //fetch the ID
        var name = users[i].getElementsByTagName("name")[0].childNodes[0].nodeValue;

        //fetch the time
        var time = users[i].getElementsByTagName("time")[0].childNodes[0].nodeValue;

        //fetch the IP address
        var ip = users[i].getElementsByTagName("ip")[0].childNodes[0].nodeValue;


        //add the information to the table
        addToTable(name, ip, time, latitude, longitude);
	}

	//set the timer for the next update to refresh data
	window.setTimeout(loadData, 5000);

}


/*******************************************************************
** Function: addToTable
**
** Date: March 1st, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			addToTable(name, ip, time, latitude, longitude)
**
** Returns:
**			void
**
** Notes:
**	Adds the data to the page in a table format
**
**********************************************************************/
function addToTable(name, ip, time, latitude, longitude)
{
	//add the user to the table
	$('#users > tbody:last').append('<tr><td>' + name + '</td><td>'+ ip + '</td><td>'+ time + '</td><td>' 
		+ latitude + '</td><td>' + longitude + '</td></tr>');

}