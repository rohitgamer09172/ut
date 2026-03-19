/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  lombok.Generated
 */
package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import ac.grim.grimac.utils.data.webhook.discord.EmbedAuthor;
import ac.grim.grimac.utils.data.webhook.discord.EmbedField;
import ac.grim.grimac.utils.data.webhook.discord.EmbedFooter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.Objects;
import lombok.Generated;

public class Embed
implements JsonSerializable {
    public static final int MAX_TITLE_LENGTH = 256;
    public static final int MAX_DESCRIPTION_LENGTH = 4096;
    public static final int MAX_FIELDS = 25;
    @Nullable
    private String title;
    @NotNull
    private String description;
    @Nullable
    private String titleURL;
    @Nullable
    private Instant timestamp;
    @Nullable
    private Integer color;
    @Nullable
    private EmbedFooter footer;
    @Nullable
    private String imageURL;
    @Nullable
    private String thumbnailURL;
    @Nullable
    private EmbedAuthor author;
    private @NotNull EmbedField @Nullable [] fields;

    public Embed(@NotNull String description) {
        this.description(description);
    }

    public Embed(@NotNull JsonElement jsonElement) {
        JsonObject json = jsonElement.getAsJsonObject();
        this.description(json.get("description").getAsString());
        JsonElement element = json.get("title");
        if (element != null) {
            this.title(element.getAsString());
        }
        if ((element = json.get("url")) != null) {
            this.titleURL(element.getAsString());
        }
        if ((element = json.get("timestamp")) != null) {
            this.timestamp(Instant.parse(element.getAsString()));
        }
        if ((element = json.get("color")) != null) {
            this.color(element.getAsInt());
        }
        if ((element = json.get("footer")) != null) {
            this.footer(new EmbedFooter(element));
        }
        if ((element = json.get("image")) != null) {
            this.imageURL(element.getAsJsonObject().get("url").getAsString());
        }
        if ((element = json.get("thumbnail")) != null) {
            this.imageURL(element.getAsJsonObject().get("url").getAsString());
        }
        if ((element = json.get("author")) != null) {
            this.author(new EmbedAuthor(element));
        }
        if ((element = json.get("fields")) != null) {
            this.fields((EmbedField[])JsonSerializable.deserializeArray((JsonArray)element.getAsJsonArray(), EmbedField[]::new, EmbedField::new));
        }
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public Embed description(@NotNull String description) {
        Objects.requireNonNull(description, "Embed description cannot be null!");
        if (description.length() > 4096) {
            throw new IllegalArgumentException("Embed description too long, " + description.length() + " > 4096");
        }
        this.description = description;
        return this;
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public Embed title(@Nullable String title) {
        if (title != null && title.length() > 256) {
            throw new IllegalArgumentException("Embed title too long, " + title.length() + " > 256");
        }
        this.title = title;
        return this;
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public Embed fields(@NotNull EmbedField @Nullable [] fields) {
        if (fields != null) {
            if (fields.length > 25) {
                throw new IllegalArgumentException("Too many fields, " + fields.length + " > 25");
            }
            for (EmbedField field : fields) {
                Objects.requireNonNull(field);
            }
        }
        this.fields = fields;
        return this;
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public Embed addFields(EmbedField ... fields) {
        if (fields.length == 0) {
            return this;
        }
        if (this.fields() == null) {
            return this.fields(fields);
        }
        EmbedField[] newFields = new EmbedField[this.fields().length + fields.length];
        System.arraycopy(this.fields(), 0, newFields, 0, this.fields().length);
        System.arraycopy(fields, this.fields().length, newFields, this.fields().length, fields.length);
        return this.fields(newFields);
    }

    @NotNull
    public Embed footer(@Nullable EmbedFooter footer) {
        this.footer = footer == null || footer.icon() == null && footer.text().isBlank() ? null : footer;
        return this;
    }

    @NotNull
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("description", this.description());
        if (this.title() != null) {
            json.addProperty("title", this.title());
        }
        if (this.color() != null) {
            json.addProperty("color", (Number)(this.color() & 0xFFFFFF));
        }
        if (this.titleURL() != null) {
            json.addProperty("url", this.titleURL());
        }
        if (this.timestamp() != null) {
            json.addProperty("timestamp", this.timestamp().toString());
        }
        if (this.footer() != null) {
            json.add("footer", this.footer().toJson());
        }
        if (this.imageURL() != null) {
            JsonObject image = new JsonObject();
            image.addProperty("url", this.imageURL());
            json.add("image", (JsonElement)image);
        }
        if (this.thumbnailURL() != null) {
            JsonObject thumbnail = new JsonObject();
            thumbnail.addProperty("url", this.thumbnailURL());
            json.add("thumbnail", (JsonElement)thumbnail);
        }
        if (this.author() != null) {
            json.add("author", this.author().toJson());
        }
        if (this.fields() != null) {
            json.add("fields", (JsonElement)JsonSerializable.serializeArray(this.fields()));
        }
        return json;
    }

    @Nullable
    @Generated
    public String title() {
        return this.title;
    }

    @NotNull
    @Generated
    public String description() {
        return this.description;
    }

    @Nullable
    @Generated
    public String titleURL() {
        return this.titleURL;
    }

    @Nullable
    @Generated
    public Instant timestamp() {
        return this.timestamp;
    }

    @Nullable
    @Generated
    public Integer color() {
        return this.color;
    }

    @Nullable
    @Generated
    public EmbedFooter footer() {
        return this.footer;
    }

    @Nullable
    @Generated
    public String imageURL() {
        return this.imageURL;
    }

    @Nullable
    @Generated
    public String thumbnailURL() {
        return this.thumbnailURL;
    }

    @Nullable
    @Generated
    public EmbedAuthor author() {
        return this.author;
    }

    @NotNull
    @Generated
    public @NotNull EmbedField @Nullable [] fields() {
        return this.fields;
    }

    @Generated
    public Embed titleURL(@Nullable String titleURL) {
        this.titleURL = titleURL;
        return this;
    }

    @Generated
    public Embed timestamp(@Nullable Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Generated
    public Embed color(@Nullable Integer color) {
        this.color = color;
        return this;
    }

    @Generated
    public Embed imageURL(@Nullable String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    @Generated
    public Embed thumbnailURL(@Nullable String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
        return this;
    }

    @Generated
    public Embed author(@Nullable EmbedAuthor author) {
        this.author = author;
        return this;
    }
}

