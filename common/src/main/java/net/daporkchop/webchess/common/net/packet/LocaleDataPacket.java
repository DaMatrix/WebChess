package net.daporkchop.webchess.common.net.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.daporkchop.lib.binary.stream.DataIn;
import net.daporkchop.lib.binary.stream.DataOut;
import net.daporkchop.lib.network.packet.Codec;
import net.daporkchop.lib.network.packet.Packet;
import net.daporkchop.webchess.common.net.WebChessSession;
import net.daporkchop.webchess.common.util.locale.Locale;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class LocaleDataPacket implements Packet {
    @NonNull
    public Locale locale;

    public Map<String, String> mappings = new Hashtable<>();

    @Override
    public void read(DataIn in) throws IOException {
        this.locale = Locale.valueOf(in.readUTF());
        this.mappings.clear();
        for (int i = in.readInt(); i > 0; i--) {
            this.mappings.put(in.readUTF(), in.readUTF());
        }
    }

    @Override
    public void write(DataOut out) throws IOException {
        out.writeUTF(this.locale.name());
        out.writeInt(this.mappings.size());
        for (Map.Entry<String, String> entry : this.mappings.entrySet())    {
            out.writeUTF(entry.getKey());
            out.writeUTF(entry.getValue());
        }
    }

    public static class LocaleDataCodec<S extends WebChessSession> implements Codec<LocaleDataPacket, S>     {
        @Override
        public void handle(LocaleDataPacket packet, S session) {
            ((WebChessSession.ClientSession) session).handle(packet);
        }

        @Override
        public LocaleDataPacket newPacket() {
            return new LocaleDataPacket();
        }
    }
}
