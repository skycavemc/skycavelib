package de.skycave.skycavelib.models;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Class that takes a raw string and returns the string with translated color codes and prefix.
 * Also helps to send messages out to players easier.
 */
@SuppressWarnings("unused")
public class ChatMessage {

    private final String prefix;
    private String base;

    /**
     * Creates a new colored string instance from a PrefixHolder instance and a base string.
     * @param prefix The prefix for chat messages
     * @param base The raw string to save
     */
    public ChatMessage(String prefix, String base) {
        this.prefix = prefix;
        this.base = base;
    }

    /**
     * Replaces the first occurrence of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    public ChatMessage replace(String from, String to) {
        base = base.replace(from, to);
        return this;
    }

    /**
     * Replaces all occurrences of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    public ChatMessage replaceAll(String from, String to) {
        while (base.contains(from)) {
            base = base.replace(from, to);
        }
        return this;
    }

    /**
     * Gets the string with prefix and translated Minecraft color codes.
     * @return The string output
     */
    public String get() {
        base = ChatColor.translateAlternateColorCodes('&', prefix + base);
        return base;
    }

    /**
     * Gets the string with translated Minecraft color codes and optionally with prefix.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @return The string output
     */
    public String get(boolean prefix) {
        if (prefix) {
            base = prefix + base;
        }
        base = ChatColor.translateAlternateColorCodes('&', base);
        return base;
    }

    /**
     * Gets the string, optionally with prefix and optionally with translated Minecraft color codes.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @param formatted Whether to translate Minecraft color codes
     * @return The string output
     */
    public String get(boolean prefix, boolean formatted) {
        if (prefix) {
            base = prefix + base;
        }
        if (formatted) {
            base = ChatColor.translateAlternateColorCodes('&', base);
        }
        return base;
    }

    /**
     * Directly sends the message result to the specified CommandSender.
     * @param sender The receiver
     */
    public void send(@NotNull CommandSender sender) {
        sender.sendMessage(get());
    }

    /**
     * Directly sends the message result to the specified CommandSender, optionally with prefix.
     * @param sender The receiver
     * @param prefix Whether to start with the prefix of the prefix holder
     */
    public void send(@NotNull CommandSender sender, boolean prefix) {
        sender.sendMessage(get(prefix));
    }

    /**
     * Directly sends the message result to the specified CommandSender, optionally with prefix
     * and optionally with translated Minecraft color codes.
     * @param sender The receiver
     * @param prefix Whether to start with the prefix of the prefix holder
     * @param formatted Whether to translate Minecraft color codes
     */
    public void send(@NotNull CommandSender sender, boolean prefix, boolean formatted) {
        sender.sendMessage(get(prefix, formatted));
    }

}
