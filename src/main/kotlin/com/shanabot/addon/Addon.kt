package com.shanabot.addon
import com.vgdragon.discordbot.discord.json.Embed
import com.vgdragon.discordbot.discord.json.Field
import com.vgdragon.discordbot.discord.json.websocket.payloadJson.MessageReceive
import com.vgdragon.discordbot.discord.json.websocket.payloadJson.events.MessageReactionObject
import com.vgdragon.discordbotaddon.DiscordAPI


class Addon {


    // multi Thread save
    val saveClassLock = Object()
    // Class with variables to save with save()
    var saveClass = SaveClass()


    /**
     * Will be executed if someone write a text with the command name of the addon.
     * Example: shana say
     *
     * @param discordAPI the discordAPI to use with the Bot.
     * @param messageReceive the message Object from Discord
     * @param messageSplit the message slitted with split(" ")
     * @param startMessageSplitInt the first position in the messageSplit List you can use for your addon.
     *        Example: Shana say hello
     *                   0    1    2
     *        startMessageSplitInt = 2
     * @param botCommandStartString the text to invoke the bot (currently "shana ")
     *
     */
    public fun onMessageCreate(discordAPI: DiscordAPI, messageReceive: MessageReceive, messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String) {
        synchronized(saveClassLock){
            saveClass.counter++
        }
        var string = ""
        for ((i, s) in messageSplit.withIndex()) {
            if (i >= startMessageSplitInt)
                string = "$string $s"
        }
        discordAPI.sendMessage(string.trim(), messageReceive.channel_id)
    }

    /**
     * The Bot remembers the messageID of the message your Addon was sending, for a short amount of time (currently 5min).
     * If someone add A Reaction of your Addons message, this function will be called.
     *
     * @param discordAPI the discordAPI to use with the Bot.
     * @param messageReactionObject the Reaction Object from Discord.
     */
    public fun onMessageReactionAdd(discordAPI: DiscordAPI, messageReactionObject: MessageReactionObject){
        if(!messageReactionObject.emoji.name.equals("❌"))
            return
        discordAPI.deleteMessage(messageReactionObject.message_id)
    }

    /**
     * If someone need help with your Addon, he/she/apache_helicopter can use the command "help $commandName".
     * This function will be called if someone need help with your Addon.
     *
     * @param messageSplit the message slitted with split(" ")
     * @param startMessageSplitInt the first position in the messageSplit List you can use for your addon.
     *             Example: Shana say hello
     *                        0    1    2
     *             startMessageSplitInt = 2
     * @param botCommandStartString the text to invoke the bot (currently "shana ")
     *
     * @return The Embed Object that will be added to the answer message.
     */
    public fun help(messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String): Embed {
        val embed = Embed()
        embed.title = "Command: say"
        val fieldList: MutableList<Field> = mutableListOf()
        fieldList.add(Field("say", "Shana gives the given text back.\n\n" +
                "User: ${botCommandStartString}say Hi, I am Shana.\n" +
                "Shana: Hi, I am Shana.\n"))
        embed.fields = fieldList
        return embed
    }

    /**
     * In this you can write something about your Addon.
     * This will be displayed only on the Website and should be a short Text.
     * Everyone should understand the purpose of this Addon is and what it does.
     *
     * @param botCommandStartString the text to invoke the bot (currently "shana ")
     * @return text that is displayed on the Website.
     */
    public fun description(botCommandStartString: String): String {
        return "The command let shana write the text after the command name.\n" +
                "Example: ${botCommandStartString}say Hi, I am Shana.\n" +
                "Shana: Hi, I am Shana.\n"
    }

    /**
     * This function will be executed over time (currently every 5min), if the Bot Admin remove your Addon from the Bot
     * or the bot will be closed from the Admin(Bot update).
     * You can use this function to saving important variables like rights, counters, ...
     *
     * @return String to save (normally a json)
     */
    public fun save(): String{
        var saveString = ""
        synchronized(saveClassLock){
            try {
                saveString = saveClass.save()
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
        return saveString
    }
    /**
     * This function will be executed if this Addon will be added to the Bot from an Admin or the Bot start.
     * You can use this function to load saved variables
     *
     * @param string from the saved File (normally the String from save())
     */
    public fun load(string: String) {
        synchronized(saveClassLock) {
            try {
                saveClass.load(string)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This is for sorting all active Addons on the Discord Server, in the help message.
     * Currently is only one Type supported.
     *
     * @return String of the Name to sort (Example: Fun)
     *
     */
    public fun commandType(): String{
        return "Other"
    }

    /**
     * This will be Executed if the Admin remove this Addon from the Bot or the Bot will be closed.
     * If your Addon have to do something before, you can put it in this function.
     * The save() function will be executed after the onExit() function.
     */
    public fun onExit(){
        // nothing to do
    }
}

/*
onMessageCreate(discordAPI: DiscordAPI, messageReceive: MessageReceive, messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String)
onMessageReactionAdd(discordAPI: DiscordAPI, messageReactionObject: MessageReactionObject)
help(messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String): Embed
description(): String
save(): String
load(string: String)
onExit()
 */
