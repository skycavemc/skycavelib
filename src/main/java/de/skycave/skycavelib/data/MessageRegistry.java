package de.skycave.skycavelib.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.skycave.skycavelib.models.ChatMessage;
import de.skycave.skycavelib.models.SkyCavePlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registry for chat messages that can be edited at any time without needing to touch the source code.
 */
@SuppressWarnings("unused")
public class MessageRegistry {

    private final File dataFolder;
    private final SkyCavePlugin plugin;
    private final Map<String, String> messages = new HashMap<>();

    /**
     * Creates a new message registry instance and loads the existing messages.
     * @param plugin Instance of your plugin
     */
    public MessageRegistry(@NotNull SkyCavePlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        load();
    }

    /**
     * Registers a new message and saves it. Use update if you want to change the message text later.
     * The key may only contain lowercase letters and numbers separated by dashes (-). This is to maintain consistency.
     * @param key The key to the message
     * @param message The message itself
     * @throws IllegalArgumentException if the key does not match the pattern
     * @return true - if the message was registered, false - if the message already exists
     */
    public boolean register(String key, String message) {
        Pattern pattern = Pattern.compile("^(?!-)([a-z0-9\\-]+)(?<!-)$"); // example: test-str1ng-98-abc
        Matcher matcher = pattern.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Key " + key + " does not match pattern.");
        }
        if (messages.containsKey(key)) {
            return false;
        }
        messages.put(key, message);
        save();
        return true;
    }

    /**
     * Registers the new messages and saves them. Use update if you want to change one of the messages' text later.
     * The key may only contain lowercase letters and numbers separated by dashes (-). This is to maintain consistency.
     * @param messages A map of messages to register
     * @throws IllegalArgumentException if the key does not match the pattern
     * @return true - if the message was registered, false - if the message already exists
     */
    public boolean registerMany(@NotNull Map<String, String> messages) {
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            if (!register(entry.getKey(), entry.getValue())) return false;
        }
        return true;
    }

    /**
     * Deletes a message.
     * @param key The key to the message
     */
    public void delete(String key) {
        messages.remove(key);
        save();
    }

    /**
     * Updates a message.
     * @param key The key to the message
     * @param message The updated message
     * @return true - if the message was updated, false - if a message with that key does not exist
     */
    public boolean update(String key, String message) {
        if (!messages.containsKey(key)) {
            return false;
        }
        messages.put(key, message);
        save();
        return true;
    }

    /**
     * Gets a registered message as a chat message that can be easily edited and sent to a player.
     * @param key The key to the message
     * @throws IllegalArgumentException if a message with that key does not exist.
     * @return The created chat message instance
     */
    public ChatMessage get(String key) {
        if (!messages.containsKey(key)) {
            throw new IllegalArgumentException("That message does not exist!");
        }
        return new ChatMessage(plugin.getPrefix(), messages.get(key));
    }

    /**
     * Loads all messages from the message registry.
     */
    @SuppressWarnings("UnstableApiUsage")
    public void load() {
        if (!dataFolder.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }
        File registry = new File(dataFolder, "message_registry.json");
        if (!registry.isFile()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                registry.createNewFile();
                plugin.getLogger().severe("Message registry created.");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileReader reader = new FileReader(registry)) {
            Type mapType = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> result = gson.fromJson(reader, mapType);
            messages.putAll(result);
            plugin.getLogger().info("Message registry loaded successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe("There was an error loading the message registry.");
            e.printStackTrace();
        }
    }

    /**
     * Saves all messages to the registry file. This should only be called internally,
     * and you do not need to use it outside this class.
     */
    private void save() {
        if (!dataFolder.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }
        File registry = new File(dataFolder, "message_registry.json");
        if (!registry.isFile()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                registry.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(registry)) {
            String jsonResult = gson.toJson(messages);
            writer.write(jsonResult);
            writer.flush();
            plugin.getLogger().info("Message registry saved successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe("There was an error saving the message registry.");
            e.printStackTrace();
        }
    }

}
