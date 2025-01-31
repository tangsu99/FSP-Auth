package cn.tangsu99.fspauth;

import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private final Logger logger = FSP_Auth.getInstance().getLogger();
    private final Path dataDirectory;
    public Config(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
    public ConfigurationNode loadConfig() {
        // 配置文件路径
        Path configFile = dataDirectory.resolve("config.conf");

        // 如果配置文件不存在，则从资源中复制
        if (!configFile.toFile().exists()) {
            dataDirectory.toFile().mkdirs(); // 创建插件数据目录
            try {
                Files.copy(getClass().getResourceAsStream("/config.conf"), configFile);
            } catch (IOException e) {
                logger.error("Failed to create config file!", e);
                return null;
            }
        }

        // 加载配置文件
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(configFile)
                .build();

        try {
            return loader.load();
        } catch (IOException e) {
            logger.error("Failed to load config file!", e);
            return null;
        }
    }
}