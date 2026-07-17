import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumFacing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonGenerator {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DIRECTORY = new File("").getAbsolutePath();

    public static void main(String[] args) throws IOException {
        //scrollshelfState();
        //scrollshelfModels();
        //aleaDiaboliModels();
    }

    private static void scrollshelfModels() {
        for (int i = 0; i < 8; i++) {
            File file = new File(DIRECTORY + ("/src/main/resources/assets/magiadaemonica/models/block/scrollshelf_scroll_" + i + ".json"));
            System.out.println(file);
            JsonObject obj = new JsonObject();
            obj.addProperty("parent", "magiadaemonica:block/cover");
            JsonObject textures = new JsonObject();
            textures.addProperty("texture", "magiadaemonica:blocks/scrollshelf_scroll_" + i);
            textures.addProperty("particle", "magiadaemonica:blocks/scrollshelf");
            obj.add("textures", textures);
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(GSON.toJson(obj));
                writer.close();
            } catch (Exception e) {}
        }
    }

    private static void scrollshelfState() {
        File file = new File(DIRECTORY + "/src/main/resources/assets/magiadaemonica/blockstates/scrollshelf.json");
        JsonObject obj = new JsonObject();
        JsonArray multipart = new JsonArray();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            writeMultipart(multipart, facing, "magiadaemonica:scrollshelf", null);
            for (int i = 0; i < 8; i++) {
                writeMultipart(multipart, facing, "magiadaemonica:scrollshelf_scroll_" + i, "scroll_" + i);
            }
        }
        obj.add("multipart", multipart);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(GSON.toJson(obj));
        } catch (Exception e) {}
    }

    private static void writeMultipart(JsonArray multipart, EnumFacing facing, String model, String property) {
        JsonObject obj = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", model);
        if (facing != EnumFacing.NORTH) {
            apply.addProperty("y", facing.getOpposite().getHorizontalIndex() * 90);
            apply.addProperty("uvlock", true);
        }
        obj.add("apply", apply);
        JsonObject when = new JsonObject();
        if (facing != null) when.addProperty("facing", facing.getName());
        if (property != null) when.addProperty(property, true);
        obj.add("when", when);
        multipart.add(obj);
    }

    private static void aleaDiaboliModels() {
        for (int i = 1; i <= 20; i++) {
            File file = new File(DIRECTORY + ("/src/main/resources/assets/magiadaemonica/models/item/alea_diaboli/alea_diaboli_" + i + ".json"));
            System.out.println(file);
            JsonObject obj = new JsonObject();
            obj.addProperty("parent", "minecraft:item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "magiadaemonica:items/alea_diaboli");
            textures.addProperty("layer1", "magiadaemonica:items/alea_diaboli/number_" + i);
            obj.add("textures", textures);
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(GSON.toJson(obj));
                writer.close();
            } catch (Exception e) {}
        }
        File file = new File(DIRECTORY + ("/src/main/resources/assets/magiadaemonica/models/item/alea_diaboli.json"));
        System.out.println(file);
        JsonObject obj = new JsonObject();
        obj.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "magiadaemonica:items/alea_diaboli");
        obj.add("textures", textures);
        JsonArray overrides = new JsonArray();
        for (int i = 1; i <= 20; i++) {
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("magiadaemonica:number", i);
            override.add("predicate", predicate);
            override.addProperty("model", "magiadaemonica:item/alea_diaboli/alea_diaboli_"+i);
            overrides.add(override);
        }
        obj.add("overrides", overrides);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(GSON.toJson(obj));
            writer.close();
        } catch (Exception e) {}
    }

}
