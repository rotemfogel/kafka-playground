package me.rotemfo.serde;

import com.google.gson.JsonSyntaxException;
import me.rotemfo.model.TickerSymbol;
import me.rotemfo.util.JsonUtil;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public final class TickerSymbolSerializer implements Serializer<TickerSymbol> {

    @Override
    public byte[] serialize(String s, TickerSymbol tickerSymbol) {
        try {
            return JsonUtil.toJson(tickerSymbol).getBytes(StandardCharsets.UTF_8);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}