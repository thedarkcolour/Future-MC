package com.herobrine.future.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockProperties {
    private String registryName;
    private Material material = Material.ROCK;
    private SoundType soundType = SoundType.STONE;

    public BlockProperties(String registryName) { // Constructors
        this.registryName = registryName;
    }

    public BlockProperties(String registryName, Material material) {
        this(registryName);
        this.material = material;
    }

    public BlockProperties(String registryName, Material material, SoundType soundType) {
        this(registryName, material);
        this.soundType = soundType;
    }

    public String registryName() {
        return registryName;
    }

    public Material material() {
        return material;
    }

    public SoundType soundType() {
        return soundType;
    }
}