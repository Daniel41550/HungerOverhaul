package iguanaman.hungeroverhaul.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOption<T>
{
    public String category;
    public String name;
    public T defaultValue;
    public T blankSlate;
    public String comment;
    public T minValue;
    public T maxValue;

    public ConfigOption(String category, String name, T defaultValue, T blankSlate, String comment)
    {
        this(category, name, defaultValue, null, null, blankSlate, comment);
    }

    public ConfigOption(String category, String name, T defaultValue, T minValue, T maxValue, T blankSlate, String comment)
    {
        this.category = category;
        this.name = name;
        this.defaultValue = defaultValue;
        this.blankSlate = blankSlate;
        this.comment = comment;
        this.minValue = minValue != null ? minValue : getDefaultMinValue(defaultValue);
        this.maxValue = maxValue != null ? maxValue : getDefaultMaxValue(defaultValue);
    }

    @SuppressWarnings("unchecked")
    private T getDefaultMinValue(T defaultValue)
    {
        if (defaultValue instanceof Integer)
            return (T) Integer.valueOf(Integer.MIN_VALUE);
        else if (defaultValue instanceof Float)
            return (T) Float.valueOf(Float.MIN_VALUE);
        else if (defaultValue instanceof Double)
            return (T) Double.valueOf(Double.MIN_VALUE);
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    private T getDefaultMaxValue(T defaultValue)
    {
        if (defaultValue instanceof Integer)
            return (T) Integer.valueOf(Integer.MAX_VALUE);
        else if (defaultValue instanceof Float)
            return (T) Float.valueOf(Float.MAX_VALUE);
        else if (defaultValue instanceof Double)
            return (T) Double.valueOf(Double.MAX_VALUE);
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    public T get(Configuration config)
    {
        if (defaultValue instanceof Boolean)
            return (T) Boolean.valueOf(config.getBoolean(name, category, (Boolean) defaultValue, comment));
        else if (defaultValue instanceof Integer)
            return (T) Integer.valueOf(config.getInt(name, category, (Integer) defaultValue, (Integer) minValue, (Integer) maxValue, comment));
        else if (defaultValue instanceof Float)
            return (T) Float.valueOf((float) config.getFloat(name, category, (Float) defaultValue, (Float) minValue, (Float) maxValue, comment));
        else if (defaultValue instanceof Double)
            return (T) Double.valueOf(Math.min((Double) maxValue, Math.max((Double) minValue, getProperty(config).getDouble())));
        else if (defaultValue instanceof String)
            return (T) config.getString(name, category, (String) defaultValue, comment);
        else
            throw new RuntimeException("Unknown ConfigOption type for '" + category + ":" + name + "': " + defaultValue.getClass().getName());
    }

    public Property getProperty(Configuration config)
    {
        return config.getCategory(category).get(name);
    }

    public void set(Configuration config, T value)
    {
        Property property = getProperty(config);
        if (value instanceof Boolean)
            property.set((Boolean) value);
        else if (value instanceof Integer)
            property.set((Integer) value);
        else if (value instanceof Float)
            property.set((Float) value);
        else if (value instanceof Double)
            property.set((Double) value);
        else if (value instanceof String)
            property.set((String) value);
        else
            throw new RuntimeException("Unknown ConfigOption type for '" + category + ":" + name + "': " + defaultValue.getClass().getName());
    }

    public void setToBlankSlate(Configuration config)
    {
        get(config);
        set(config, blankSlate);
    }

    public void setToDefault(Configuration config)
    {
        get(config);
        set(config, defaultValue);
    }

    public void remove(Configuration config)
    {
        config.getCategory(category).remove(name);
    }
}
