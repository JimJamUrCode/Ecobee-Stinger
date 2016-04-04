# Ecobee-Stinger
This application will allow you to login to your ecoBee account using pin authentication.

###Long term goals
Ideally this application will:
* Give different detailed usage statictics than the current ecobee web portal (HOME IQ)
* Graph your usage statistics (Very similar to what exists on the ecobee portal, just more information, and less loading)
* Usage Calculator (will allow you to calculate your usage)

#Project Setup
###ecoBee AppKey
This is needed to get the application working. You will need to sign up as a developer with the ecoBee portal. Once you are an ecoBee developer, you should be presented with an appkey. Copy the appkey and put it in "android/assets/ecobeeConfig.cfg". The application should now be able to properly authenticate.

###IDE Choice
Since this porject is using the gradle based build system, pretty much any IDE that is compatible with gradle will work here. I started the project using eclipse, transitioned to Android Studio, and have setup Intellij IDEA to work with this project.
