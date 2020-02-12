package utility

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

private const val prefix = "§a§lT §8>> §f"

fun CommandSender.message(input: String, hasPrefix: Boolean = true) = sendMessage(ChatColor.translateAlternateColorCodes('§', (if (hasPrefix) prefix else "") + input))

fun staff(message: String) = Bukkit.broadcast("$prefix$message", "tickets.staff")

fun log(message: String) = Bukkit.getConsoleSender().sendMessage("$prefix$message")

fun getOfflinePlayer(input: String?) = if (input != null) Bukkit.getOfflinePlayer(input) else null