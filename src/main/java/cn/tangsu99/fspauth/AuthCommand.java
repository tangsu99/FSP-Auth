package cn.tangsu99.fspauth;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

public class AuthCommand {
    public static void register(ProxyServer proxy) {
        CommandManager commandManager = proxy.getCommandManager();
        // 创建指令节点
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("auth")
                .requires(source -> source instanceof Player)
                .then(BrigadierCommand.literalArgumentBuilder("register")
                        .then(BrigadierCommand.requiredArgumentBuilder("password", StringArgumentType.string())
                                .then(BrigadierCommand.requiredArgumentBuilder("repassword", StringArgumentType.string())
                                        .executes(context -> {
                                            context.getSource().sendMessage(Component.text("Registering..."));
                                            Player player = (Player) context.getSource();
                                            var p = player.getGameProfile();
                                            context.getSource().sendMessage(Component.text(API.register(p.getName(), "123", p.getId().toString())));
                                            return 1;
                                        })
                                )
                        )
                )
                .build();
        var a = new BrigadierCommand(node);
//        CommandMeta commandMeta = commandManager.metaBuilder(a)
//                .plugin(FSP_Auth.getInstance())
//                .build();
        commandManager.register(a);
    }
}