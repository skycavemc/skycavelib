package de.skycave.skycavelib.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Utils {

    /**
     * Transforms a location into a string containing the x, y and z components of the location.
     * @param location The location to transform
     * @return The result
     */
    public static @NotNull String locationAsString(@NotNull Location location) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(location.getX()) + ", " +
                decimalFormat.format(location.getY()) + ", " +
                decimalFormat.format(location.getZ());
    }

}
