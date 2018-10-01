package com.shanabot.addon

import com.vgdragon.discordbot.discord.json.EditPosition
import com.vgdragon.discordbot.discord.json.Embed
import com.vgdragon.discordbot.discord.json.Field
import com.vgdragon.discordbot.discord.json.Permission
import com.vgdragon.discordbot.discord.json.api.send.ChannelModify
import com.vgdragon.discordbot.discord.json.api.send.CreateChannelInvite
import com.vgdragon.discordbot.discord.json.api.send.CreateGuildRole
import com.vgdragon.discordbot.discord.json.api.send.MessageSend
import com.vgdragon.discordbot.discord.json.websocket.payloadJson.MessageReceive
import com.vgdragon.discordbotaddon.DiscordAPI

fun example(discordAPI: DiscordAPI, messageReceive: MessageReceive, messageSplit: List<String>, startMessageSplitInt: Int, botCommandStartString: String){
    when {
        messageReceive.content.startsWith("${botCommandStartString}example say", true) -> {
            val split = messageReceive.content.split(" ")
            if(split.size < 3)
                return

            var sendText = ""
            for((i,s) in split.withIndex()){
                if(i < 2)
                    continue
                sendText = "$sendText $s"
            }
            discordAPI.sendMessage(sendText, messageReceive.channel_id)

            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example history", true) -> {
            val messagesHistory = discordAPI.getMessagesHistory() ?: return
            for (message in messagesHistory) {
                println("${message.author.username}: ${message.content}")
            }
            return
        }
        messageReceive.content.equals("${botCommandStartString}example delete channel", true) -> {
            discordAPI.deleteChannel(messageReceive.channel_id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example like me", true) -> {
            discordAPI.createReaction(messageReceive.id, "❤")
            Thread.sleep(4000)
            discordAPI.deleteOwnReaction(messageReceive.id, "❤")
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example delete my", true) -> {
            val messagesHistory = discordAPI.getMessagesHistory() ?: return
            
            if (messageSplit.size > startMessageSplitInt + 2)
                discordAPI.deleteReactionFromUser(messagesHistory[1].id, messageSplit[startMessageSplitInt + 1], messageReceive.author.id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example getReaction", true) -> {
            val messagesHistory = discordAPI.getMessagesHistory() ?: return
            if (messageSplit.size > startMessageSplitInt + 1) {
                val reactionUser = discordAPI.getReactionUser(messagesHistory[1].id, messageSplit[startMessageSplitInt + 1])
                println(reactionUser)
            }
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example message edit", true) -> {
            val messagesHistory = discordAPI.getMessagesHistory() ?: return

            if (messageSplit.size > startMessageSplitInt + 1) {
                var sendString = ""
                for ((intex, string) in messageSplit.withIndex()) {
                    if (intex > startMessageSplitInt + 1)
                        sendString = "$sendString $string"
                }
                discordAPI.editMessage(messagesHistory[1].id, sendString.trim())
                println("")
            }

            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example delete message", true) -> {
            discordAPI.deleteMessage(messageReceive.id)

            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example delete last message", true) -> {

            if (messageSplit.size > startMessageSplitInt + 3) {

                if (messageSplit[startMessageSplitInt + 3].toIntOrNull() == null)
                    return
                val messagesHistory = discordAPI.getMessagesHistory() ?: return
                val int = messageSplit[startMessageSplitInt + 3].toInt()
                val list = mutableListOf<String>()
                for ((i, m) in messagesHistory.withIndex()) {
                    if (i >= int)
                        break
                    list.add(m.id)
                }
                discordAPI.deleteMessage(list)
            }

            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get channel invites", true) -> {
            val channelInvites = discordAPI.getChannelInvites() ?: return
            var string = ""
            for (s in channelInvites)
                string = "$string, ${s.code}"
            discordAPI.sendMessage(MessageSend(string), messageReceive.channel_id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example create invite", true) -> {

            val createChannelInvites = discordAPI.createChannelInvites() ?: return

            discordAPI.sendMessage(createChannelInvites.code, messageReceive.channel_id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example create invite", true) -> {
            if (messageSplit.size < startMessageSplitInt + 3)
                return
            val inviteObject = when (messageSplit[startMessageSplitInt + 2]) {
                "0" -> discordAPI.createChannelInvites()
                "1" -> discordAPI.createChannelInvites(CreateChannelInvite(60))
                else -> null
            }
            if(inviteObject != null)
                discordAPI.sendMessage(inviteObject.code, messageReceive.channel_id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get channel", true) -> {
            val channel = discordAPI.getChannel() ?: return
            println(channel.name)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example Delete Channel Permission", true) -> {
            val channel = discordAPI.getChannel() ?: return
            if (channel.permission_overwrites.size > 0)
                discordAPI.deleteChannelPermission(channel.permission_overwrites[0].id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example start tipping", true) -> {
            discordAPI.triggerTypingIndicator()
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get pinned messages", true) -> {
            val pinnedMessages = discordAPI.getPinnedMessages()
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get Emoji List", true) -> {
            val guildEmojis = discordAPI.getGuildEmojis() ?: return
            var emojiString = ""
            for (e in guildEmojis)
                emojiString = "$emojiString, <:${e.name}:${e.id}>"
            discordAPI.sendMessage(emojiString, messageReceive.channel_id)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get guild", true) -> {
            val guild = discordAPI.getGuild() ?: return
            println(guild.name)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example create channel", true) -> {

            if (messageSplit.size < startMessageSplitInt + 3)
                return
            val dmChannel = discordAPI.getChannel() ?: return
            val channelModify = ChannelModify()
            channelModify.name = messageSplit[startMessageSplitInt + 2]
            channelModify.parent_id = dmChannel.parent_id
            channelModify.type = 0

            val createChannel = discordAPI.createChannel(channelModify) ?: return;
            println(createChannel.name)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example edit channelposition", true) -> {

            if (messageSplit.size < startMessageSplitInt + 3)
                return
            val position = messageSplit[startMessageSplitInt + 2].toIntOrNull() ?: return
            val dmChannel = discordAPI.getChannel() ?: return
            discordAPI.editChannelPosition(EditPosition(dmChannel.id, position))
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example edit channel", true) -> {
            if(messageSplit.size < startMessageSplitInt + 3)
                return
            val channelModify = ChannelModify()
            val channel = discordAPI.getChannel() ?: return
            channelModify.put(channel)
            channelModify.name = messageSplit[startMessageSplitInt + 2]
            discordAPI.editChannel(channelModify)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example get Members", true) -> {

            val guildMember = discordAPI.getGuildMember() ?: return
            var outputString = ""
            for (member in guildMember) {
                outputString = "$outputString, ${member.user.username}"
            }
            discordAPI.sendMessage(outputString)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example member edit", true) -> {
            if (messageSplit.size < startMessageSplitInt + 4)
                return
            discordAPI.editGuildMember(messageSplit[startMessageSplitInt + 2], nick = messageSplit[startMessageSplitInt + 3])
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example kick", true) -> {
            if (messageSplit.size < startMessageSplitInt + 2)
                return
            val userID = if (!messageSplit[2].startsWith("<@"))
                messageSplit[startMessageSplitInt + 1].substring(2, messageSplit[startMessageSplitInt + 1].length - 1)
            else
                messageSplit[2]
            discordAPI.kickMember(userID)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example ban", true) -> {
            if (messageSplit.size < startMessageSplitInt + 2)
                return
            if (!messageSplit[2].startsWith("<@"))
                return
            val userID = if (!messageSplit[2].startsWith("<@"))
                messageSplit[startMessageSplitInt + 1].substring(2, messageSplit[startMessageSplitInt + 1].length - 1)
            else
                messageSplit[2]
            if (messageSplit.size > startMessageSplitInt + 2) {
                var reason = ""
                for ((i, s) in messageSplit.withIndex()) {
                    if (i < startMessageSplitInt + 2)
                        continue
                    reason = "$reason $s"
                }
                discordAPI.banMember(userID, reason = reason)
            } else
                discordAPI.banMember(userID)
            return
        }
        messageReceive.content.startsWith("${botCommandStartString}example unban", true) -> {

            if (messageSplit.size < startMessageSplitInt + 2)
                return

            val userID = if (!messageSplit[2].startsWith("<@"))
                messageSplit[startMessageSplitInt + 1].substring(2, messageSplit[startMessageSplitInt + 1].length - 1)
            else
                messageSplit[2]
            discordAPI.deleteMemberBan(userID)
        }
        messageReceive.content.startsWith("${botCommandStartString}example get ban", true) -> {

            if (messageSplit.size < startMessageSplitInt + 3)
                return
            val userID = if (!messageSplit[2].startsWith("<@"))
                messageSplit[startMessageSplitInt + 1].substring(2, messageSplit[startMessageSplitInt + 1].length - 1)
            else
                messageSplit[2]

            val guildBan = discordAPI.getGuildBan(userID) ?: return
            discordAPI.sendMessage("User: ${guildBan.user}\n" +
                    "Reason: ${guildBan.reason}")
        }
        messageReceive.content.startsWith("${botCommandStartString}example get Listofban", true) -> {

            if (messageSplit.size < startMessageSplitInt + 2)
                return

            val guildBanList = discordAPI.getGuildBan() ?: return
            val messageSend = MessageSend()
            val fieldList: MutableList<Field> = mutableListOf()
            for (guildBan in guildBanList) {
                val reason = if (guildBan.reason == null)
                    "No Reason"
                else
                    guildBan.reason
                if (reason == null)
                    continue

                fieldList.add(Field(name = guildBan.user.username, value = reason))
            }
            messageSend.embed = Embed(fields = fieldList)
            discordAPI.sendMessage(messageSend)

        }
        messageReceive.content.startsWith("${botCommandStartString}example create role", true) -> {

            if (messageSplit.size < startMessageSplitInt + 2) {
                discordAPI.createGuildRole()
            } else {
                when(messageSplit[startMessageSplitInt + 2]){
                    "0" -> discordAPI.createGuildRole()
                    "1" ->{
                        val permission = Permission()
                        permission.createInstantInvite = true
                        permission.kickMembers = true
                        permission.banMembers = true
                        permission.administrator = true
                        permission.manageChannels = true
                        permission.manageGuild = true
                        permission.addReactions = true
                        permission.viewAuditLog = true
                        permission.viewChannel = true
                        permission.sendMessages = true
                        permission.sendTtsMessages = true
                        permission.manageMessages = true
                        permission.embedLinks = true
                        permission.attachFiles = true
                        permission.readMessageHistory = true
                        permission.mentionEveryone = true
                        permission.useExternalEmojis = true
                        permission.connect = true
                        permission.speak = true
                        permission.muteMembers = true
                        permission.deafenMembers = true
                        permission.moveMembers = true
                        permission.useVad = true
                        permission.changeNickname = true
                        permission.manageNicknames = true
                        permission.manageRoles = true
                        permission.manageWebhooks = true
                        permission.manageEmojis = true
                        val createGuildRole = CreateGuildRole(permission = permission)
                        discordAPI.createGuildRole(createGuildRole)
                    }
                }


            }

        }
        messageReceive.content.startsWith("${botCommandStartString}example edit roleposition", true) -> {
            if (messageSplit.size < startMessageSplitInt + 2)
                return
            val position = messageSplit[startMessageSplitInt + 2].toIntOrNull() ?: return
            val guildRoles = discordAPI.getGuildRoles() ?: return
            if (guildRoles.isEmpty())
                return
            val role = guildRoles[0]
            val editGuildRolePosition = discordAPI.editGuildRolePosition( EditPosition(role.id, position))
            return
        }
    }
}