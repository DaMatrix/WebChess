package net.daporkchop.webchess.common.net;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.daporkchop.lib.network.conn.Session;
import net.daporkchop.webchess.common.user.User;

/**
 * @author DaPorkchop_
 */
@Getter
@Setter
public abstract class WebChessSession extends Session {
    @NonNull
    private User user;
}
