package org.acme;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

public class Weather {
    @Tool(description = "Get weather detail for a location based on latitude and longitude.")
    String getWeatherDetails(@ToolArg(description = "Latitude of the location") double latitude,
                             @ToolArg(description = "Longitude of the location") double longitude) {
        // Make any api call to a third party weather api service to get weather details.
        return "The weather is pleasant sunny with a slight prediction of rain in Bangalore.";
    }
}
