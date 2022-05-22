package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate
{
    private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
    private static StringTranslate instance = new StringTranslate();
    private final Map<String, String> languageList = Maps.<String, String>newHashMap();
    private long lastUpdateTimeInMilliseconds;

    public StringTranslate()
    {
        InputStream inputstream = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
        inject(this, inputstream);
    }

    public static void inject(InputStream inputstream)
    {
        inject(instance, inputstream);
    }

    private static void inject(StringTranslate inst, InputStream inputstream)
    {
        java.util.HashMap<String, String> map = parseLangFile(inputstream);
        inst.languageList.putAll(map);
        inst.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public static java.util.HashMap<String,String> parseLangFile(InputStream inputstream)
    {
        java.util.HashMap<String,String> table = Maps.newHashMap();
        try
        {
            inputstream = net.minecraftforge.fml.common.FMLCommonHandler.instance().loadLanguage(table, inputstream);
            if (inputstream == null) return table;

            for (String s : IOUtils.readLines(inputstream, Charsets.UTF_8))
            {
                if (!s.isEmpty() && s.charAt(0) != 35)
                {
                    String[] astring = (String[])Iterables.toArray(equalSignSplitter.split(s), String.class);

                    if (astring != null && astring.length == 2)
                    {
                        String s1 = astring[0];
                        String s2 = numericVariablePattern.matcher(astring[1]).replaceAll("%$1s");
                        table.put(s1, s2);
                    }
                }
            }

        }
        catch (Exception var7)
        {
            ;
        }
        return table;
    }

    static StringTranslate getInstance()
    {
        return instance;
    }

    @SideOnly(Side.CLIENT)

    public static synchronized void replaceWith(Map<String, String> p_135063_0_)
    {
        instance.languageList.clear();
        instance.languageList.putAll(p_135063_0_);
        instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String key)
    {
        return this.tryTranslateKey(key);
    }

    public synchronized String translateKeyFormat(String key, Object... format)
    {
        String s = this.tryTranslateKey(key);

        try
        {
            return String.format(s, format);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + s;
        }
    }

    private String tryTranslateKey(String key)
    {
        String s = (String)this.languageList.get(key);
        return s == null ? key : s;
    }

    public synchronized boolean isKeyTranslated(String key)
    {
        return this.languageList.containsKey(key);
    }

    public long getLastUpdateTimeInMilliseconds()
    {
        return this.lastUpdateTimeInMilliseconds;
    }
}