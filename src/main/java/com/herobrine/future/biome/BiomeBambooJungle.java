package com.herobrine.future.biome;

import com.herobrine.future.entity.panda.EntityPanda;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.world.biome.Biome;

public class BiomeBambooJungle extends Biome {
    private final boolean isEdge;

    public BiomeBambooJungle(boolean isEdgeIn) {
        super(new BiomeProperties("Bamboo").setRainfall(0.9F).setTemperature(0.95F));
        this.isEdge = isEdgeIn;

        if (!isEdgeIn) {
            this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
            this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPanda.class, 1, 1, 2));
        }

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 40, 1, 2));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }
}