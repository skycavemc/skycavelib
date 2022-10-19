package de.skycave.skycavelib.models;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import de.skycave.skycavelib.annotations.CreateDataFolder;
import de.skycave.skycavelib.annotations.InjectService;
import de.skycave.skycavelib.annotations.Prefix;
import de.skycave.skycavelib.exceptions.ServiceNotFoundException;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

@SuppressWarnings("unused")
public abstract class SkyCavePlugin extends JavaPlugin {

    private String prefix = "";

    public String getPrefix() {
        return prefix;
    }

    @Override
    public void onEnable() {
        Class<? extends SkyCavePlugin> clazz = SkyCavePlugin.this.getClass();
        if (clazz.isAnnotationPresent(CreateDataFolder.class)) {
            if (!getDataFolder().isDirectory()) {
                //noinspection ResultOfMethodCallIgnored
                getDataFolder().mkdirs();
            }
        }

        if (clazz.isAnnotationPresent(Prefix.class)) {
            prefix = clazz.getAnnotation(Prefix.class).value();
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectService.class)) {
                Class<?> fieldClass = field.getType();
                RegisteredServiceProvider<?> provider = getServer().getServicesManager()
                        .getRegistration(fieldClass);

                if (provider == null) {
                    getLogger().severe("No registered service found for class " + fieldClass.getName());
                    getServer().getPluginManager().disablePlugin(this);
                    throw new ServiceNotFoundException();
                }

                field.setAccessible(true);
                try {
                    field.set(SkyCavePlugin.this, provider.getProvider());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    public void registerCommand(String command, CommandExecutor executor) {
        PluginCommand cmd = getCommand(command);
        Validate.notNull(cmd);
        cmd.setExecutor(executor);
    }

    public void registerEvents(Listener @NotNull ... events) {
        Validate.notNull(events);
        for (Listener event : events) {
            getServer().getPluginManager().registerEvents(event, this);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean copyResource(@NotNull String resourceName) {
        Validate.notNull(resourceName);
        File destination = new File(getDataFolder(), resourceName);
        URL resource = getClass().getClassLoader().getResource(resourceName);
        Validate.notNull(resource);

        if (destination.isFile()) {
            getLogger().info("The file " + resourceName + " already exists.");
            return true;
        }

        try {
            //noinspection ResultOfMethodCallIgnored
            destination.createNewFile();
            Resources.asByteSource(resource).copyTo(Files.asByteSink(destination));
            getLogger().info("The file " + resourceName + " has been created.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
