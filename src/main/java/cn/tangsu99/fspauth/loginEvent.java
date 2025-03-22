package cn.tangsu99.fspauth;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;

public class loginEvent {
    private final Logger logger = FSP_Auth.getInstance().getLogger();
    private static final ConfigurationNode config = FSP_Auth.getInstance().getConfig();
    private static final HashMap<String, String> msg = new HashMap<>();
    @Subscribe
    public void onPreLoginEvent(PreLoginEvent event) {
        var result = API.checkWhitelist(event.getUsername(), event.getUniqueId().toString());
        if (result != null) {
            int code = result.get("code").getAsInt();
            String desc = result.get("desc").getAsString();
            if (code == 0) {
                logger.info("放行{}", event.getUsername());
            }else if (code == 2) {
                logger.info("放行{}; {}", event.getUsername(), desc);
                msg.put(event.getUsername(), desc);
            }else if (result.get("code").getAsInt() == 1) {
                event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text(config.node("kick-msg").getString()).color(NamedTextColor.RED)));
                logger.info("无白名单玩家{}尝试加入! ", event.getUsername());
            }
        } else {
            logger.info("请求出错");
        }
    }

    @Subscribe
    public void onJoin(LoginEvent event) {
        var name = event.getPlayer().getUsername();
        if (msg.containsKey(name)) {
            event.getPlayer().sendMessage(Component.text(msg.get(name)));
            msg.remove(name);
        }
    }
}
