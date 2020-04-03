package fr.undev.linuxhacks.listeners;

import fr.undev.linuxhacks.command.Command;
import fr.undev.linuxhacks.command.Commands;
import fr.undev.linuxhacks.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientChatListener {
    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (args[0].startsWith(Commands.getPrefix())) {
            try {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getOriginalMessage());
                event.setCanceled(true);
                if (Commands.getRegistered().containsKey(args[0].replace(Commands.getPrefix(), ""))) {
                    Command command = Commands.getRegistered().get(args[0].replace(Commands.getPrefix(), ""));
                    if (command.onCommand(args[0], args)) {
                        ChatUtils.printMessage(ITextComponent.Serializer.fromJsonLenient((String)("{\"text\":\"Usage: " + Commands.getPrefix() + command.getUsage() + "\",\"color\":\"red\"}")));
                    }
                } else {
                    ChatUtils.printMessage(ITextComponent.Serializer.fromJsonLenient((String)("{\"text\":\"Unknown command. Type \\\"" + Commands.getPrefix() + "help\\\" for help.\",\"color\":\"red\"}")));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
