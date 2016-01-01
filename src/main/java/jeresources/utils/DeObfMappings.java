package jeresources.utils;

public enum DeObfMappings
{
    numberOfBlocks("numberOfBlocks", "field_76541_b"),
    itemWeight("itemWeight", "field_76292_a");

    private String deObfName;
    private String obfName;

    private DeObfMappings(String deObfName, String obfName)
    {
        this.deObfName = deObfName;
        this.obfName = obfName;
    }

    public String getFieldName()
    {
        return ReflectionHelper.isObf ? obfName : deObfName;
    }
}
