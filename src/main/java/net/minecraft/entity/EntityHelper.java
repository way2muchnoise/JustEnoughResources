package net.minecraft.entity;

public class EntityHelper
{
    public static String getEntityName(EntityLivingBase entity)
    {
        return entity.getEntityString();
    }

    public static int getExperience(EntityLiving entity)
    {
        return entity.experienceValue;
    }
}
