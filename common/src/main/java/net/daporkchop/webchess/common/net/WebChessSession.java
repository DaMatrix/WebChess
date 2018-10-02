package net.daporkchop.webchess.common.net;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.daporkchop.lib.network.conn.Session;
import net.daporkchop.webchess.common.net.packet.LocaleDataPacket;
import net.daporkchop.webchess.common.net.packet.LoginRequestPacket;
import net.daporkchop.webchess.common.net.packet.LoginResponsePacket;
import net.daporkchop.webchess.common.net.packet.UserDataPacket;
import net.daporkchop.webchess.common.user.User;
import net.daporkchop.webchess.common.util.Constants;

/**
 * @author DaPorkchop_
 */
@Getter
@Setter
public abstract class WebChessSession extends Session implements Constants {
    @NonNull
    protected User user;

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public interface ClientSession {
        void handle(@NonNull LoginResponsePacket packet);

        void handle(@NonNull UserDataPacket packet);

        void handle(@NonNull LocaleDataPacket packet);
    }

    public interface ServerSession {
        void handle(@NonNull LoginRequestPacket packet);
    }
}
