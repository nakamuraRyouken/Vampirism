package de.teamlapen.vampirism.core;


import de.teamlapen.lib.util.ParticleHandler;
import de.teamlapen.vampirism.client.render.particle.FlyingBloodEntityParticle;
import de.teamlapen.vampirism.client.render.particle.FlyingBloodParticle;
import de.teamlapen.vampirism.client.render.particle.HalloweenParticle;
import de.teamlapen.vampirism.util.REFERENCE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModParticles {
    public static final ResourceLocation FLYING_BLOOD = new ResourceLocation(REFERENCE.MODID, "flying_blood");
    public static final ResourceLocation FLYING_BLOOD_ENTITY = new ResourceLocation(REFERENCE.MODID, "flying_blood_entity");
    public static final ResourceLocation HALLOWEEN = new ResourceLocation(REFERENCE.MODID, "halloween");

    public static void init() {
        ParticleHandler.registerParticle(FLYING_BLOOD, new ParticleHandler.ICustomParticleFactory() {
            @SideOnly(Side.CLIENT)
            @Override
            public Particle createParticle(World world, double posX, double posY, double posZ, Object... param) {
                return new FlyingBloodParticle(world, posX, posY, posZ, (double) param[0], (double) param[1], (double) param[2], (int) param[3]);
            }

            @Nonnull
            @Override
            public NBTTagCompound createParticleInfo(Object... param) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setDouble("0", (Double) param[0]);
                nbt.setDouble("1", (Double) param[1]);
                nbt.setDouble("2", (Double) param[2]);
                nbt.setInteger("3", (Integer) param[3]);
                return nbt;
            }

            @SideOnly(Side.CLIENT)
            @Nullable
            @Override
            public Object[] readParticleInfo(NBTTagCompound nbt) {
                Object[] data = new Object[4];
                data[0] = nbt.getDouble("0");
                data[1] = nbt.getDouble("1");
                data[2] = nbt.getDouble("2");
                data[3] = nbt.getInteger("3");
                return new Object[0];
            }
        });
        ParticleHandler.registerParticle(FLYING_BLOOD_ENTITY, new ParticleHandler.ICustomParticleFactory() {
            @SideOnly(Side.CLIENT)
            @Override
            public Particle createParticle(World world, double posX, double posY, double posZ, Object... param) {
                return new FlyingBloodEntityParticle(world, posX, posY, posZ, (Entity) param[0], (Boolean) param[1]);
            }

            @Nonnull
            @Override
            public NBTTagCompound createParticleInfo(Object... param) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("0", ((Entity) param[0]).getEntityId());
                nbt.setBoolean("1", (Boolean) param[1]);
                return nbt;
            }

            @SideOnly(Side.CLIENT)
            @Nullable
            @Override
            public Object[] readParticleInfo(NBTTagCompound nbt) {
                int i = nbt.getInteger("0");
                World world = Minecraft.getMinecraft().world;
                if (world == null) return null;
                Entity e = world.getEntityByID(i);
                if (e == null) return null;
                Object[] data = new Object[2];
                data[0] = e;
                data[1] = nbt.getBoolean("1");
                return data;
            }
        });

        ParticleHandler.registerParticle(HALLOWEEN, new ParticleHandler.ICustomParticleFactory() {
            @SideOnly(Side.CLIENT)
            @Override
            public Particle createParticle(World world, double posX, double posY, double posZ, Object... param) {
                return new HalloweenParticle(world, posX, posY, posZ);
            }

            @Nonnull
            @Override
            public NBTTagCompound createParticleInfo(Object... param) {
                return new NBTTagCompound();
            }

            @Nullable
            @Override
            public Object[] readParticleInfo(NBTTagCompound nbt) {
                return new Object[0];
            }
        });
    }
}
