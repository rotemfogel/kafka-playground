package me.rotemfo.serde;

import com.google.gson.JsonSyntaxException;
import me.rotemfo.model.TickerSymbol;
import me.rotemfo.util.JsonUtil;
import org.apache.kafka.common.serialization.Deserializer;

public final class TickerSymbolDeserializer implements Deserializer<TickerSymbol> {

    @Override
    public TickerSymbol deserialize(String s, byte[] bytes) {
        try {
            return JsonUtil.fromJson(bytes, TickerSymbol.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
