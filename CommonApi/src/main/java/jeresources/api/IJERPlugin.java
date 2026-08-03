package jeresources.api;

/**
 * Implement to receive an instance of {@link IJERAPI}
 * This instance can be used to register integration with JER
 *
 * NeoForge: annotate the implementing class with {@link JERPlugin}
 * Fabric: list it as the {@link #entry_point} entrypoint in fabric.mod.json
 */
public interface IJERPlugin {
    String entry_point = "jer_mod_plugin";

    void receive(IJERAPI api);
}
