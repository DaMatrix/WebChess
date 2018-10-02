package net.daporkchop.webchess.client.net;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.webchess.client.ClientMain;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.net.packet.UserDataPacket;

/**
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
public class WebChessSessionClient extends WebChessSession implements WebChessSession.ClientSession, ClientConstants {
    @NonNull
    public final ClientMain client;

    @Override
    public void handle(LoginResponsePacket packet) {
        this.client.loginData.handle(packet);
    }

    @Override
    public void handle(UserDataPacket packet) {
        this.client.cachedUsers.put(packet.user.getName(), packet.user);
        this.client.loginData.handle(packet);
    }

    @Override
    public void handle(LocaleDataPacket packet) {
        Localization.receiveLocales(packet);
    }
}
