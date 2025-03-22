package cn.tangsu99.fspauth;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

public class loginEvent {
    private final Logger logger = FSP_Auth.getInstance().getLogger();
    private static final ConfigurationNode config = FSP_Auth.getInstance().getConfig();
    @Subscribe
    public void onPreLoginEvent(PreLoginEvent event) {
        var result = API.checkWhitelist(event.getUsername(), event.getUniqueId().toString());
        if (result != null) {
            if (result.get("code").getAsInt() == 0) {
                logger.info("放行{}", event.getUsername());
            }else if (result.get("code").getAsInt() == 1) {
                event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text(config.node("kick-msg").getString()).color(NamedTextColor.RED)));
                logger.info("无白名单玩家{}尝试加入! ", event.getUsername());
            }
        } else {
            logger.info("请求出错");
        }
    }
}
