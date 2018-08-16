package com.shanabot.addon
import com.vgdragon.discordbot.discord.json.Embed
import com.vgdragon.discordbot.discord.json.Field
import com.vgdragon.discordbot.discord.json.websocket.payloadJson.MessageReceive
import com.vgdragon.discordbot.discord.json.websocket.payloadJson.events.MessageReactionObject
import com.vgdragon.discordbotaddon.DiscordAPI


open class Addon {
    /*
   * messageSplit = messageReceive.content.split(" ")
   * it give you a slitted message to work with. You can split it yourself, too.
   *
   * startMessageSplitInt
   * The bot normally use "shana <commandName> ..." and startMessageSplitInt would be 2.
   * I could happen that the bot change that, for example to "s!<command> ...> and then startMessageSplitInt would be 1.
   * It is better to use startMessageSplitInt to be save.
   *
    */
    val saveClassLock = Object()
    var saveClass = SaveClass()


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

    public fun onMessageReactionAdd(discordAPI: DiscordAPI, messageReactionObject: MessageReactionObject){
        if(!messageReactionObject.emoji.name.equals("❌"))
            return
        discordAPI.deleteMessage(messageReactionObject.message_id)
    }

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

    public fun description(botCommandStartString: String): String {

        return "The command let shana write the text after the command name.\n" +
                "Example: ${botCommandStartString}say Hi, I am Shana.\n" +
                "Shana: Hi, I am Shana.\n"
    }

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
    public fun load(string: String) {
        synchronized(saveClassLock) {
            try {
                saveClass.load(string)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    public fun commandType(): String{
        return ""
    }
}

/*
onMessageCreate(discordAPI: DiscordAPI, messageReceive: MessageReceive, messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String)
onMessageReactionAdd(discordAPI: DiscordAPI, messageReactionObject: MessageReactionObject)
help(messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String): Embed
description(): String
save(): String
load(string: String)
 */
