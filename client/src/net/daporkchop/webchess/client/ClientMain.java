package net.daporkchop.webchess.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.daporkchop.lib.network.endpoint.EndpointListener;
import net.daporkchop.lib.network.endpoint.builder.ClientBuilder;
import net.daporkchop.lib.network.endpoint.client.PorkClient;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.client.net.WebChessSessionClient;
import net.daporkchop.webchess.client.util.ChessTex;
import net.daporkchop.webchess.client.util.ClientConstants;
import net.daporkchop.webchess.client.util.Localization;
import net.daporkchop.webchess.common.game.impl.Side;
import net.daporkchop.webchess.common.game.impl.chess.ChessBoard;
import net.daporkchop.webchess.common.game.impl.chess.figure.ChessFigure;
import net.daporkchop.webchess.common.net.WebChessProtocol;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.net.packet.UpdateColorPacket;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ClientMain extends ApplicationAdapter implements ClientConstants {
    @NonNull
    private final String localAddress;
    private final ChessBoard board = new ChessBoard();
    public int color = 0x777777;
    private PorkClient<WebChessSession> client;

    @Override
    public void create() {
        Localization.init();
        ChessTex.init();

        if (false) {
            this.client = new ClientBuilder<WebChessSession>()
                    .setProtocol(new WebChessProtocol(WebChessSessionClient::new))
                    .setAddress(new InetSocketAddress(this.localAddress, NETWORK_PORT))
                    .addListener(new EndpointListener<WebChessSession>() {
                        @Override
                        public void onConnect(WebChessSession session) {
                        }

                        @Override
                        public void onDisconnect(WebChessSession sesion, String reason) {
                        }

                        @Override
                        public void onReceieve(WebChessSession session, Packet packet) {
                            if (packet instanceof UpdateColorPacket) {
                                ClientMain.this.color = ((UpdateColorPacket) packet).color;
                            }
                        }
                    })
                    .build();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor((float) (color & 0xFF) / 255.0f, (float) ((color >> 8) & 0xFF) / 255.0f, (float) ((color >> 16) & 0xFF) / 255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (true) {
            batch.begin();
            //this.batch.draw(this.img, 0, 0);
            /*{
                ChessFigure figure = this.board.getFigure(1, 1);
                //this.batch.setColor(0.5f, 0.5f, 0.0f, 1.0f);
                figure.getValidMovePositions().forEach(pos -> this.batch.draw(this.img, pos.x * 16, pos.y * this.font.getLineHeight(), 16, this.font.getLineHeight()));
            }*/
            for (int y = 7; y >= 0; y--) {
                for (int x = 7; x >= 0; x--) {
                    ChessFigure figure = this.board.getFigure(x, y);
                    /*if (figure != null && figure.getSide() == Side.WHITE) {
                        this.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                    } else {
                        this.font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
                    }
                    ChessTex.font.draw(batch, "" + (figure == null ? ' ' : figure.getCode()), x * 16, (y + 1) * this.font.getLineHeight());*/
                    ChessTex.draw(figure, x, y);
                }
            }
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        ChessTex.dispose();

        if (this.client != null && this.client.isRunning()) {
            this.client.close("User exit");
        }
    }
}
