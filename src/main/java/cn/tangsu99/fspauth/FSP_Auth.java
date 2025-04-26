package cn.tangsu99.fspauth;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;

@Plugin(
        id = "fsp-auth",
        name = "FSP-Auth",
        version = BuildConstants.VERSION
)
public class FSP_Auth {
    private static FSP_Auth instance;
    private final ProxyServer proxy;
    private final Logger logger;
    private final ConfigurationNode config;

    @Inject
    public FSP_Auth(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        instance = this;
        config = new Config(dataDirectory).loadConfig();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
//        AuthCommand.register(proxy);
        logger.info("FSP-Auth registered");
        proxy.getEventManager().register(this, new loginEvent());
    }

    public static FSP_Auth getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
    public ConfigurationNode getConfig() {
        return config;
    }
}
