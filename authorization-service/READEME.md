Project : Authorization Service
Descrption : Authorised User can have access to do any CRUD operation only after login.

Rest API's with example
Get Profile details for seleted user

login api

POST: http://localhost:9090/assignement/login
{
	"username": "admin",
	"password":"password"
}

Register
POST: http://localhost:9090/assignement/register
{
	"username": "admin",
	"password":"password"
}

GET: http://localhost:9090/assignement/profile

Create Profile for seleted user
POST: http://localhost:9090/assignement/profile
Sample Json Input
{
	"address": "bbsr",
	"mobile":"7223776227"
}

Update Profile for seleted user
PUT: http://localhost:9090/assignement/profile
Sample Json Input
{
	"address": "kolkata",
	"mobile":"7222776227"
}

DELETE Profile for seleted user
DELETE: http://localhost:9090/assignement/profile

