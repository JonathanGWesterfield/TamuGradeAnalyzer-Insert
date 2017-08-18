# TamuGradeAnalyzer-Insert
This is the backend to the TamuGradeAnalyzer. I'm making it a standalone project since the other part is all for the user. I don't want the user to have access to it, but data still needs to be inserted into the database.

# What It Does
It takes in the Grade PDF's from the TAMU registrar site, strips the text off of the pdf's and upload them to a database. 
It uploads to the database by having the user input a password (that is not hardcoded into the application) to create the 
database connection. From there, the gui will give the user the option to upload to the database or not. After that, the
program will strip the text off of the pdf's, record which files were read and then upload the data to the database.
