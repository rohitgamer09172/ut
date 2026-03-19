/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;

@ApiStatus.NonExtendable
public interface ParserDirective
extends Tag {
    public static final Tag RESET = new ParserDirective(){

        public String toString() {
            return "ParserDirective.RESET";
        }
    };
}

