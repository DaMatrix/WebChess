/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.webchess.server.net;

import com.esotericsoftware.kryonet.Connection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.conn.PorkConnection;
import net.daporkchop.lib.network.conn.Session;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.server.ServerMain;
import net.daporkchop.webchess.server.util.ServerConstants;
import net.daporkchop.webchess.server.util.ServerLocalization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ServerListener implements EndpointListener<WebChessSessionServer>, ServerConstants {
    @NonNull
    public final ServerMain server;

    @Override
    public void onConnect(WebChessSessionServer session) {
        ServerLocalization.sendLocales(session);

        if (IDE)    {
            try {
                Field f = Session.class.getDeclaredField("porkConnection");
                f.setAccessible(true);
                PorkConnection porkConnection = (PorkConnection) f.get(session);
                porkConnection.getNetConnection().setTimeout(10000000);
            } catch (Exception e)   {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisconnect(WebChessSessionServer session, String reason) {
        if (session.isLoggedIn()) {
            this.server.db.unload(session.getUser().getName());
            if (session.isIngame()) {
                session.currentOpponent.opponentLeft();
            }
        }
        /*if (session.challenged != null){
            session.challenged.send(new RematchCancelPacket("menu.rematch.left"));
        }*/
    }

    @Override
    public void onReceieve(WebChessSessionServer session, Packet packet) {
    }
}
