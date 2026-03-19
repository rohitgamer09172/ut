/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Filterable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class WritableBookContent {
    private List<Filterable<String>> pages;

    public WritableBookContent(List<Filterable<String>> pages) {
        this.pages = pages;
    }

    public static WritableBookContent read(PacketWrapper<?> wrapper) {
        List<Filterable<String>> pages = wrapper.readList(ew -> Filterable.read(ew, eew -> eew.readString(1024)));
        return new WritableBookContent(pages);
    }

    public static void write(PacketWrapper<?> wrapper, WritableBookContent content) {
        wrapper.writeList(content.pages, (ew, page) -> Filterable.write(ew, page, (eew, text) -> eew.writeString((String)text, 1024)));
    }

    @Nullable
    public Filterable<String> getPage(int index) {
        return index >= 0 && index < this.pages.size() ? this.pages.get(index) : null;
    }

    public void addPage(Filterable<String> page) {
        this.pages.add(page);
    }

    public List<Filterable<String>> getPages() {
        return this.pages;
    }

    public void setPages(List<Filterable<String>> pages) {
        this.pages = pages;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WritableBookContent)) {
            return false;
        }
        WritableBookContent that = (WritableBookContent)obj;
        return this.pages.equals(that.pages);
    }

    public int hashCode() {
        return Objects.hashCode(this.pages);
    }
}

