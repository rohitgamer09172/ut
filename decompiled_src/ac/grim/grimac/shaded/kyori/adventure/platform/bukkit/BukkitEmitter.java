/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 */
package ac.grim.grimac.shaded.kyori.adventure.platform.bukkit;

import ac.grim.grimac.shaded.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;

final class BukkitEmitter
implements Sound.Emitter {
    final Entity entity;

    BukkitEmitter(Entity entity) {
        this.entity = entity;
    }
}

