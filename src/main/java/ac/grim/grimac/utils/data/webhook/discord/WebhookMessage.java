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
import ac.grim.grimac.utils.data.webhook.discord.Embed;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;

public class WebhookMessage
implements JsonSerializable {
    public static final int MAX_CONTENT_LENGTH = 2000;
    public static final int MAX_EMBEDS = 10;
    @Nullable
    private String content;
    @Nullable
    private String username;
    @Nullable
    private String avatar;
    @Nullable
    private Boolean tts;
    private @NotNull Embed @Nullable [] embeds;

    public WebhookMessage() {
    }

    public WebhookMessage(@NotNull JsonObject json) {
        JsonElement element = json.get("content");
        if (element != null) {
            this.content(element.getAsString());
        }
        if ((element = json.get("username")) != null) {
            this.username(element.getAsString());
        }
        if ((element = json.get("avatar_url")) != null) {
            this.avatar(element.getAsString());
        }
        if ((element = json.get("tts")) != null) {
            this.tts(element.getAsBoolean());
        }
        if ((element = json.get("embeds")) != null) {
            this.embeds((Embed[])JsonSerializable.deserializeArray((JsonArray)element.getAsJsonArray(), Embed[]::new, Embed::new));
        }
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public WebhookMessage content(@Nullable String content) {
        if (content != null && content.length() > 2000) {
            throw new IllegalArgumentException("Webhook content too long, " + content.length() + " > 2000");
        }
        this.content = content;
        return this;
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public WebhookMessage embeds(@NotNull Embed @Nullable [] embeds) {
        if (embeds != null) {
            if (embeds.length > 10) {
                throw new IllegalArgumentException("Too many embeds, " + embeds.length + " > 10");
            }
            for (Embed embed : embeds) {
                Objects.requireNonNull(embed);
            }
        }
        this.embeds = embeds;
        return this;
    }

    @Contract(value="_ -> this", mutates="this")
    @NotNull
    public WebhookMessage addEmbeds(Embed ... embeds) {
        if (embeds.length == 0) {
            return this;
        }
        if (this.embeds() == null) {
            return this.embeds(embeds);
        }
        Embed[] newEmbeds = new Embed[this.embeds().length + embeds.length];
        System.arraycopy(this.embeds(), 0, newEmbeds, 0, this.embeds().length);
        System.arraycopy(embeds, this.embeds().length, newEmbeds, this.embeds().length, embeds.length);
        return this.embeds(newEmbeds);
    }

    @NotNull
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (this.content() != null) {
            json.addProperty("content", this.content());
        }
        if (this.username() != null) {
            json.addProperty("username", this.username());
        }
        if (this.avatar() != null) {
            json.addProperty("avatar_url", this.avatar());
        }
        if (this.tts() != null) {
            json.addProperty("tts", this.tts());
        }
        if (this.embeds() != null) {
            json.add("embeds", (JsonElement)JsonSerializable.serializeArray(this.embeds()));
        }
        return json;
    }

    @Nullable
    @Generated
    public String content() {
        return this.content;
    }

    @Nullable
    @Generated
    public String username() {
        return this.username;
    }

    @Nullable
    @Generated
    public String avatar() {
        return this.avatar;
    }

    @Nullable
    @Generated
    public Boolean tts() {
        return this.tts;
    }

    @NotNull
    @Generated
    public @NotNull Embed @Nullable [] embeds() {
        return this.embeds;
    }

    @Generated
    public WebhookMessage username(@Nullable String username) {
        this.username = username;
        return this;
    }

    @Generated
    public WebhookMessage avatar(@Nullable String avatar) {
        this.avatar = avatar;
        return this;
    }

    @Generated
    public WebhookMessage tts(@Nullable Boolean tts) {
        this.tts = tts;
        return this;
    }
}

