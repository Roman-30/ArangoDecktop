package vsu.csf.arangodbdecktop.model;

import com.google.bigtable.repackaged.com.google.gson.TypeAdapter;
import com.google.bigtable.repackaged.com.google.gson.stream.JsonReader;
import com.google.bigtable.repackaged.com.google.gson.stream.JsonWriter;
import vsu.csf.arangodbdecktop.controllers.Quarry;

import java.io.IOException;

public class QuarryAdapter extends TypeAdapter<Quarry> {
    @Override
    public void write(JsonWriter out, Quarry value) throws IOException {
        out.beginObject();
        out.name("quarry").value(value.getQuarry());
        out.endObject();
    }

    @Override
    public Quarry read(JsonReader in) throws IOException {
        in.beginObject();
        String quarry = null;
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("quarry")) {
                quarry = in.nextString();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return new Quarry(quarry);
    }
}
