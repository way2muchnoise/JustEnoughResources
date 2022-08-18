package jeresources.api;

/**
 * Apply to a class to receive an instance of {@link IJERAPI}
 * This instance can be used to register integration with JER
 */
public interface IJERPlugin {
    String entry_point = "jer_mod_plugin";

    void receive(IJERAPI api);
}
