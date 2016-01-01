package jeresources.api.messages;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Message
{
    public NBTTagCompound getMessage()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public abstract NBTTagCompound writeToNBT(NBTTagCompound tagCompound);

    public abstract boolean isValid();

    public static class Storage
    {
        private final String key;
        private final Message message;
        private final NBTTagCompound messageNBT;

        public Storage(String key, Message message)
        {
            this.key = key;
            this.message = message;
            this.messageNBT = message.writeToNBT(new NBTTagCompound());
        }

        public Storage(String key, NBTTagCompound message)
        {
            this.key = key;
            this.message = null;
            this.messageNBT = message;
        }

        public Message getMessage()
        {
            return message;
        }

        public String getKey()
        {
            return key;
        }

        public NBTTagCompound getMessageNBT()
        {
            return messageNBT;
        }
    }
}
