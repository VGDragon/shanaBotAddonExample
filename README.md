# Shana Bot Addon
This is the template for making an Addon for the Discord Bot ShanaBot. You can clone this Project and you have a working Addon for the Bot. 
The Documetation and important informations are in the Addon Class.

## Discord Bot ShanaBot
ShanaBot is an Addon based Discord Bot. With an Account for the ShanaBot Website, you can add/remove Addons(commands) to your Server. 
To make an Account, you go to a Server with ShanaBot and write
```
shana login
```

## Functions
Your Addon have some functions to work with
```
onMessageCreate(discordAPI: DiscordAPI, messageReceive: MessageReceive, messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String)
onMessageReactionAdd(discordAPI: DiscordAPI, messageReactionObject: MessageReactionObject)
help(messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String): Embed
description(): String
save(): String
load(string: String)
onExit()
```
The function have to be the same name and variable as listed. You can find a detailed infomations in the Example File <a href="https://github.com/VGDragon/shanaBotAddonExample/blob/master/src/main/kotlin/com/shanabot/addon/Addon.kt">Addon.kt</a>.

## Writing a message
For sending a message you need to use the DiscordAPI given from ShanaBot. 
In the DiscordAPI class you have different types of actions your Addon can use. 
For writing a message, you can use 6 function and the simble one is sendMessage("").
```
discordAPI.sendMessage("Hello World.")
```
With that function, ShanaBot answer the text "Hello World." on the same Channel as someone used your command. 
If you want to write the user in a DM (Direct Message) you can use this
```
discordAPI.sendDMMessage("Hello World.")
```
If you want more style to your message, you can use the MessageSend class. You have a lot more setting with that
```
val botCommandStartString = "Shana "

val messageSend = MessageSend()
val embed = Embed()
embed.title = "Command: say"
val fieldList: MutableList<Field> = mutableListOf()
fieldList.add(Field("say", "Shana gives the given text back.\n\n" +
                    "User: ${botCommandStartString}say Hi, I am Shana.\n" +
                    "Shana: Hi, I am Shana.\n"))
embed.fields = fieldList
messageSend.embed = embed
api.sendMessage(messageSend)
```

## Why ShanaBot?
The problem with Discord Bots is that you need 2 or 3 Bots for everything you need or you have a Bot with a lot of functions you don't need. 
The problem is, if you have 30 commands, the help message is a big block of Text and it is hard to find what you search for. 
Sometimes you need something and found out, you can't find a Bot that support what you need. I want to make ShanaBot in a way, 
where everyone can add an Addon as long as they don't want to harm someone (or the Bot). With that in mind, you don't need to make your own 
Bot. If you want a function, you only need to write the Admin of ShanaBot and make an Addon. If the function is not easy to make, the Admin 
will help you.

## Why did you make ShanaBot?

At first I wanted to learn Kotlin and make my own Discord library. With that library, I wanted to make a small Bot for a task on my Discord Server. 
I found a Discord Server with some library to make Bots with, but to add my library it have to support every function of the Discord API, 
and if a library for that coding laguage exist, it have to be better as that one. I don't think it make much sense to add my project to it, 
so I had a different Idea. To add functions to a Bot, you have to add the functions to your code, upload it to your server (if you don't use your PC) 
and restart the Bot (with the new File). I didn't want to restart my Bot every time I added a function. 
Every update I have some downtime and the File to upload will go bigger and bigger. At that point I had the Addon function in mind...

